# https://github.com/datasift/datasift-ruby
$LOAD_PATH.unshift( File.join( File.dirname(__FILE__), 'lib' ) )
require 'rubygems'
require 'datasift-multi'
require 'mongo'
require 'url_expander'
require 'foursquare2'

city = "combined"

## connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
# city_collection = db.collection(city)
puts "mongo connected"

#          "jakarta" => "b9d3e773ca3e982402c116ac5e7b8b06",

hashes = {"rosenheim" => "db6cd2371313e968d8fe83fb86499d29", 
          "berlin" => "802182644f35e61cf07bbaba8af71ba5", 
          "london" => "d46aa73ebfd684b64181eac5be55b6f6",
          "potsdam" => "e2edb25293e87403518302ddb97b1dd1",
          "munchen" => "9372493b156967a86a4d377db613311d",
          "newyork" => "a912be065b3f44b482bb7726f2bd69ba",
          "sanfrancisco" => "c44b49b2bc57d315dbdb825d18593b4f",
          "cupertino" => "3ccd20e38611fdc59c5db53f4147dd39",
          "menlopark" => "6d1e7ff00f54f9bdd8e41d3403ae6ae9"
        }
cities = hashes.invert

## foursquare credentials
@fsq = Foursquare2::Client.new(:client_id => 'A4VXLAPLSKM1ZEBVJPQ0ZDE3G0A4WO3VL24M0F51M2TZCMY3', :client_secret => 'B5XRRXOYN03UKUZNMGBUMAEIQ11KJMQ1SDFV2WM1UJKL4P1Y')


## datasift config
user = DataSift::User.new('csteinlehner', '4d2ab5f35db3158c199aaf0aaed70eba')
consumer = user.getConsumer(DataSift::StreamConsumer::TYPE_HTTP, hashes.map{|k,v| "#{v}"}.join(','))

## datasift error handling
consumer.onStopped do |reason|
	puts "\n", 'Stopped: ' + reason, "\n"
end



def foursquare_extract(data)
	begin
		t_urls = data["twitter"]["links"]
     	 t_urls.select{|v| v =~ /4sq.com/}.each do |url2|
         	ex_url = UrlExpander::Client.expand(url2)
            if ex_url[/.*checkin.*/]
              check_id = ex_url.scan(/checkin\/(.*)\?s=(.*)&/)
              fsq_data = (@fsq.checkin(check_id[0][0],{'signature' => check_id[0][1]}))
              fsq_categories = Array.new
              fsq_categorieParents = Array.new

              fsq_data.venue.categories.each do |cat|
                fsq_categories.push(cat.name)
                cat.parents.each do |par|
                    fsq_categorieParents.push(par)
                end
              end
              fsq_hash = {"fsq_userHomeCity"=> fsq_data.user.homeCity, "fsq_venueName"=> fsq_data.venue.name, "fsq_venueID" => fsq_data.venue.id, "fsq_categories" => fsq_categories, "fsq_categorieParents" => fsq_categorieParents}
              data.merge! fsq_hash
            elsif !ex_url[/\/v\//]
              	venue_id = ex_url[/\d*$/]
        		fsq_data = @fsq.venue(venue_id)
        		fsq_categories = Array.new
            	fsq_categorieParents = Array.new

              	fsq_data.categories.each do |cat|
                	fsq_categories.push(cat.name)
                  	cat.parents.each do |par|
                    	fsq_categorieParents.push(par)
                  	end
              	end
      		fsq_hash = {"fsq_venueName"=> fsq_data.name, "fsq_venueID" => fsq_data.id, "fsq_categories" => fsq_categories, "fsq_categorieParents" => fsq_categorieParents}
      		data.merge! fsq_hash
        end
    end
      rescue => err
        e_file = File.open("crawler_error.log","a") 
        e_file.puts("#{data["interaction"]["id"]} : foursquare error: #{err.message}")
        e_file.close
      end
    return data
end



## datasift event / database entry
consumer.consume(true) do |interaction|
	if interaction
		data = interaction["data"]
    ## insert foursquare data
    if data["interaction"]["source"]=="foursquare" 
      data = foursquare_extract(data)
    end

    puts 
    ## insert into db
		db.collection(cities[interaction["hash"]]).insert(data)
	end
end
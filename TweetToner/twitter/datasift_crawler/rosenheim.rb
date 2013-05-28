# https://github.com/datasift/datasift-ruby

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
city_collection = db.collection(city)
puts "mongo connected"

## foursquare credentials
@fsq = Foursquare2::Client.new(:client_id => 'A4VXLAPLSKM1ZEBVJPQ0ZDE3G0A4WO3VL24M0F51M2TZCMY3', :client_secret => 'B5XRRXOYN03UKUZNMGBUMAEIQ11KJMQ1SDFV2WM1UJKL4P1Y')


## datasift config
user = DataSift::User.new('csteinlehner', '4d2ab5f35db3158c199aaf0aaed70eba')
consumer = user.getConsumer(DataSift::StreamConsumer::TYPE_HTTP, 'db6cd2371313e968d8fe83fb86499d29')

## datasift error handling
consumer.onStopped do |reason|
	puts "\n", 'Stopped: ' + reason, "\n"
end



def foursquare_extract(data)
	# begin
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
      # rescue => err
      #   e_file = File.open("crawler_error.log","a") 
      #   e_file.puts("foursquare error: #{err.message}")
      #   e_file.close
      # end
    return data
end



## datasift event / database entry
consumer.consume(true) do |interaction|
	if interaction
		# puts 'Type: ' + interaction['interaction']['type']
		# puts 'Content: ' + interaction['interaction']['content']
		# puts 'Source: ' + interaction['interaction']['source']
		data = interaction
		if data["interaction"]["source"]=="foursquare" 
			data = foursquare_extract(data)
		end
		city_collection.insert(data);
		# puts '--'
	end
end
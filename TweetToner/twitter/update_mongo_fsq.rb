require 'rubygems'
require 'mongo'
require 'url_expander'
require 'foursquare2'

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("tweets")
btweets = db.collection("btweets")
puts "*** mongo connected ***"

fsq = Foursquare2::Client.new(:client_id => 'A4VXLAPLSKM1ZEBVJPQ0ZDE3G0A4WO3VL24M0F51M2TZCMY3', :client_secret => 'B5XRRXOYN03UKUZNMGBUMAEIQ11KJMQ1SDFV2WM1UJKL4P1Y')

btweets.find({"url" => /http:\/\/4sq.com\//, "fsq_error" => {"$ne" => "unknow_id"}}).each do |row|
	if !row["fsq_venueName"]
	row["url"].select{|v| v =~ /http:\/\/4sq.com\//}.each do |url|
		ex_url = UrlExpander::Client.expand(url)
		if ex_url[/.*checkin.*/]
			check_id = ex_url.scan(/checkin\/(.*)\?s=(.*)&/)
			begin
				fsq_data = (fsq.checkin(check_id[0][0],{'signature' => check_id[0][1]}))
				fsq_categories = Array.new
				fsq_categorieParents = Array.new
				if fsq_data!=nil and fsq_data.venue!=nil
					fsq_data.venue.categories.each do |cat|
						fsq_categories.push(cat.name)
						cat.parents.each do |par|
							fsq_categorieParents.push(par)
						end
					end
					fsq_hash = {"fsq_userHomeCity"=> fsq_data.user.homeCity, "fsq_venueName"=> fsq_data.venue.name, "fsq_venueID" => fsq_data.venue.id, "fsq_categories" => fsq_categories, "fsq_categorieParents" => fsq_categorieParents}
					row.merge! fsq_hash
					btweets.save(row)
					puts "#{row} updated"
				end
			rescue => err
				puts "foursquare threw an error: #{err.message}"
			end
		elsif !ex_url[/\/v\//]
		 	venue_id = ex_url[/\d*$/]
		 	begin
				fsq_data = fsq.venue(venue_id)
		 		fsq_categories = Array.new
	           	fsq_categorieParents = Array.new
           		fsq_data.categories.each do |cat|
               		fsq_categories.push(cat.name)
               		cat.parents.each do |par|
                   		fsq_categorieParents.push(par)
              		end
           		end
				fsq_hash = {"fsq_venueName"=> fsq_data.name, "fsq_venueID" => fsq_data.id, "fsq_categories" => fsq_categories, "fsq_categorieParents" => fsq_categorieParents}
				row.merge! fsq_hash
				btweets.save(row)
				puts "#{row} updated"
			rescue => err
				puts "foursquare threw an error: #{err.message}"
			end
		elsif ex_url[/\/v\//]
			fsq_hash = {"fsq_error" => "unkown_id"}
			row.merge! fsq_hash
			btweets.save(row)
			puts "#{row} updated"
		else
			puts "I don't wanna handle this: #{ex_url}"
		end
	end
	end
end
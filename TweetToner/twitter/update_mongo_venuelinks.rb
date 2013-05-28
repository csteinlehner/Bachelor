require 'rubygems'
require 'yajl'
require 'mongo'
require "time"
require 'url_expander'
require 'foursquare2'

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("tweets")
btweets = db.collection("btweets")
puts "*** mongo connected ***"

# foursquare credentials
fsq = Foursquare2::Client.new(:client_id => 'A4VXLAPLSKM1ZEBVJPQ0ZDE3G0A4WO3VL24M0F51M2TZCMY3', :client_secret => 'B5XRRXOYN03UKUZNMGBUMAEIQ11KJMQ1SDFV2WM1UJKL4P1Y')


btweets.find({"url" => /http:\/\/4sq.com\//}).each do |row|
	if !row["fsq_venueName"]
		row["url"].select{|v| v =~ /http:\/\/4sq.com\//}.each do |url|
			ex_url = UrlExpander::Client.expand(url)
			 if !ex_url[/.*checkin.*/] && !ex_url[/\/v\//]
			 	# puts "#{ex_url} : #{row["text"]}"
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
		end
		end
	end
end
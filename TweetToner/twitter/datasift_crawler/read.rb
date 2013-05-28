# https://github.com/datasift/datasift-ruby

require 'rubygems'
require 'mongo'

city = "berlin"

## connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
city_collection = db.collection(city)
puts "mongo connected"


city_collection.find({"interaction.source"=>"foursquare"}).each do |entry|
	puts entry["twitter"]["links"].inspect
end
# puts btweets.find({"geo" => nil}).each {|row| puts row.to_a}
			
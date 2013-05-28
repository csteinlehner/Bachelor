require 'rubygems'
require 'yajl'
require 'mongo'
require "time"
require 'csv'

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("tweets")
btweets = db.collection("btweets")
puts "*** mongo connected ***"


csv_file = File.open("btweets.csv",'w')
csv_file.puts("_id,text,lat,lon,user,created_at,id")


# puts btweets.find("geo"!="null").to_a
# puts btweets.find({"user" => "mgross9"}).to_a


btweets.find.each do |row| 
	# puts row.keys	
	# puts row.values.to_a
	# partition("text")[2]

	write_row = "#{row["_id"]},#{row["text"]},#{row["geo"].to_a[0]},#{row["geo"].to_a[1]},#{row["user"]},#{row["created_at"]},#{row["id"]}"
	csv_file.puts(write_row)
	# row_array = row.values.to_a
	# puts row_array[2]
	# geo_array = row_array[2].to_s.split(".")
	# row_array.delete_at(2)
	# row_array.insert(2,geo_array[0])
	# row_array.insert(3,geo_array[1])
	# csv_file.puts(CSV.generate_line(row_array))
end
csv_file.close
# cursor = btweets.find(:user => "ProhorovTweets").to_a
# cursor.each {|row| puts row.to_a[1]}
# cursor.each { |h| puts h['geo'] }
# things = cursor.map { |h| h['geo'] }

### search nearest locations
# btweets.create_index([["loc", Mongo::GEO2D]])
# btweets.find({"geo" => {"$nearSphere" => [52, 13]}}, {:limit => 50}).each do |p|
#   puts p["text"]
# end

# puts btweets.find({"geo" => nil}).count

# btweets.find({"geo" => nil}).each {|row| puts row.to_a}
# cursor = btweets.find("",:fields =>["geo"])
# cursor.each {|row| puts geo.to_a}
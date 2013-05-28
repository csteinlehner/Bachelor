require 'rubygems'
require 'mongo'
require 'whatlanguage'

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("tweets")
btweets = db.collection("btweets")
puts "*** mongo connected ***"


btweets.find.each do |row| 
	# write_row = "#{row["_id"]},#{row["text"]},#{row["geo"].to_a[0]},#{row["geo"].to_a[1]},#{row["user"]},#{row["created_at"]},#{row["id"]}"
	if row["geo"].to_a[0].to_f < 52.3418234221 || row["geo"].to_a[0].to_f > 52.6697240587 || row["geo"].to_a[1].to_f < 13.0882097323 || row["geo"].to_a[1].to_f > 13.7606105539
		puts "remove geo: #{row["geo"].inspect} lang: #{row["text"].language}"
		btweets.remove(row)
	end
end
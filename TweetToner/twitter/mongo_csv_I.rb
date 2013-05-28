require 'rubygems'
require 'mongo'
require 'csv'
require 'foursquare2'
# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("tweets")
btweets = db.collection("btweets")
puts "*** mongo connected ***"

# fsq = Foursquare2::Client.new(:client_id => 'A4VXLAPLSKM1ZEBVJPQ0ZDE3G0A4WO3VL24M0F51M2TZCMY3', :client_secret => 'B5XRRXOYN03UKUZNMGBUMAEIQ11KJMQ1SDFV2WM1UJKL4P1Y')

startD = Time.utc(2012,8,13)
endD = Time.utc(2012,8,19)

puts btweets.find({"created_at" => {"$gte" => startD,"$lt" => endD}, "$or" => [{"text" => /(^|\s)I'\S/i}, {"text" => /(^|\s)I\s/i}] }).count

csv_file = File.open("btweets_I_kw33.csv",'w')
csv_file.puts("_id,text,lat,lon,user,created_at,id")


btweets.find({"created_at" => {"$gte" => startD,"$lt" => endD}, "$or" => [{"text" => /(^|\s)I'\S/i}, {"text" => /(^|\s)I\s/i} ] }).each do |row|
	if !row["text"][/.*t.co.*/] 
		write_row = "#{row["_id"]},#{row["text"]},#{row["geo"].to_a[0]},#{row["geo"].to_a[1]},#{row["user"]},#{row["created_at"]},#{row["id"]}"
		csv_file.puts(write_row)
	end
end



# # puts btweets.find("geo"!="null").to_a
# # puts btweets.find({"user" => "mgross9"}).to_a

# btweets.find("text" => /I'\S/).each do |row|
# 	if !row["text"][/.*t.co.*/] 
# 		write_row = "#{row["_id"]},#{row["text"]},#{row["geo"].to_a[0]},#{row["geo"].to_a[1]},#{row["user"]},#{row["created_at"]},#{row["id"]}"
# 		csv_file.puts(write_row)
# 	end
# end
# btweets.find("text" => /I\s/).each do |row|
# 	if !row["text"][/.*t.co.*/] 
# 		write_row = "#{row["_id"]},#{row["text"]},#{row["geo"].to_a[0]},#{row["geo"].to_a[1]},#{row["user"]},#{row["created_at"]},#{row["id"]}"
# 		csv_file.puts(write_row)
# 	end
# end
csv_file.close
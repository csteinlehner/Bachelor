# https://api.twitter.com/1/statuses/show.json?id=245092735537786880
require 'rubygems'
require 'mongo'
require 'net/http'
require 'json'

base_url = "http://api.twitter.com/1/statuses/show.json?id="

city = "menlopark"

### connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(city)
puts "*** mongo connected ***"


col.find.each do |row|
	puts row["klout"]
end

# col.find.sort([['_id', -1]]).each do |row|
# 	url = "#{base_url}#{row["twitter"]["id"]}"
# 	begin
# 		uri = URI.parse(url)
# 		http= Net::HTTP.new(uri.host,uri.port)
# 		r=http.request(Net::HTTP::Get.new(uri.request_uri))
# 		result = JSON.parse(r.body)
# 		puts result.inspect
# 	rescue => err
# 		puts err
# 	end
# end
require 'rubygems'
require 'mongo'

city = "sanfrancisco"

### connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(city)
puts "*** mongo connected ***"

user_count = Hash.new
puts city

col.find("interaction.author.username"=>{"$exists" => true},"ignore"=>{"$exists"=>false}).each do |row|
	 username = row["interaction"]["author"]["username"]
	  if user_count.has_key?(username)
	  	user_count[username] += 1
	  else
	  	user_count.store(username,1)
	  end
end

puts user_count.count
# puts user_count.sort_by{|k,v| v}.reverse[0..10]
#user_count = col.group(["twitter.user.screen_name"],nil,{:count => 0}, "function(x,y) {y.count++}").to_a
#puts user_count.inspect

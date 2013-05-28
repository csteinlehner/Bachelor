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

user_klout = Hash.new
puts city

col.find("klout"=>{"$exists" => true},"ignore"=>{"$exists"=>false}).each do |row|
	 username = row["interaction"]["author"]["username"]
	 klout = row["klout"]["score"]
	  if !user_klout.has_key?(username)
	  	user_klout[username] = klout
	  end
end

sorted_klout = user_klout.sort_by{|k,v| v}
average = sorted_klout.inject(0){|sum,x| sum+x[1]}/user_klout.count

puts "#Klout:"
puts "min: #{sorted_klout[0][1]} / max:#{sorted_klout[-1][1]} / ø #{average}"
puts "#Top Klout User"
puts sorted_klout.reverse[0...10]


# puts user_count.count
# puts user_count.sort_by{|k,v| v}.reverse[0..10]
#user_count = col.group(["twitter.user.screen_name"],nil,{:count => 0}, "function(x,y) {y.count++}").to_a
#puts user_count.inspect

require 'rubygems'
require 'mongo'

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("city_tweets")
# city_collection = db.collection(city)
# doc = {"name" => "MongoDB", "type" => "database", "count" => 1, "info" => {"x" => 203, "y" => '102'}}
# btweets.insert(doc)
# puts btweets.count
# tweets = File.open("tweets.txt", "w")

puts "Berlin: #{db.collection("berlin").find({}, :sort => ['_id',:desc]).limit(1).next["created_at"]} \nc: #{db.collection("berlin").find.count} \n#{db.collection("berlin").find({}, :sort => ['_id',:desc]).limit(1).next.inspect}"
puts "\nReykjavik: #{db.collection("reykjavik").find({}, :sort => ['_id',:desc]).limit(1).next["created_at"]}\nc: #{db.collection("reykjavik").find.count} \n#{db.collection("reykjavik").find({}, :sort => ['_id',:desc]).limit(1).next.inspect}"
puts "\nRosenheim: #{db.collection("rosenheim").find({}, :sort => ['_id',:desc]).limit(1).next["created_at"]}\nc: #{db.collection("rosenheim").find.count} \n#{db.collection("rosenheim").find({}, :sort => ['_id',:desc]).limit(1).next.inspect}"
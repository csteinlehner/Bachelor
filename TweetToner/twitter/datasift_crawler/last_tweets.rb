require 'rubygems'
require 'mongo'

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
# city_collection = db.collection(city)
# doc = {"name" => "MongoDB", "type" => "database", "count" => 1, "info" => {"x" => 203, "y" => '102'}}
# btweets.insert(doc)
# puts btweets.count
# tweets = File.open("tweets.txt", "w")

puts "Jakarta: #{db.collection("jakarta").find({}, :sort => ['_id',:desc]).limit(1).next["interaction"]["created_at"]}\nc: #{db.collection("jakarta").find.count} \n#{db.collection("jakarta").find({}, :sort => ['_id',:desc]).limit(1).next.inspect}"
puts "\nBerlin: #{db.collection("berlin").find({}, :sort => ['_id',:desc]).limit(1).next["interaction"]["created_at"]} \nc: #{db.collection("berlin").find.count} \n#{db.collection("berlin").find({}, :sort => ['_id',:desc]).limit(1).next.inspect}"
puts "\nCupertino: #{db.collection("cupertino").find({}, :sort => ['_id',:desc]).limit(1).next["interaction"]["created_at"]}\nc: #{db.collection("cupertino").find.count} \n#{db.collection("cupertino").find({}, :sort => ['_id',:desc]).limit(1).next.inspect}"
puts "\nRosenheim: #{db.collection("rosenheim").find({}, :sort => ['_id',:desc]).limit(1).next["interaction"]["created_at"]}\nc: #{db.collection("rosenheim").find.count} \n#{db.collection("rosenheim").find({}, :sort => ['_id',:desc]).limit(1).next.inspect}"
puts "\nLondon: #{db.collection("london").find({}, :sort => ['_id',:desc]).limit(1).next["interaction"]["created_at"]}\nc: #{db.collection("london").find.count} \n#{db.collection("london").find({}, :sort => ['_id',:desc]).limit(1).next.inspect}"
puts "\nmenlopark: #{db.collection("menlopark").find({}, :sort => ['_id',:desc]).limit(1).next["interaction"]["created_at"]}\nc: #{db.collection("menlopark").find.count} \n#{db.collection("menlopark").find({}, :sort => ['_id',:desc]).limit(1).next.inspect}"
puts "\nmunchen: #{db.collection("munchen").find({}, :sort => ['_id',:desc]).limit(1).next["interaction"]["created_at"]}\nc: #{db.collection("munchen").find.count} \n#{db.collection("munchen").find({}, :sort => ['_id',:desc]).limit(1).next.inspect}"
puts "\nnewyork: #{db.collection("newyork").find({}, :sort => ['_id',:desc]).limit(1).next["interaction"]["created_at"]}\nc: #{db.collection("newyork").find.count} \n#{db.collection("newyork").find({}, :sort => ['_id',:desc]).limit(1).next.inspect}"
puts "\npotsdam: #{db.collection("potsdam").find({}, :sort => ['_id',:desc]).limit(1).next["interaction"]["created_at"]}\nc: #{db.collection("potsdam").find.count} \n#{db.collection("potsdam").find({}, :sort => ['_id',:desc]).limit(1).next.inspect}"
puts "\nsanfrancisco: #{db.collection("sanfrancisco").find({}, :sort => ['_id',:desc]).limit(1).next["interaction"]["created_at"]}\nc: #{db.collection("sanfrancisco").find.count} \n#{db.collection("sanfrancisco").find({}, :sort => ['_id',:desc]).limit(1).next.inspect}"
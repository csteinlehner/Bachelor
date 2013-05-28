require 'rubygems'
require 'language_detector'
require 'mongo'


# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("tweets")
btweets = db.collection("btweets")
puts "*** mongo connected ***"

d = LanguageDetector.new
btweets.find.each do |row| 
	p d.detect(row["text"])
end
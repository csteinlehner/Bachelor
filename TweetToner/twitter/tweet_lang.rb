require 'rubygems'
require 'yajl'
require 'mongo'
require "time"
# require 'whatlanguage'
require 'language_detector'

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("tweets")
btweets = db.collection("btweets")
puts "*** mongo connected ***"


language_count = Hash.new
lang_files = Hash.new
d = LanguageDetector.new

btweets.find.each do |row| 
	# write_row = "#{row["_id"]},#{row["text"]},#{row["geo"].to_a[0]},#{row["geo"].to_a[1]},#{row["user"]},#{row["created_at"]},#{row["id"]}"
	# detect_lang = row["text"].language
	detect_lang = d.detect(row["text"])
	if language_count.has_key?(detect_lang)
		language_count[detect_lang] += 1
		lang_files["#{detect_lang}.txt"].puts(row["text"])
		puts language_count[detect_lang]
	else
		language_count.store(detect_lang,1)
		lang_files.store("#{detect_lang}.txt",File.open("./lang/#{detect_lang}.txt","w"))
		lang_files["#{detect_lang}.txt"].puts(row["text"])
		puts language_count[detect_lang]
	end 
end
lang_files.each.close
puts language_count.inspect
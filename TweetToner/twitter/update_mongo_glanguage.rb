require 'rubygems'
require 'mongo'
require 'net/https'
require 'json'
require 'uri'

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("tweets")
btweets = db.collection("btweets")
puts "*** mongo connected ***"

# google translate conf
api_key = "AIzaSyCZqRqqcekBd3OwwZJ8BZV_W3IhXB3bnUA"
base_url = "https://www.googleapis.com/language/translate/v2/detect?key="



btweets.find().each do |row|
	if !row["glan_language"]
		begin 
			strip_str = row["text"].gsub(/http:\/\/\S+\s?/, '')
			if strip_str.size > 0
				text = strip_str
			else
				text = row["text"]
			end
			# puts "before: #{row["text"]} / after: #{text}"
			url = "#{base_url}#{api_key}&q=#{URI.encode(text)}"
			uri = URI.parse(url)
			http= Net::HTTP.new(uri.host,uri.port)
			http.use_ssl = true
			http.verify_mode = OpenSSL::SSL::VERIFY_NONE
			r=http.request(Net::HTTP::Get.new(uri.request_uri))
			result = JSON.parse(r.body)
			# puts "#{result["data"]["detections"][0][0]["language"]} / #{result["data"]["detections"][0][0]["confidence"]} : #{row["text"]}"
			lang_hash = {"glan_language"=> result["data"]["detections"][0][0]["language"], "glan_confidence" => result["data"]["detections"][0][0]["confidence"]}
			row.merge! lang_hash
			btweets.save(row)
			puts "#{row}.inspect updated"
		rescue => err
			puts err.message
		end
	end
end
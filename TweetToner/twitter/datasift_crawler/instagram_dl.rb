require 'rubygems'
require 'mongo'
require 'net/http'
require 'json'
require 'open-uri'
require 'ruby-progressbar'

# base_url = "https://api.instagram.com/v1/media/search?client_id=07fd3dd42b9344eda205f298bc3fe4a1&"

base_url = "http://api.instagram.com/oembed?url="

city = "newyork"

### connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(city)
puts "*** mongo connected ***"

cursor = col.find({"twitter.domains"=>/instagr\.am/i})
progressbar = ProgressBar.create(:title => "Write", :starting_at => 0, :total =>cursor.count,:format => '%a/%e %B %c/%C %t',:length => 100)
col.find({"twitter.domains"=>/instagr\.am/i}, :timeout => false) do |cur|
	cur.each do |row|
	inst_url = row["twitter"]["links"].select{|v| v=~ /instagr\.am/i}
	time = row["created_at_dt"]
	id = row["_id"]
	url = "#{base_url}#{inst_url}"
	filename = "./insta_dl/#{city}_#{time.strftime("%s")}_#{id}.jpg"
	if !File.exists? filename
		begin 
				uri = URI.parse(url)
				http= Net::HTTP.new(uri.host,uri.port)
				# http.use_ssl = true
				# http.verify_mode = OpenSSL::SSL::VERIFY_NONE
				r=http.request(Net::HTTP::Get.new(uri.request_uri))
				result = JSON.parse(r.body)
				
				
					open(filename, 'wb') do |file|
	  					file << open(result["url"]).read
					end
				
				# puts "#{id}\t#{time.strftime("%s")}\t#{result["url"]}"
		rescue => err
			puts err
		end
	end
	progressbar.increment
end
cur.close
end
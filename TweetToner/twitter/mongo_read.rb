require 'rubygems'
require 'yajl'
require 'mongo'
require "time"
require 'url_expander'
require 'foursquare2'

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("tweets")
btweets = db.collection("btweets")
puts "*** mongo connected ***"


result = btweets.group(["user"],nil,{'sum'=>0},"function(doc,prev){prev.sum += 1}")

s_result = result.sort_by { |r| r["sum"]}

# result.sort do |a,b|
# 	a["sum"] <=> b["sum"]
# end

sum_all = 0
s_result.each do |r|
	sum_all += r["sum"].to_i
	puts "#{r["sum"]} / #{r["user"]}"
end
sum_d = sum_all/s_result.length
puts "durchschnitt posts: #{sum_d}"

# btweets.find({"url" => /http:\/\/4sq.com\//}).each do |row|
# 	if !row["fsq_userHomeCity"]
# 		row["url"].select{|v| v =~ /http:\/\/4sq.com\//}.each do |url|
# 			ex_url = UrlExpander::Client.expand(url)
# 			 if !ex_url[/.*checkin.*/] then puts row end
				
# 		end
# 	end
# end






# btweets.find("text" => /Kottbusser Tor/).each do |row|
# 		puts row["text"] 
# end


# btweets.find("text" => /I'\S/).each do |row|
# 	if !row["text"][/.*t.co.*/] 
# 		puts row["text"] 
# 	end
# end
# btweets.find("url"=>nil).each do |row|
# 	row.inspect
# end

# puts btweets.find("geo"!="null").to_a
# puts btweets.find({"user" => "mgross9"}).to_a
# btweets.find.each { |row| puts row.to_a[1] }
 # cursor = btweets.find(:user => "ProhorovTweets").to_a
 # cursor.each {|row| puts row.to_a[1]}
# cursor.each { |h| puts h['geo'] }
# things = cursor.map { |h| h['geo'] }

### search nearest locations
# btweets.create_index([["loc", Mongo::GEO2D]])
# btweets.find({"geo" => {"$nearSphere" => [52, 13]}}, {:limit => 50}).each do |p|
#   puts p["text"]
# end

# puts btweets.find({"geo" => nil}).each {|row| puts row.to_a}

# btweets.find({"geo" => nil}).each {|row| puts row.to_a}
# cursor = btweets.find("",:fields =>["geo"])
# cursor.each {|row| puts geo.to_a}


# btweets.find.each do |row| 
# 	# puts row["text"].language.to_s
# 	if row["text"].language.to_s.eql?("russian") 
# 		puts "user: #{row["user"]} text: #{row["text"]}"
# 	end
# end

# btweets.find({"url" => /http:\/\/4sq.com\//}).each do |row|
# 	row["url"].select{|v| v =~ /http:\/\/4sq.com\//}.each do |url|
# 		# puts UrlExpander::Client.expand(url)
# 		ex_url = UrlExpander::Client.expand(url)
# 		if ex_url[/.*checkin.*/]
# 		fsq = ex_url.scan(/checkin\/(.*)\?s=(.*)&/)
# 		puts "#{ex_url} : #{fsq.inspect}"
# 	else 
# 		puts "what's that? #{ex_url}"
# 	end
# 		# puts UrlExpander::Client.expand(url)[/checkin\/(.*)\?s=(.*)&/][$2]
# 		# puts url.class
# 	end
# end


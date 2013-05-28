require 'rubygems'
require 'json'
require 'mongo'
require 'url_expander'
require 'foursquare2'

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("tweets")
btweets = db.collection("btweets")
puts "*** mongo connected ***"

fsq = Foursquare2::Client.new(:client_id => 'A4VXLAPLSKM1ZEBVJPQ0ZDE3G0A4WO3VL24M0F51M2TZCMY3', :client_secret => 'B5XRRXOYN03UKUZNMGBUMAEIQ11KJMQ1SDFV2WM1UJKL4P1Y')

btweets.find({"url" => /http:\/\/4sq.com\//}).each do |row|
	row["url"].select{|v| v =~ /http:\/\/4sq.com\//}.each do |url|
		ex_url = UrlExpander::Client.expand(url)
		if ex_url[/.*checkin.*/]
		check_id = ex_url.scan(/checkin\/(.*)\?s=(.*)&/)
		# puts fsq.user('128318')
		# puts fsq.venue(5104)
		fsq_data = (fsq.checkin(check_id[0][0],{'signature' => check_id[0][1]}))
		# puts "#{fsq_data.venue.categories[0].name} : #{fsq_data.venue.name}"
		fsq_data.venue.categories.each do |cat|
			cat.parents.each do |par|
				puts par
			end
		end
		# puts "#{fsq_data.venue.categories[0].parents[0]}"
		# puts (fsq.checkin(check_id[0][0],{'signature' => check_id[0][1]})).score.scores[0].message

		# puts "#{ex_url} : #{check_id.inspect}"
	else 
		puts "what's that? #{ex_url}"
	end
	end
end


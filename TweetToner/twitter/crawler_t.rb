require 'rubygems'
require 'yajl'
require 'tweetstream'
require 'mongo'
require "time"


# config tweetstream
TweetStream.configure do |config|
  config.consumer_key       = 'SogEzyMHgoBmHKij8EAPbw'
  config.consumer_secret    = 'JW7t1azuiKyEMAXlxpZqowY3NcKXJxhBgYSFVOLU'
  config.oauth_token        = '2173451-SDIukh4PYBewcCew5h6UgbXr9NuBWYTe1OGY7g2AU'
  config.oauth_token_secret = 'O572GUkoELuWBhLxwZWMUXOr4sMHohr9phWdBvvLM'
  config.auth_method        = :oauth
end
client = TweetStream::Client.new()
# Daemon Running
# client = TweetStream::Daemon.new("bTweets")
puts "tweetstream initiated"

# connect mongodb
# connection = Mongo::Connection.new("localhost", 20707)
# db = connection.db("admin")
# auth = db.authenticate("norrix_mongoadmin", "EntIftub")
# db = connection.db("tweets")
# btweets = db.collection("btweets")
# puts "mongo connected"
# doc = {"name" => "MongoDB", "type" => "database", "count" => 1, "info" => {"x" => 203, "y" => '102'}}
# btweets.insert(doc)
# puts btweets.count
# tweets = File.open("tweets.txt", "w")

client.on_error do |message|
  puts message
end
# filter({"locations" => "13.0882097323,52.3418234221,13.7606105539,52.6697240587"}) do |status|
#   # puts "#{status.text} - #{status.created_at}"
#   btweets.insert(status)
# end
  
client.locations(13.0882097323,52.3418234221,13.7606105539,52.6697240587) do |status|
  # puts "#{status.text} - #{status.created_at}"
  puts "geo: #{status.geo.inspect}"
  puts "iso lang: #{status.iso_language_code}"
  puts "place name: #{status.place.name}"
  puts "place full_name: #{status.place.full_name}"
  puts "place country: #{status.place.country}"
  puts "name: #{status.from_user}"
  puts status.place.bounding_box.inspect
  puts "coords: lng: #{status.place.bounding_box.coordinates[0][0][1]}  lot: #{status.place.bounding_box.coordinates[0][0][0]}"
  
  if(status.geo!=nil)
    t_geo = [status.geo.lat, status.geo.lng]
  end
  

if status.urls.count>0
t_urls = []
  status.urls.each { |u| t_urls.push(u.expanded_url)}
  data = {"id" => status.id, "created_at" => status.created_at, "user" => status.from_user, "text" => status.full_text, "geo" => t_geo, "url" => t_urls}
else
  data = {"id" => status.id, "created_at" => status.created_at, "user" => status.from_user, "text" => status.full_text, "geo" => t_geo}
end
  
#   if status.urls==nil
#   data = {"created_at" => status.created_at, "user" => status.from_user, "text" => status.full_text, "geo" => status.geo, "id" => status.id}
# else
#   data = {"created_at" => status.created_at, "user" => status.from_user, "text" => status.full_text, "geo" => status.geo, "id" => status.id, "url" => status.urls[expanded_url]}
# end
  #  "urls" => status.urls
  # status_json = status.inspect.each.map { |tweet| tweet.attrs.to_json } 
   # btweets.insert({"data" => data});
  # tweets.write(status.inspect)
  # tweets.write("\n")
end

# client.track("berlin") do |s|
#   tweets.write(s.inspect)
#   tweets.write("\n")
#   puts s.inspect 
# end
# client.locations(13.0882097323,52.3418234221,13.7606105539,52.6697240587) do |status|
#   puts "#{status.text} - #{status.created_at}"
#   btweets.insert(status)
#   puts btweets.count
#   # tweets.write(status.inspect)
#   # tweets.write("\n")
# end
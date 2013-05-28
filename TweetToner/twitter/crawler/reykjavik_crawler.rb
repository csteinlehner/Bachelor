require 'rubygems'
require 'yajl'
require 'tweetstream'
require 'mongo'
require "time"
require 'url_expander'
require 'foursquare2'
require 'json'
require 'uri'

# config tweetstream
TweetStream.configure do |config|
  config.consumer_key       = 'mE2G6oHlyvZMUrmzXmfyQ'
  config.consumer_secret    = 'MLbShwp16fodWHYhrU5KBXl9cx15KHn70QOAmb82H0'
  config.oauth_token        = '2173451-VlYAPpJXH3ifAp0HlHRNuZKN4k2x5s4nYnvWJItevw'
  config.oauth_token_secret = 'y9ov8iIH1tku13cqsiC6xozvwVB19fRWNjxvsde7Cs'
  config.auth_method        = :oauth
end


## name the city + bounding box
city = "reykjavik"
geo = [-21.962940,64.120270,-21.843590,64.15644]

client = TweetStream::Client.new()
# Daemon Running
# client = TweetStream::Daemon.new(city)
puts "tweetstream initiated"

# foursquare credentials
fsq = Foursquare2::Client.new(:client_id => 'A4VXLAPLSKM1ZEBVJPQ0ZDE3G0A4WO3VL24M0F51M2TZCMY3', :client_secret => 'B5XRRXOYN03UKUZNMGBUMAEIQ11KJMQ1SDFV2WM1UJKL4P1Y')

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("city_tweets")
city_collection = db.collection(city)
puts "mongo connected"
# doc = {"name" => "MongoDB", "type" => "database", "count" => 1, "info" => {"x" => 203, "y" => '102'}}
# btweets.insert(doc)
# puts btweets.count
# tweets = File.open("tweets.txt", "w")


# google translate conf
api_key = "AIzaSyCZqRqqcekBd3OwwZJ8BZV_W3IhXB3bnUA"
base_url = "https://www.googleapis.com/language/translate/v2/detect?key="



client.on_error do |message|
  e_file = File.open("crawler_error.log","a") 
   e_file.puts("#{city}: #{message}")
   e_file.close
end.locations(geo[0],geo[1],geo[2],geo[3]) do |status|
  # puts "#{status.text} - #{status.created_at}"
  if(status.geo!=nil)
    t_geo = [status.geo.lat, status.geo.lng]
    if status.urls.count>0
      t_urls = []
      status.urls.each { |u| t_urls.push(u.expanded_url)}
      data = {"id" => status.id, "created_at" => status.created_at, "user" => status.from_user, "text" => status.full_text, "geo" => t_geo, "url" => t_urls}

      ### foursquare addition
      begin
      t_urls.select{|v| v =~ /4sq.com/}.each do |url2|
          ex_url = UrlExpander::Client.expand(url2)
            if ex_url[/.*checkin.*/]
              check_id = ex_url.scan(/checkin\/(.*)\?s=(.*)&/)
              fsq_data = (fsq.checkin(check_id[0][0],{'signature' => check_id[0][1]}))
              fsq_categories = Array.new
              fsq_categorieParents = Array.new

              fsq_data.venue.categories.each do |cat|
                fsq_categories.push(cat.name)
                  cat.parents.each do |par|
                    fsq_categorieParents.push(par)
                  end
              end
              fsq_hash = {"fsq_userHomeCity"=> fsq_data.user.homeCity, "fsq_venueName"=> fsq_data.venue.name, "fsq_venueID" => fsq_data.venue.id, "fsq_categories" => fsq_categories, "fsq_categorieParents" => fsq_categorieParents}
              data.merge! fsq_hash
            elsif !ex_url[/\/v\//]
              venue_id = ex_url[/\d*$/]
        fsq_data = fsq.venue(venue_id)
        fsq_categories = Array.new
                fsq_categorieParents = Array.new

              fsq_data.categories.each do |cat|
                fsq_categories.push(cat.name)
                  cat.parents.each do |par|
                    fsq_categorieParents.push(par)
                  end
              end
      fsq_hash = {"fsq_venueName"=> fsq_data.name, "fsq_venueID" => fsq_data.id, "fsq_categories" => fsq_categories, "fsq_categorieParents" => fsq_categorieParents}
      data.merge! fsq_hash
            end
      end
      rescue => err
        e_file = File.open("crawler_error.log","a") 
        e_file.puts("foursquare error: #{err.message}")
        e_file.close
      end
    else
      data = {"id" => status.id, "created_at" => status.created_at, "user" => status.from_user, "text" => status.full_text, "geo" => t_geo}
    end
    if t_geo.to_a[0].to_f > geo[1] && t_geo.to_a[0].to_f < geo[3] && t_geo.to_a[1].to_f > geo[0] && t_geo.to_a[1].to_f < geo[2]
      ### google translate get language
      # begin 
      #   url = "#{base_url}#{api_key}&q=#{URI.encode(data["text"])}"
      #   uri = URI.parse(url)
      #   http= Net::HTTP.new(uri.host,uri.port)
      #   http.use_ssl = true
      #   http.verify_mode = OpenSSL::SSL::VERIFY_NONE
      #   r=http.request(Net::HTTP::Get.new(uri.request_uri))
      #   result = JSON.parse(r.body)
      #   lang_hash = {"glan_language"=> result["data"]["detections"][0][0]["language"], "glan_confidence" => result["data"]["detections"][0][0]["confidence"]}
      #   data.merge! lang_hash
      # rescue => err
      #   e_file = File.open("crawler_error.log","a") 
      #   e_file.puts("glang error: #{err.message}")
      #   e_file.close
      # end
      city_collection.insert(data);
    end
  end
end
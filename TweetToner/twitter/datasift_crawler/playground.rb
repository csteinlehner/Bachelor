require 'rubygems'
require 'mongo'
require 'ruby-progressbar'
require 'geokit'
require 'active_support/time'
require 'tzinfo'



require 'twitter'
Twitter.configure do |config|
  config.consumer_key = 'SogEzyMHgoBmHKij8EAPbw'
  config.consumer_secret = 'JW7t1azuiKyEMAXlxpZqowY3NcKXJxhBgYSFVOLU'
  config.oauth_token = 'SDIukh4PYBewcCew5h6UgbXr9NuBWYTe1OGY7g2AU'
  config.oauth_token_secret = 'O572GUkoELuWBhLxwZWMUXOr4sMHohr9phWdBvvLM'
end



city = "potsdam"

class BSON::OrderedHash
  def dig(dotted_path)
    parts = dotted_path.split '.', 2
    match = self[parts[0]]
    if !parts[1] or match.nil?
      return match
    else
      return match.dig(parts[1])
    end
  end
end

# @word_regex = Regexp.new(IO.read("ressource/word_regex.txt"))

### connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(city)
puts "*** mongo connected ***"


# col.find.sort([['_id', -1]]).each do |row|
# 	begin
# 		puts row["twitter"]["id"]
# 	puts "#{Twitter.status(row["twitter"]["id"]).retweet_count} : #{row["interaction"]["content"]} "
# 	rescue => err
# 		puts err
# 	end
# end



### Time Analytics
# tz = TZInfo::Timezone.get('Europe/Berlin')
# start_range = tz.utc_to_local(Time.utc(2012,9,3,0,0,0))
# end_range = tz.utc_to_local(Time.utc(2012,9,9,23,59,59))

# t_start = start_range
# while t_start <= end_range do
# 	t_end = t_start+5.minutes
# 	puts t_start.to_s + " " + col.find({"created_at_dt"=>{:$gte => t_start, :$lte => t_end}}).count.to_s
# 	t_start = t_end
# end

# start_range = start_range.new_offset(Rational(utc_offset,24))

# t_end = (start_range.to_time + 5.minutes).to_datetime
# puts t_end.to_time

 



### statistics
all = col.find("ignore"=>{"$exists"=>false}).count
fsq = col.find({"fsq_venueName"=>{"$exists" => true},"ignore"=>{"$exists"=>false}}).count
insta = col.find({"twitter.domains"=>/instagr\.am/i,"ignore"=>{"$exists"=>false}}).count
dead = col.find({"deleted"=>true}).count
# ratio = fsq.to_f/all*100
puts "\n#### #{city.upcase} ####"
puts "#All: #{all} / #Deleted #{dead} : #Foursquare #{fsq} : #Instagram #{insta}"
#// Prozentanteil: #{ratio}"






### difference
# puts ["ab","cd","cd","dd","dd","ad"].to_set.inspect
# puts (["ab","cd","cd","dd","dd","ad"].to_set - ["dd","cd"].to_set).to_a.inspect

# ### count words for cell
# gridsize = 0.5 #km
# city_locations = {"berlin"        =>      [[52.667511, 13.053550],[52.330269, 13.726160]],
#                   "rosenheim"     =>      [[47.882568, 12.077190],[47.826542, 12.155770]],
#                   "london"        =>      [[51.686031, -0.563000],[51.261318, 0.280360]],
#                   "potsdam"       =>      [[52.514648, 12.887620],[52.342690, 13.172900]],
#                   "munchen"       =>      [[48.229240, 11.377090],[48.041660, 11.749040]],
#                   "newyork"       =>      [[40.917622, -74.255653],[40.495682, -73.689484]],
#                   "sanfrancisco"  =>      [[37.832371, -123.013657],[37.604031, -122.355301]],
#                   "cupertino"     =>      [[37.343269, -122.091164],[37.274429, -121.992699]],
#                   "menlopark"     =>      [[37.553051, -122.229683],[37.413849, -122.077759]]
# }
# def change_latlng(latlng)
#   c_latlng = [latlng[1],latlng[0]]
#   return c_latlng
# end
# ## building index
# puts "*** building geo index ***"
# col.create_index([['loc', Mongo::GEO2D]])

# nw = Geokit::LatLng.new(city_locations[city][0][0], city_locations[city][0][1])
# se = Geokit::LatLng.new(city_locations[city][1][0], city_locations[city][1][1])
# ne = Geokit::LatLng.new(nw.lat,se.lng)
# sw = Geokit::LatLng.new(se.lat,nw.lng)

# dist_lat = nw.distance_to(sw,{:units => :kms, :formular => :sphere})
# dist_lng = nw.distance_to(ne,{:units => :kms, :formular => :sphere})

# steps_lat = (dist_lat/gridsize).ceil
# steps_lng = (dist_lng/gridsize).ceil

# ## count top x of words
# def extract_words(row)
# 	words_in_arr = row["interaction"]["content"].downcase.split(" ")
# 	words_out_arr = Array.new
# 	words_in_arr.each{|w| words_out_arr.push(w.gsub(@word_regex,""))}
# 	words_out_arr.reject!(&:empty?)
# 	return words_out_arr
# end


# def word_count(words,topnum)
#   begin
#     word_count = Hash.new
#     filter_words = %(die der und in zu den das nicht von sie ist des sich mit dem dass er es ein ich auf so eine auch als an nach wie im für du)
#     filter_words.concat( %(the of to and a in for is that on said with be was by as are at from it has an have will or its he not were which this but can more his been would about their also they you me my i'm))
    

#     words.each do |w|
#       if word_count.has_key?(w)
#         word_count[w] += 1
#       else
#         word_count.store(w,1) unless filter_words.include?(w)
#       end
#     end
#     return word_count.sort_by{|k,v| v}.reverse[0...topnum]
#   end
# end

# (1..(steps_lat+1)).each do |i|
#   	t_lat = sw.endpoint(0,gridsize*i, {:units => :kms, :formular => :sphere}).lat
#   	(1..(steps_lng+1)).each do |j|
#     	t_sw = Geokit::LatLng.new(t_lat,sw.endpoint(90,gridsize*j,{:units => :kms, :formular => :sphere}).lng)
#     	t_ne = t_sw.endpoint(45,Math.sqrt(gridsize**2 + gridsize**2),{:units => :kms, :formular => :sphere})
#     	t_midpoint = t_sw.midpoint_to(t_ne,{:units => :kms, :formular => :sphere})
#     	words_all_rows = Array.new
# 	    cell_cursor = col.find("loc" => {"$within" => {"$box" => [change_latlng(t_ne.to_a),change_latlng(t_sw.to_a)]} })
# 	    cell_cursor.each do |row|
# 	    	words_all_rows += extract_words(row)
# 	    end
# 	     puts word_count(words_all_rows,5).inspect
# 	end
# end

### count words
# word_count = Hash.new
# # filter_words = ["die", "der", "und", "in", "zu", "den", "das", "nicht", "von", "sie", "ist", "des", "sich", "mit", "dem", "dass", "er", "es", "ein", "ich", "auf", "so", "eine", "auch", "als", "an", "nach", "wie", "im", "für"]
# filter_words = %(die der und in zu den das nicht von sie ist des sich mit dem dass er es ein ich auf so eine auch als an nach wie im für du)
# filter_words.concat( %(the of to and a in for is that on said with be was by as are at from it has an have will or its he not were which this but can more his been would about their also they you me my i'm))
# col.find("interaction.content" => {"$exists" => true}).each do |row|
# 	words = row["interaction"]["content"].downcase.gsub(/\@\S*|http\S*\:\/\/\S*|\&\S*|[\/#,.@:;!%+*?!()=~|-]/,"").split(" ")		## old regex, look at csv_export_gridcollect.rb
# 	words.each do |w|
# 		if word_count.has_key?(w)
# 			word_count[w] += 1
# 		else
# 			word_count.store(w,1) unless filter_words.include?(w)
# 		end	
# 	end
# end
# word_count_sorted =  word_count.sort_by{|k,v| v}.reverse
# word_string = String.new
# word_count_sorted.each{|k,v| word_string << "#{k}=#{v}_"}
# puts word_string[0...-1]
 
### count user tweets
# user_count = Hash.new
# col.find("interaction.author" => {"$exists" => true}).each do |row|
# 	username = row["interaction"]["author"]["username"]
# 	if user_count.has_key?(username)
# 		user_count[username] += 1
# 	else
# 		user_count.store(username,1)
# 	end
# end
# user_count_sorted = user_count.sort_by {|k,v| v}.reverse[0..9]
# puts user_count_sorted.to_json




### extract languages & count
# language_count = Hash.new

# col.find("language" => {"$exists" => true}).each do |row|
# 	if row["language"]["confidence"] > 60
# 		detect_lang = row["language"]["tag"]
# 		if language_count.has_key?(detect_lang)
# 			language_count[detect_lang] += 1
# 		else
# 			language_count.store(detect_lang,1)
# 		end
# 	end
# end
# lang_string = ""
# language_count.sort_by{|k,v| v}.reverse.each{|k,v| lang_string << "#{k}=#{v}_"}
# puts lang_string


### get a value
# col.find("salience" => {"$exists" => true}).each do |row|
# 	puts row["salience"]["content"]["sentiment"].inspect
# end


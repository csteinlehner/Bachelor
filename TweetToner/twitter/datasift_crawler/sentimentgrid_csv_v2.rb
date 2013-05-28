require 'rubygems'
require "faster_csv"
require 'mongo'
require 'ostruct'
require 'ruby-progressbar'
require 'geokit'

#### configuration
@city = "berlin"
gridsize = 0.5 #km

### static stuff
city_locations = {"berlin"        =>      [[52.667511, 13.053550],[52.330269, 13.726160]],
                  "rosenheim"     =>      [[47.882568, 12.077190],[47.826542, 12.155770]],
                  "london"        =>      [[51.686031, -0.563000],[51.261318, 0.280360]],
                  "potsdam"       =>      [[52.514648, 12.887620],[52.342690, 13.172900]],
                  "munchen"       =>      [[48.229240, 11.377090],[48.041660, 11.749040]],
                  "newyork"       =>      [[40.917622, -74.255653],[40.495682, -73.689484]],
                  "sanfrancisco"  =>      [[37.832371, -123.013657],[37.604031, -122.355301]],
                  "cupertino"     =>      [[37.343269, -122.091164],[37.274429, -121.992699]],
                  "menlopark"     =>      [[37.553051, -122.229683],[37.413849, -122.077759]]
}

@filter_words_de = IO.read("ressource/filter_words_de.txt")
@filter_words_en = IO.read("ressource/filter_words_en.txt")
@filter_words_misc = IO.read("ressource/filter_words_misc.txt")

filter_words = %(#{@filter_words_de})
filter_words.concat( %(#{@filter_words_en}))
filter_words.concat( %(#{@filter_words_misc}))
filter_words.concat(%(#{@city}))
@filter_words = filter_words.split(" ")

regex_string = IO.read("ressource/word_regex.txt")
@word_regex = /#{regex_string}/u


## explores a BSON:OrderedHash with point syntax
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


def extract_names(coll)
	names = Array.new
	coll.each do |entry|
		entry_a = entry.split '.'
		names << entry_a.last
	end
	return names
end


def change_latlng(latlng)
  c_latlng = [latlng[1],latlng[0]]
  return c_latlng
end

def extract_words(row)
  words_in_arr = row["interaction"]["content"].downcase.split(" ")
  words_out_arr = Array.new
  words_in_arr.each{|w| words_out_arr.push(w.gsub(@word_regex,"").gsub(@word_regex,""))}
  words_out_arr.reject!(&:empty?)
  return words_out_arr
end


def word_count(words,topnum)
    word_c = Hash.new
    # words_f = words - @filter_words
    if(words.length>0)
      words.each do |w|
        if word_c.has_key?(w)
          word_c[w] += 1
        else
          word_c.store(w,1) #unless filter_words.include?(w)
        end
      end
  end
  word_c.reject!{|k,v| @filter_words.include?(k)}
  return word_c.sort_by{|k,v| v}.reverse[0...topnum]
end

## calculates median of an array
def calc_median(arr)
  sorted = arr.sort
  len = arr.size
  median = len % 2 == 1 ? sorted[len/2] : (sorted[len/2 - 1] + sorted[len/2]).to_f / 2
  return median
end

## calculates averave of an array
def calc_average(arr)
  return arr.inject(0.0){|sum, el| sum + el}/arr.size
end

## convert hash to csvstring
def array_to_csvstring(ar)
  word_string = String.new
ar.each{|k,v| word_string << "#{k}=#{v} "}
return word_string[0...-1]
end 


### connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(@city)
puts "*** mongo connected ***"

### building index
puts "*** building geo index ***"
col.create_index([['loc', Mongo::GEO2D]])

nw = Geokit::LatLng.new(city_locations[@city][0][0], city_locations[@city][0][1])
se = Geokit::LatLng.new(city_locations[@city][1][0], city_locations[@city][1][1])
ne = Geokit::LatLng.new(nw.lat,se.lng)
sw = Geokit::LatLng.new(se.lat,nw.lng)

dist_lat = nw.distance_to(sw,{:units => :kms, :formular => :sphere})
dist_lng = nw.distance_to(ne,{:units => :kms, :formular => :sphere})

steps_lat = (dist_lat/gridsize).ceil
steps_lng = (dist_lng/gridsize).ceil

## box = sw,ne

CSV_FILE_PATH = File.join(File.dirname(__FILE__), "csv/wordgrid_#{@city}__#{Time.new.strftime("%Y-%m-%d_%H-%M-%S")}_#{gridsize}.csv")

puts "exporting #{@city} to #{CSV_FILE_PATH}"

FasterCSV.open(CSV_FILE_PATH, "w") do |csv|
  progressbar = ProgressBar.create(:title => "Cells", :starting_at => 0, :total => (steps_lat+1)*(steps_lng+1),:format => '%a/%e %B %c/%C %t',:length => 100)
  csv << ["lat","long","size","languages","user_top10","words_top5","sentiment_average","sentiment_median"]
(1..(steps_lat+1)).each do |i|
  t_lat = sw.endpoint(0,gridsize*i, {:units => :kms, :formular => :sphere}).lat
  (1..(steps_lng+1)).each do |j|
    t_sw = Geokit::LatLng.new(t_lat,sw.endpoint(90,gridsize*j,{:units => :kms, :formular => :sphere}).lng)
    t_ne = t_sw.endpoint(45,Math.sqrt(gridsize**2 + gridsize**2),{:units => :kms, :formular => :sphere})
    t_midpoint = t_sw.midpoint_to(t_ne,{:units => :kms, :formular => :sphere})
    
    cell_cursor = col.find("loc" => {"$within" => {"$box" => [change_latlng(t_ne.to_a),change_latlng(t_sw.to_a)]} })
    size = cell_cursor.count
    ### counter vars
    language_count = Hash.new
    user_count = Hash.new
    words_all_rows = Array.new
    sentiment_all_rows = Array.new
    cell_cursor.each do |row|
      ### language detection
      begin
        if row["language"]["confidence"] > 60
          detect_lang = row["language"]["tag"]
          if language_count.has_key?(detect_lang)
            language_count[detect_lang] += 1
          else
            language_count.store(detect_lang,1)
          end
        end
      rescue Exception => e
      end
      begin
        username = row["interaction"]["author"]["username"]
        if user_count.has_key?(username)
          user_count[username] += 1
        else
          user_count.store(username,1)
        end
      rescue Exception => e
      end
    words_all_rows += extract_words(row)
    if row.dig("salience.content.sentiment")!=nil && row.dig("salience.content.sentiment").to_i!=0
      sentiment_all_rows.push(row.dig("salience.content.sentiment").to_i)
    end
    #sentiment_all_rows +=
  end
    if sentiment_all_rows.size < 1
      sentiment_all_rows[0]=0
    end
    csv << t_midpoint.to_a + [size] + [array_to_csvstring(language_count.sort_by{|k,v| v}.reverse)] + [array_to_csvstring(user_count.sort_by {|k,v| v}.reverse[0..9])] + [array_to_csvstring(word_count(words_all_rows,5))] + [calc_average(sentiment_all_rows)] + [calc_median(sentiment_all_rows)]
   progressbar.increment
  end
end
# progressbar.finish
end
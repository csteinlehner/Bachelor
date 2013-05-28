require 'rubygems'
require "faster_csv"
require 'mongo'
require 'ruby-progressbar'
require 'active_support/time'
require 'tzinfo'

city = "munchen"
time_step_minutes = 60;

time_zones = { "berlin"        =>  "Europe/Berlin",    
			   "rosenheim"     =>  "Europe/Berlin",
			   "london"        =>  "Europe/London",
			   "potsdam"       =>  "Europe/Berlin",    
			   "munchen"       =>  "Europe/Berlin",
			   "newyork"       =>  "US/Eastern",
			   "sanfrancisco"  =>  "US/Pacific",
			   "cupertino"     =>  "US/Pacific",
			   "menlopark"     =>  "US/Pacific"
			 }  

@filter_words_de = IO.read("ressource/filter_words_de.txt")
@filter_words_en = IO.read("ressource/filter_words_en.txt")
@filter_words_misc = IO.read("ressource/filter_words_misc.txt")

filter_words = %(#{@filter_words_de})
filter_words.concat( %(#{@filter_words_en}))
filter_words.concat( %(#{@filter_words_misc}))
filter_words.concat(%(#{city}))
@filter_words_regex = /^(?:#{filter_words.gsub!(" ","|")}|\w\w)$/iu

regex_string = IO.read("ressource/word_regex.txt")
@word_regex = /#{regex_string}/ui


## convert hash to csvstring
def array_to_csvstring(ar)
  word_string = String.new
ar.each{|k,v| word_string << "#{k}=#{v}_"}
return word_string[0...-1]
end 

def word_count(words,topnum)
    word_c = Hash.new
    words.reject!{|w| w =~ @filter_words_regex }
    if(words.length>0)
      words.each do |w|
        if word_c.has_key?(w)
          word_c[w] += 1
        else
          word_c.store(w,1) #unless filter_words.include?(w)
        end
      end
  end
  word_c.reject!{|k,v| v<2}
  return word_c.sort_by{|k,v| v}.reverse[0...topnum]
end

def extract_words(row)
  words_in_arr = row["interaction"]["content"].downcase.split(" ")
  words_out_arr = Array.new
  words_in_arr.each{|w| words_out_arr.push(w.gsub(@word_regex,"").gsub(@word_regex,""))}
  words_out_arr.reject!(&:empty?)
  return words_out_arr
end

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(city)
puts "*** mongo connected ***"

# col.find.each do |row|
# 	puts row.inspect
# end

# col.find("fsq_categorieParents"=>{"$exists" => true}).each do |row|
# 	puts "C: "+row["fsq_categories"].inspect
# 	puts "P: "+row["fsq_categorieParents"].inspect
# end

# fsq_categories

CSV_FILE_PATH = File.join(File.dirname(__FILE__), "csv/top_words_#{time_step_minutes}_#{city}__#{Time.new.strftime("%Y-%m-%d_%H-%M-%S")}.csv")

puts "exporting #{city} to #{CSV_FILE_PATH}"

tz = TZInfo::Timezone.get(time_zones[city])
start_range = tz.local_to_utc(Time.utc(2012,9,3,0,0,0))
end_range = tz.local_to_utc(Time.utc(2012,9,9,23,59,59))

t_start = start_range

puts "*** create index"
col.create_index("created_at_dt")
puts "*** start export"
FasterCSV.open(CSV_FILE_PATH, "w") do |csv|
	progressbar = ProgressBar.create(:title => "Write", :starting_at => 0, :total =>((end_range - start_range)/60/time_step_minutes).ceil,:format => '%a/%e %B %c/%C %t',:length => 100)
	csv << ["time"] + ["count"] + ["top_words"]
	while t_start <= end_range do
		progressbar.increment
		t_end = t_start+time_step_minutes.minutes
		
		cursor = col.find({"created_at_dt"=>{:$gte => t_start, :$lte => t_end},"twitter.user.screen_name" => {"$not" => /(RadioTeddyMusic|_BB_RADIO_MUSIC)/}})
		count_msg = cursor.count
		words_all_rows = Array.new
		cursor.each do |row|
			words_all_rows += extract_words(row)
		end

		# csv << [tz.utc_to_local(t_start)] + [count_msg] + [array_to_csvstring(word_count(words_all_rows,5))]
		# cat_parents.select{|k,v| v==cat_parents.values.max}
		t_wc = word_count(words_all_rows,5)
		# puts t_wc
		csv << [tz.utc_to_local(t_start)] + [count_msg] + [array_to_csvstring(t_wc.select{|k,v| v == t_wc[0][1]})]
		t_start = t_end
	end
end
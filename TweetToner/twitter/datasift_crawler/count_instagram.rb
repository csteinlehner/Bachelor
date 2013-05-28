require 'rubygems'
require "faster_csv"
require 'mongo'
require 'ruby-progressbar'
require 'active_support/time'
require 'tzinfo'

city = "london"
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

## convert hash to csvstring
# def array_to_csvstring(ar)
#   word_string = String.new
# ar.each{|k,v| word_string << "#{k}=#{v}_"}
# return word_string[0...-1]
# end 

def array_to_csvstring(ar)
	word_string = String.new
	ar.each{|v| word_string << "#{v}_"}
	return word_string[0...-1]
end

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(city)
puts "*** mongo connected ***"


CSV_FILE_PATH = File.join(File.dirname(__FILE__), "csv/instagram_count_#{time_step_minutes}_#{city}__#{Time.new.strftime("%Y-%m-%d_%H-%M-%S")}.csv")

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
	csv << ["time"] + ["count_instagram"] + ["ids"]
	while t_start <= end_range do
		progressbar.increment
		t_end = t_start+time_step_minutes.minutes
		
		cursor = col.find({"created_at_dt"=>{:$gte => t_start, :$lte => t_end},"twitter.domains"=>/instagr\.am/i})
		count_msg = cursor.count
		ids = []
		cursor.each do |row|
			ids.push(row["_id"])
		end

		csv << [tz.utc_to_local(t_start)] + [count_msg] +[array_to_csvstring(ids)]
		t_start = t_end
	end
end
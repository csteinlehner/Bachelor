require 'rubygems'
require "faster_csv"
require 'mongo'
require 'ruby-progressbar'
require 'active_support/time'
require 'tzinfo'

city = "berlin"
time_step_minutes = 30;

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
def array_to_csvstring(ar)
  word_string = String.new
ar.each{|k,v| word_string << "#{k}=#{v}_"}
return word_string[0...-1]
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

CSV_FILE_PATH = File.join(File.dirname(__FILE__), "csv/fsq_timecount_#{time_step_minutes}_#{city}__#{Time.new.strftime("%Y-%m-%d_%H-%M-%S")}.csv")

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
	csv << ["time"] + ["day"] + ["hour"] + ["minute"] + ["count"] + ["fsq_count"] + ["CategorieParents"] + ["Categories"] + ["Venues"]
	while t_start <= end_range do
		progressbar.increment
		t_end = t_start+time_step_minutes.minutes
		cat = Hash.new
		cat_parents = Hash.new
		venues = Hash.new
		count_msg = col.find({"created_at_dt"=>{:$gte => t_start, :$lte => t_end}}).count
		cursor = col.find({"created_at_dt"=>{:$gte => t_start, :$lte => t_end},"fsq_categorieParents"=>{"$exists"=>true}})
		 fsq_count = cursor.count
			cursor.each do |row|
			row["fsq_categorieParents"].each do |cp|
				 if cat_parents.has_key?(cp)
          			cat_parents[cp] += 1
        		else
          			cat_parents.store(cp,1)
        		end
			end
			row["fsq_categories"].each do |c|
				 if cat.has_key?(c)
          			cat[c] += 1
        		else
          			cat.store(c,1)
        		end
			end
			row["fsq_venueName"].each do |v|
				if venues.has_key?(v)
					venues[v] += 1
				else
					venues.store(v,1)
				end
			end
		end
		day =  tz.utc_to_local(t_start).strftime("%w").to_i-1
		day!=-1 ? day=day : day=6
		minute = tz.utc_to_local(t_start).hour*60+tz.utc_to_local(t_start).min
		hour = tz.utc_to_local(t_start).hour
		# array_to_csvstring(cat_parents.sort_by{|k,v| v}.reverse) ## all sorted
		csv << [tz.utc_to_local(t_start)] + [day] + [hour] + [minute] + [count_msg] + [fsq_count] + (cat_parents.size>0 ? [array_to_csvstring(cat_parents.select{|k,v| v==cat_parents.values.max})] : [""]) + (cat.size>0 ? [array_to_csvstring(cat.select{|k,v| v==cat.values.max})] : [""]) + (venues.size>0 ? [array_to_csvstring(venues.sort_by{|k,v| v}.reverse)] : [""])
		t_start = t_end
	end
end
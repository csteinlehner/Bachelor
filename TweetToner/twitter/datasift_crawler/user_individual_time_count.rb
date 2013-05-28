require 'rubygems'
require "faster_csv"
require 'mongo'
require 'ruby-progressbar'
require 'active_support/time'
require 'tzinfo'
require 'geokit'

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


def calc_median(arr)
  sorted = arr.sort
  len = arr.size
  median = len % 2 == 1 ? sorted[len/2] : (sorted[len/2 - 1] + sorted[len/2]).to_f / 2
  return median
end

## convert hash to csvstring
def hash_to_csvstring(hs)
  word_string = String.new
hs.each{|k,v| word_string << "#{k}=#{v}_"}
return word_string[0...-1]
end 

## get bigger distance
def get_bigger_distance(old_dist,p1,p2)
	p1 = Geokit::LatLng.new(p1[1],p1[0])
	p2 = Geokit::LatLng.new(p2[1],p2[0])
	dist = p1.distance_to(p2, {:units => :kms})
	if(dist>old_dist)
		return dist
	else
		return old_dist
	end
end

def add_distance(old_dist,p1,p2)
	p1 = Geokit::LatLng.new(p1[1],p1[0])
	p2 = Geokit::LatLng.new(p2[1],p2[0])
	dist = p1.distance_to(p2, {:units => :kms})
	return old_dist+dist
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

CSV_FILE_PATH = File.join(File.dirname(__FILE__), "csv/user_invidual_count_#{time_step_minutes}_#{city}__#{Time.new.strftime("%Y-%m-%d_%H-%M-%S")}.csv")

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
	csv << ["time"] + ["count"] + ["user_count"]
	while t_start <= end_range do
		progressbar.increment
		t_end = t_start+time_step_minutes.minutes
		
		cursor = col.find({"created_at_dt"=>{:$gte => t_start, :$lte => t_end}})
		count_msg = cursor.count
		user_count = Hash.new
		user_last_coordinate = Hash.new
		user_distance = Hash.new
		cursor.each do |row|
			begin
		        username = row["interaction"]["author"]["username"]
		        if user_count.has_key?(username)
		          user_count[username] += 1
		          user_distance[username] = add_distance(user_distance[username],row["loc"],user_last_coordinate[username])
		          user_last_coordinate[username] = row["loc"]
		        else
		          user_count.store(username,1)
		          user_last_coordinate.store(username,row["loc"])
		          user_distance.store(username,0)
		        end
		    rescue Exception => e
	    	end
		end
		all_distance = 0
		user_distance.reject!{|k,v|v<0.1}
		if(!user_distance.empty?)
			all_distance_arr = user_distance.values
			puts calc_median(all_distance_arr)
		end
		# user_distance.each{|k,v| all_distance+=v}
		# if(!user_distance.empty?)

		# 	# puts all_distance/user_distance.count
		# end
		# puts user_distance.sort_by{|k,v| v}.reverse[0..5].inspect
		csv << [tz.utc_to_local(t_start)] + [count_msg] + [hash_to_csvstring(user_count.sort_by{|k,v| v}.reverse[0..10])]
		t_start = t_end
	end
end
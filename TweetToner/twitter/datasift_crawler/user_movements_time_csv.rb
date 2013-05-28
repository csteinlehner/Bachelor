require 'rubygems'
require "faster_csv"
require 'mongo'
require 'ruby-progressbar'
require 'active_support/time'
require 'tzinfo'

city = "berlin"
time_step = 60;

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

CSV_FILE_PATH = File.join(File.dirname(__FILE__), "csv/user_movement_#{city}__#{Time.new.strftime("%Y-%m-%d_%H-%M-%S")}.csv")

puts "exporting #{city} to #{CSV_FILE_PATH}"

tz = TZInfo::Timezone.get(time_zones[city])
start_range = tz.local_to_utc(Time.utc(2012,9,3,0,0,0))
end_range = tz.local_to_utc(Time.utc(2012,9,9,23,59,59))

t_start = start_range

puts "*** create index"
col.create_index("created_at_dt")
puts "*** start export"
FasterCSV.open(CSV_FILE_PATH, "w") do |csv|
	progressbar = ProgressBar.create(:title => "Write", :starting_at => 0, :total =>((end_range - start_range)/60/time_step).ceil,:format => '%a/%e %B %c/%C %t',:length => 100)
	csv << ["time"] + ["users"]
	while t_start <= end_range do
		progressbar.increment
		t_end = t_start+time_step.minutes
		users = Hash.new
		col.find({"created_at_dt"=>{:$gte => t_start, :$lte => t_end},"interaction.author.username"=>{"$exists"=>true}}).each do |row|
			user = row.dig("interaction.author.username")
				 if users.has_key?(user)
          			users[user] += 1
        		else
          			users.store(user,1)
        		end
		end
		if users.size>0
			user_string = array_to_csvstring(users.sort_by{|k,v| v}.reverse[1..5])
		else
			user_string = ""
		end
		csv << [tz.utc_to_local(t_start)] + [user_string]
		t_start = t_end
	end
end
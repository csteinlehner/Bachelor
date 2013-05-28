require 'rubygems'
require "faster_csv"
require 'mongo'
require 'ruby-progressbar'
require 'active_support/time'
require 'tzinfo'

city = "newyork"
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

cities = 	 ["berlin",    
			   "rosenheim",
			   "london",
			   "potsdam",    
			   "munchen",
			   "newyork",
			   "sanfrancisco",
			   "cupertino",
			   "menlopark"
			 ]

## convert hash to csvstring
def array_to_csvstring(ar)
  word_string = String.new
ar.each{|k,v| word_string << "#{k}=#{v}_"}
return word_string[0...-1]
end 


# connect mongodb
connection = Mongo::Connection.new("localhost")
# db = connection.db("admin")
# auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
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

# puts "exporting #{city} to #{CSV_FILE_PATH}"

uniq_parents = Hash.new
uniq_cats = Hash.new

cities.each do |c|
col = db.collection(c)
puts c
tz = TZInfo::Timezone.get(time_zones[city])
start_range = tz.local_to_utc(Time.utc(2012,9,3,0,0,0))
end_range = tz.local_to_utc(Time.utc(2012,9,9,23,59,59))

t_start = start_range

puts "*** create index"
col.create_index("created_at_dt")
puts "*** start export"
# FasterCSV.open(CSV_FILE_PATH, "w") do |csv|
	# progressbar = ProgressBar.create(:title => "Write", :starting_at => 0, :total =>((end_range - start_range)/60/time_step_minutes).ceil,:format => '%a/%e %B %c/%C %t',:length => 100)
	# csv << ["time"] + ["count"] + ["fsq_count"] + ["CategorieParents"] + ["Categories"]
	while t_start <= end_range do
		# progressbar.increment
		t_end = t_start+time_step_minutes.minutes
		cat = Hash.new
		cat_parents = Hash.new
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
		end
		cat_parents_sorted = cat_parents.sort_by{|k,v| v}.reverse
		cat_sorted = cat.sort_by{|k,v| v}.reverse
		# csv << [tz.utc_to_local(t_start)] + [count_msg] + [fsq_count] + (cat_parents.size>0 ? [array_to_csvstring(cat_parents_sorted.select{|k,v| v == cat_parents_sorted[0][1]})] : [""]) + (cat.size>0 ? [array_to_csvstring(cat_sorted.select{|k, v| v == cat_sorted[0][1]})] : [""])
		cat_parents_sorted.each do |cp|
			if uniq_parents.has_key?(cp[0])
				uniq_parents[cp[0]] += 1
			else
				uniq_parents.store(cp[0],1)
			end
		end

		cat_sorted.each do |c|
			if uniq_cats.has_key?(c[0])
				uniq_cats[c[0]] += 1
			else
				uniq_cats.store(c[0],1)
			end
		end

		puts "#{cat_parents_sorted.count} : #{cat_parents_sorted.sort_by{|k,v| v}.reverse.inspect}"

		t_start = t_end
	end
end
puts "#{uniq_parents.count} : #{uniq_parents.sort_by{|k,v| v}.reverse.inspect}"
puts "\n \n"
puts "#{uniq_cats.count} : #{uniq_cats.sort_by{|k,v| v}.reverse.inspect}"
	# puts uniq_parents.count
	# puts uniq_parents.sort_by{|k,v| v}.reverse.inspect

	# puts uniq_cats.count
	# puts uniq_cats.sort_by{|k,v| v}.reverse.inspect
# end
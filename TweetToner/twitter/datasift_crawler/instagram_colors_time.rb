require 'rubygems'
require 'ruby-progressbar'
require "faster_csv"
require 'RMagick'
include Magick
require 'date'
require 'tzinfo'
require 'mongo'
require 'active_support/time'

def map_range(a, b, s)
  af, al, bf, bl = a.first, a.last, b.first, b.last
  return bf + (s - af).*(bl - bf).quo(al - af)
end

city = "sanfrancisco"
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

CSV_FILE_PATH = File.join(File.dirname(__FILE__), "csv/instagram_top_color_time_#{city}__#{Time.new.strftime("%Y-%m-%d_%H-%M-%S")}.csv")

tz = TZInfo::Timezone.get(time_zones[city])

# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(city)
puts "*** mongo connected ***"


path = "./insta_dl/"
# dir = Dir["#{path}#{city}_*"]
# dir.each do |f|
# 	puts tz.utc_to_local(Time.at(f.split("_")[2].to_i))
# end


start_range = tz.local_to_utc(Time.utc(2012,9,3,0,0,0))
end_range = tz.local_to_utc(Time.utc(2012,9,9,23,59,59))

t_start = start_range

puts "*** create index"
col.create_index("created_at_dt")
puts "*** start export to #{CSV_FILE_PATH}"
FasterCSV.open(CSV_FILE_PATH, "w") do |csv|
	progressbar = ProgressBar.create(:title => "Write", :starting_at => 0, :total =>((end_range - start_range)/60/time_step_minutes).ceil,:format => '%a/%e %B %c/%C %t',:length => 100)
	csv << ["time"] + ["count"] + ["red"] + ["green"] + ["blue"]
while t_start <= end_range do
	progressbar.increment
	t_end = t_start+time_step_minutes.minutes
	img_list = ImageList.new()
	cursor = col.find({"created_at_dt"=>{:$gte => t_start, :$lte => t_end},"twitter.domains"=>/instagr\.am/i}).limit(5)
	# puts cursor.count
	cursor.each do |row|
		time = row["created_at_dt"]
		id = row["_id"]
		begin
			if img_list.length>0
				img_list.append("#{path}#{city}_#{time.strftime("%s")}_#{id}.jpg")
			else
				img_list = ImageList.new("#{path}#{city}_#{time.strftime("%s")}_#{id}.jpg")
			end
		rescue => err

		end
	end
	if(img_list.length>0)
		colors = img_list.quantize(8)
		hist = colors[0].color_histogram.sort_by{|k,v| v}.reverse[0]
		px = hist.first
		red = map_range([0,65535],[0,255],px.red.to_f).to_i
		green = map_range([0,65535],[0,255],px.green.to_f).to_i
		blue = map_range([0,65535],[0,255],px.blue.to_f).to_i
		csv << [tz.utc_to_local(t_start)] + [img_list.length] + [red] + [green] + [blue]
	else
		csv << [tz.utc_to_local(t_start)] + [0] + ["-"] + ["-"] + ["-"]
	end
	t_start = t_end
end
end


# FasterCSV.open(CSV_FILE_PATH, "w") do |csv|

# 	path = "./insta_dl/"
# 	dir = Dir.new(path)
# 	progressbar = ProgressBar.create(:title => "Write", :starting_at => 0, :total =>Dir[File.join(path, '**', '*')].count { |file| File.file?(file) },:format => '%a/%e %B %c/%C %t',:length => 100)
# 	csv << ["filename"] + ["red"] + ["green"] + ["blue"]
# 	dir.each do |f|
# 		if(f!="." && f!="..")
# 			# puts "#{path}#{f}"
# 			img = ImageList.new("#{path}#{f}")
# 			colors = img.quantize(8)
# 			# puts QuantumRange
# 			hist = colors[0].color_histogram.sort_by{|k,v| v}.reverse[0]
# 			px = hist.first
# 			red = map_range([0,65535],[0,255],px.red.to_f).to_i
# 			green = map_range([0,65535],[0,255],px.green.to_f).to_i
# 			blue = map_range([0,65535],[0,255],px.blue.to_f).to_i
# 			csv << [f] + [red] + [green] + [blue]
# 			progressbar.increment
# 		end
# 	end
# end
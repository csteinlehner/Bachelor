require 'rubygems'
require 'ruby-progressbar'
require 'faster_csv'
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

city = "berlin"
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

CSV_FILE_PATH = File.join(File.dirname(__FILE__), "instagram_top_color_time_#{city}__#{Time.new.strftime("%Y-%m-%d_%H-%M-%S")}.csv")

tz = TZInfo::Timezone.get(time_zones[city])

# connect mongodb
connection = Mongo::Connection.new("localhost")
# db = connection.db("admin")
# auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(city)
puts "*** mongo connected ***"


path = "/Users/norrix/fhp/Bachelor/data/insta_dl/"
# dir = Dir["#{path}#{city}_*"]
# dir.each do |f|
# 	puts tz.utc_to_local(Time.at(f.split("_")[2].to_i))
# end


start_range = tz.local_to_utc(Time.utc(2012,9,3,0,0,0))
# start_range = tz.local_to_utc(Time.utc(2012,9,7,15,0,0))
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
		cursor = col.find({"created_at_dt"=>{:$gte => t_start, :$lte => t_end},"twitter.domains"=>/instagr\.am/i})
		cursor.each do |row|
			time = row["created_at_dt"]
			id = row["_id"]
			begin
				b_img = Magick::Image::read("#{path}#{city}_#{time.strftime("%s")}_#{id}.jpg").first
				# b_img = b_img.blur_image(10,20)
				if img_list.length==0
					img_list << b_img
				else
					 img_list.push(b_img)
				end
			rescue => err
			end
		end
		if(img_list.length>0)
			colors = img_list.quantize(128,RGBColorspace,NoDitherMethod)
			a_colors = colors.append(true)
			# a_colors = a_colors.blur_image(20,20)
			 a_colors.write("/Users/norrix/fhp/Bachelor/data/tmp/#{tz.utc_to_local(t_start)}.jpg")
			

			## highest
			hist = a_colors.color_histogram.sort_by{|k,v| v}.reverse
			px = hist[0].first
			#px2 = hist[1].first
			i = 0
			while(px.red < 30 && px.green < 30 && px.blue < 30)
				if(i<hist.length-1)
					i+=1
					px = hist[i].first
				else
					break
				end
			end
			# if px1.red < 30 && px1.green < 30 && px1.blue < 30
			# 	px = px1
			# else
			# 	px = px2
			# end



			# hist = a_colors.color_histogram
			# hist_hsla = a_colors.color_histogram.collect{|h,n| h.to_hsla}
			# max = [0,0,0,0]
			# px_max = hist_hsla.each do |p|
			# 	if p[1]>max[1]
			# 		max = p
			# 	end
			# end
			# px = Pixel.from_hsla(max[0],max[1],max[2],max[3])


			# puts hist_hsla.inspect
			# puts "#{n} / #{h.first.to_hsla}"
			# puts hist_hsla
			# px = 
			# px2 = 
			# px_bright = [hist[0].first,hist[1].first].max
			# red = map_range([0,65535],[0,255],px.red.to_f).to_i
			# green = map_range([0,65535],[0,255],px.green.to_f).to_i
			# blue = map_range([0,65535],[0,255],px.blue.to_f).to_i

			# csv << [tz.utc_to_local(t_start)] + [img_list.length] + [px.red] + [px.green] + [px.blue]
			csv << [tz.utc_to_local(t_start)] + [img_list.length] + [px.red] + [px.green] + [px.blue]
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

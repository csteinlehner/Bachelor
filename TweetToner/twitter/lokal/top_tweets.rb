require 'rubygems'
require 'mongo'
require 'tzinfo'
require 'ruby-progressbar'
require 'faster_csv'

city = "newyork"

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

### connect mongodb
connection = Mongo::Connection.new("localhost")
# db = connection.db("admin")
# auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(city)
puts "*** mongo connected ***"

user_klout = Hash.new
puts city

#col.create_index("klout")
cursor = col.find("klout"=>{"$exists" => true},"ignore"=>{"$exists"=>false})
progressbar = ProgressBar.create(:title => "Analyzing", :starting_at => 0, :total =>cursor.count,:format => '%a/%e %B %c/%C %t',:length => 100)
cursor.each do |row|
	progressbar.increment
	 username = row["interaction"]["author"]["username"]
	 klout = row["klout"]["score"]
	  if !user_klout.has_key?(username)
	  	user_klout[username] = klout
	  end
end

sorted_klout = user_klout.sort_by{|k,v| v}
# average = sorted_klout.inject(0){|sum,x| sum+x[1]}/user_klout.count
top_klout_user = sorted_klout.reverse[0...100]

srand(23)

tz = TZInfo::Timezone.get(time_zones[city])
CSV_FILE_PATH = File.join(File.dirname(__FILE__), "top_tweets/top_tweets_#{city}__#{Time.new.strftime("%Y-%m-%d_%H-%M-%S")}.csv")
FasterCSV.open(CSV_FILE_PATH, "w") do |csv|
	csv << ["time"] + ["day"] + ["hour"] + ["minute"] + ["user"] + ["text"]
	progressbar = ProgressBar.create(:title => "Write", :starting_at => 0, :total =>top_klout_user.count,:format => '%a/%e %B %c/%C %t',:length => 100)
	top_klout_user.each do |user|
		progressbar.increment
		tweets = Array.new
		col.find("interaction.author.username"=>user[0]).each do |tweet|
			text = tweet["interaction"]["content"]
			if(!(text =~ /^@\w/) && !(text =~ /http/) && !(text =~ /^RT/) && !(text =~ /“@|.@/))
				tweets << tweet
			end
		end
		if(!tweets.empty?)
			tweet = tweets[rand(tweets.length)]
			text = tweet["interaction"]["content"]
			time = tweet["created_at_dt"]
			day =  tz.utc_to_local(time).strftime("%w").to_i-1
			day!=-1 ? day=day : day=6
			minute = tz.utc_to_local(time).hour*60+tz.utc_to_local(time).min
			hour = tz.utc_to_local(time).hour
			#puts "#{day}/#{hour}/#{minute} #{user[0]} #{text.gsub("\n","")}"
			csv << [tz.utc_to_local(time)] + [day] + [hour] + [minute] + [user[0]] + [text.gsub("\n","")]
		end
	end
end

# puts "#Klout:"
# puts "min: #{sorted_klout[0][1]} / max:#{sorted_klout[-1][1]} / ø #{average}"
# puts "#Top Klout User"
# puts sorted_klout.reverse[0...30]


# puts user_count.count
# puts user_count.sort_by{|k,v| v}.reverse[0..10]
#user_count = col.group(["twitter.user.screen_name"],nil,{:count => 0}, "function(x,y) {y.count++}").to_a
#puts user_count.inspect

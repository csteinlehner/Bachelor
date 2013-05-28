require 'rubygems'
require 'mongo'
require 'language_list'
require "faster_csv"
require 'ruby-progressbar'

city = "sanfrancisco"

### connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(city)
puts "*** mongo connected ***"

# col.find("language"=>{"$exists" => true},"ignore"=>{"$exists"=>false}).each do |row|
# 	puts "#{row["language"]["tag"]} : #{row["language"]["confidence"]} : #{row["interaction"]["content"]}"
# end

lang_count = Hash.new

CSV_FILE_PATH = File.join(File.dirname(__FILE__), "csv/lang_count_#{city}__#{Time.new.strftime("%Y-%m-%d_%H-%M-%S")}.csv")
puts "exporting #{city} to #{CSV_FILE_PATH}"
cursor = col.find("language"=>{"$exists" => true},"ignore"=>{"$exists"=>false})
progressbar = ProgressBar.create(:title => "Analyzing", :starting_at => 0, :total =>cursor.count,:format => '%a/%e %B %c/%C %t',:length => 100)
cursor.each do |row|
	begin
		 if row["language"]["confidence"]>65
		 	lang = LanguageList::LanguageInfo.find(row["language"]["tag"]).name
	  		if lang_count.has_key?(lang)
	  			lang_count[lang] += 1
	  		else
	  			lang_count.store(lang,1)
	  			# puts "#{row["language"]["tag"]} : #{row["language"]["confidence"]} : #{row["interaction"]["content"]}"
	  		end
		end
	rescue => e
	end
	progressbar.increment
end


lang_sorted = lang_count.sort_by{|k,v| v}.reverse
# puts cursor.count
# lang_sorted.each do |k,v| 
# 	puts "#{v} : #{v.to_f/cursor.count}"
# end
others = lang_sorted.select{|k,v| v.to_f/cursor.count <0.005}
lang_final = lang_sorted - others
others_sum = others.inject(0){|sum,x| sum+x[1]}
lang_final.push ["Others",others_sum]
# puts lang_final.inspect
puts "#Sprachen insgesamt: #{lang_sorted.count}"
# puts "#Andere Sprachen: #{others.collect{|k,v| k}}.inspect"

# lang_sorted.collect{|k,v| v}
FasterCSV.open(CSV_FILE_PATH, "w") do |csv|
	
	csv << lang_final.collect{|k,v| k}
	csv << lang_final.collect{|k,v| v}
end

# puts lang_count.sort_by{|k,v| v}.inspect
# puts user_count.sort_by{|k,v| v}.reverse[0..10]
#user_count = col.group(["twitter.user.screen_name"],nil,{:count => 0}, "function(x,y) {y.count++}").to_a
#puts user_count.inspect

require 'rubygems'
require "faster_csv"
require 'mongo'
require 'ostruct'
require 'ruby-progressbar'


city = "berlin"

colls = ["_id","interaction.geo.longitude","interaction.geo.latitude","interaction.created_at","interaction.content","interaction.author.username","language.tag","language.confidence","salience.content.sentiment"]


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

def extract_names(coll)
	names = Array.new
	coll.each do |entry|
		entry_a = entry.split '.'
		names << entry_a.last
	end
	return names
end


# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(city)
puts "*** mongo connected ***"

CSV_FILE_PATH = File.join(File.dirname(__FILE__), "csv/#{city}__#{Time.new.strftime("%Y-%m-%d_%H-%M-%S")}.csv")

puts "exporting #{city} to #{CSV_FILE_PATH}"


FasterCSV.open(CSV_FILE_PATH, "w") do |csv|
	progressbar = ProgressBar.create(:title => "Rows", :starting_at => 0, :total => col.find({"fsq_venueName"=>{"$exists" => true}}).count,:format => '%a/%e %B %c/%C %t',:length => 100)
	csv << extract_names(colls)
	col.find({"fsq_venueName"=>{"$exists" => true}}).each do |row|
		progressbar.increment
		if !row["deleted"]
			t_row = Array.new
			colls.each do |path|
				t_row << row.dig(path)
			end
			csv << t_row
		end
	end
	progressbar.finish
end

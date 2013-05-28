require 'rubygems'
require "faster_csv"
require 'mongo'
require 'ostruct'
require 'ruby-progressbar'


city = "rosenheim"

colls = ["interaction.geo.longitude","interaction.geo.latitude"]


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

## shorten the Path for BSON dig Paths
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

CSV_FILE_PATH = File.join(File.dirname(__FILE__), "csv/fsq_#{city}__#{Time.new.strftime("%Y-%m-%d_%H-%M-%S")}.csv")

puts "exporting #{city} to #{CSV_FILE_PATH}"


fsq_venue_count = Hash.new
fsq_coords = Hash.new

progressbar = ProgressBar.create(:title => "Collect", :starting_at => 0, :total => col.find({"fsq_venueName"=>{"$exists" => true}}).count,:format => '%a/%e %B %c/%C %t',:length => 100)
col.find({"fsq_venueName"=>{"$exists" => true}}).each do |row|
	progressbar.increment
	 venue_name = row["fsq_venueName"]
	 if fsq_venue_count.has_key?(venue_name)
	 	fsq_venue_count[venue_name] += 1
	 else
	 	fsq_venue_count.store(venue_name,1)
	 	coords = Array.new
	 	colls.each do |path|
	 		coords << row.dig(path)
	 	end
	 	fsq_coords.store(venue_name,coords)
	 end
end



FasterCSV.open(CSV_FILE_PATH, "w") do |csv|
	progressbar = ProgressBar.create(:title => "Write", :starting_at => 0, :total =>fsq_venue_count.length,:format => '%a/%e %B %c/%C %t',:length => 100)
	csv << extract_names(colls) + ["fsq_venueName"] + ["fsq_venueCount"]
	fsq_venue_count.each do |e|
		progressbar.increment
		coords = fsq_coords[e[0]]
		csv <<  [coords[0]] + [coords[1]] + [e[0]] + [e[1]]
	end
end

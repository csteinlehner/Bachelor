require 'rubygems'
require 'mongo'
require 'ruby-progressbar'

city = "potsdam"


# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(city)
puts "*** mongo connected ***"

ignoreList = ["RadioTeddyMusic","_BB_RADIO_MUSIC"]

progressbar = ProgressBar.create(:title => "Rows", :starting_at => 0, :total => col.find.count,:format => '%a/%e %B %c/%C %t',:length => 100)

col.find.each do |row|
	if !row["deleted"]
		if ignoreList.include?(row["interaction"]["author"]["username"])
			ignore = {'ignore' => true}
			row.merge! ignore
			col.save(row)
		end
	end
	progressbar.increment
end
progressbar.finish
exit
require 'rubygems'
require 'mongo'
require 'ruby-progressbar'

city = "rosenheim"


# connect mongodb
connection = Mongo::Connection.new("localhost", 20707)
db = connection.db("admin")
auth = db.authenticate("norrix_mongoadmin", "EntIftub")
db = connection.db("datasift")
col = db.collection(city)
puts "*** mongo connected ***"

progressbar = ProgressBar.create(:title => "Rows", :starting_at => 0, :total => col.find.count,:format => '%a/%e %B %c/%C %t',:length => 100)

col.find.each do |row|
	if !row["deleted"]
		lonlat = {'loc' => [row["interaction"]["geo"]["longitude"],row["interaction"]["geo"]["latitude"]]}
		row.merge! lonlat
		col.save(row)
		# puts row
	end
	progressbar.increment
end
progressbar.finish
exit
require 'rubygems'
require 'ruby-progressbar'
require "faster_csv"
require 'RMagick'
include Magick

def map_range(a, b, s)
  af, al, bf, bl = a.first, a.last, b.first, b.last
  return bf + (s - af).*(bl - bf).quo(al - af)
end


CSV_FILE_PATH = File.join(File.dirname(__FILE__), "csv/instagram_top_color.csv")
FasterCSV.open(CSV_FILE_PATH, "w") do |csv|

	path = "./insta_dl/"
	dir = Dir.new(path)
	progressbar = ProgressBar.create(:title => "Write", :starting_at => 0, :total =>Dir[File.join(path, '**', '*')].count { |file| File.file?(file) },:format => '%a/%e %B %c/%C %t',:length => 100)
	csv << ["filename"] + ["red"] + ["green"] + ["blue"]
	dir.each do |f|
		if(f!="." && f!="..")
			# puts "#{path}#{f}"
			img = ImageList.new("#{path}#{f}")
			colors = img.quantize(8)
			# puts QuantumRange
			hist = colors[0].color_histogram.sort_by{|k,v| v}.reverse[0]
			px = hist.first
			red = map_range([0,65535],[0,255],px.red.to_f).to_i
			green = map_range([0,65535],[0,255],px.green.to_f).to_i
			blue = map_range([0,65535],[0,255],px.blue.to_f).to_i
			csv << [f] + [red] + [green] + [blue]
			progressbar.increment
		end
	end
end
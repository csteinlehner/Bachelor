require 'rubygems'
require 'RMagick'
include Magick

Dir.foreach(".") do |f|
	if(f.end_with?(".png"))
		GC.start
		org = ImageList.new(f)
		ne = org.resize(200,200)
		ne.write(f.gsub("\.png","_s\.png"))
		puts "#{f} written"
	end
end
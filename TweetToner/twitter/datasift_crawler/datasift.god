God.watch do |w|
	w.name = "multi_datasift"
	w.group = "datasift_crawler"
	w.start = "ruby /home/norrix/cgi-bin/twitter/datasift_crawler/multi_datasift.rb"
	w.keepalive
end
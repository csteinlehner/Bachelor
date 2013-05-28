God.watch do |w|
	w.name = "berlin_c"
	w.group = "t_crawler"
	w.start = "ruby /home/norrix/cgi-bin/twitter/crawler/berlin_crawler.rb"
	w.keepalive
end

God.watch do |w|
	w.name = "reykjavik_c"
	w.group = "t_crawler"
	w.start = "ruby /home/norrix/cgi-bin/twitter/crawler/reykjavik_crawler.rb"
	w.keepalive
end

God.watch do |w|
	w.name = "rosenheim_c"
	w.group = "t_crawler"
	w.start = "ruby /home/norrix/cgi-bin/twitter/crawler/rosenheim_crawler.rb"
	w.keepalive
end
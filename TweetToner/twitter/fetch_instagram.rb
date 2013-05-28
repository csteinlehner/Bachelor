require 'rubygems'
require 'net/http'
require 'net/https'
require 'json'
require 'uri'
require 'RMagick'
require 'miro'

# api_key = "AIzaSyCZqRqqcekBd3OwwZJ8BZV_W3IhXB3bnUA"
base_url = "https://api.instagram.com/v1/media/search?client_id=07fd3dd42b9344eda205f298bc3fe4a1&"
def minify_image(file, name)
   img = Magick::Image::read(file).first
   img.resize!(5,5)
   img = img.blur_image(3, 10.0)
   img.write("instapics/#{name}-micro.jpg")
end


# Miro.options[:color_count] = 4
# (13.0882097323,52.3418234221,13.7606105539,52.6697240587)
WIDTH = 10
HEIGHT = 10
com_list = Magick::ImageList.new
for lon in 1308..1376
		lonf = lon.to_f/100
		lat_list = Magick::ImageList.new
	5267.downto(5234) do |lat|
		latf = lat.to_f/100
		url = "#{base_url}lat=#{latf}&lng=#{lonf}&distance=500"
		begin 
			uri = URI.parse(url)
			http= Net::HTTP.new(uri.host,uri.port)
			http.use_ssl = true
			http.verify_mode = OpenSSL::SSL::VERIFY_NONE
			r=http.request(Net::HTTP::Get.new(uri.request_uri))
			result = JSON.parse(r.body)
			begin
				url_string = result["data"][0]["images"]["thumbnail"]["url"]

				Net::HTTP.start( url_string[/\S+.com/].sub!("http://","") ) do |http|   
					resp = http.get( "/#{url_string[/([^\/]+$)/]}" )
					file_name = url_string[/([^\/]+$)/].scan(/(.+)(.jpg)/)
					open(url_string, 'rb') do |f|
						img = Magick::Image::from_blob(f.read).first
						img.resize!(WIDTH,HEIGHT)
						img = img.blur_image(2, WIDTH.to_f)
						img.write("instapics/#{lat}_#{lon}-micro.jpg")
						puts "#{latf}/#{lonf} written"
						lat_list << img
					end
				end
				# puts file_name.inspect
				# open( "instapics/#{file_name[0][0]}-1#{file_name[0][1]}", 'wb' ) do |file|
			# 		file.write(resp.body)
			# 		minify_image("instapics/#{file_name[0][0]}-1#{file_name[0][1]}","#{file_name[0][0]}-1")
				# end
			rescue => err
				img = Magick::Image.new(WIDTH, HEIGHT)
				img.transparent_color = 'white'
				img.transparent('white').write("instapics/#{lat}_#{lon}-micro.jpg")
				lat_list << img.transparent('white')
				puts "fetching error: #{err.message}"
			end
		rescue => err
			img = Magick::Image.new(WIDTH, HEIGHT)
				img.transparent_color = 'white'
				img.transparent('white').write("instapics/#{lat}_#{lon}-micro.jpg")
				lat_list << img.transparent('white')
				puts "http/parser error: #{err.message}"
		end

		# lat_list.write("instapics/test.png")
	end
	com_list << lat_list.append(true)
end
com_list.append(false).write("instapics/test.png")


# https://api.instagram.com/v1/media/search?lat=52.34&lng=13.08&client_id=07fd3dd42b9344eda205f298bc3fe4a1
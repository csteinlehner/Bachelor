require 'rubygems'
require 'json'

 json = JSON.parse(IO.read("categories.json"))


# hashes are nested, containing arrays of hashes of category information
def print_hash(hash, topname)
  # We've found a category
  if hash['id'] and hash['name']
    puts "categoryParents.put(\"#{hash['name']}\", \"#{topname}\");"
  end
  # We've found an array of hashes of categories (that might contain more arrays!)
  if hash["categories"]
    hash["categories"].each do |h|
      print_hash(h, topname)
    end
  end
end

json_response = JSON.parse(IO.read("categories.json"))

json_response["response"]["categories"].each do |c|
  print_hash(c, c['name'])
end
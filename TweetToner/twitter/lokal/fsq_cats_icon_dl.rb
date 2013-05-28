require 'rubygems'
require 'json'

 json = JSON.parse(IO.read("categories.json"))


# hashes are nested, containing arrays of hashes of category information
def print_hash(hash)
  # We've found a category
  if hash['id'] and hash['name']
    puts "#{hash['icon']['prefix']}88#{hash['icon']['suffix']},#{hash['name'].gsub(/[\s+|\/|&|(|)]/, "")}"
    `curl -o "./fsq_icons2/#{hash['name'].gsub(/[\s+|\/|&|(|)]/, "")}#{hash['icon']['suffix']}" "#{hash['icon']['prefix'].gsub(/_v2/,"")}256#{hash['icon']['suffix']}"`
  end
  # We've found an array of hashes of categories (that might contain more arrays!)
  if hash["categories"]
    hash["categories"].each do |h|
      print_hash(h)
    end
  end
end

json_response = JSON.parse(IO.read("categories.json"))

json_response["response"]["categories"].each do |c|
  print_hash(c)
end
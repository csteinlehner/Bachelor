package citygrid;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import toxi.color.TColor;

public class PatternDrawManager implements DrawManager{
	Pattern pattern = Pattern.compile("\\s+|\\/|&|\\(|\\)|'");
	
	HashMap<String, TColor> iconColors = new HashMap<String, TColor>();
	HashMap<String, String> categoryParents = new HashMap<String, String>();
	
	File file = new File("data/fsq_icons");
	File[] files = file.listFiles();
	HashMap<String, String> iconPaths = new HashMap<String, String>();
	public PatternDrawManager(){
		for (int i = 0; i < files.length; i++) {
			String[] splitted = files[i].getPath().split("/"); 
			String filename = splitted[splitted.length-1].split("\\.")[0];
			iconPaths.put(filename, files[i].getPath());
	    }
		categoryParents = FsqNameHelper.CATEGORY_PARENTS;
		createColors();
	}
	
	public PImage createPattern(String catName, int sizeX, int sizeY,  PVector[] maskShape, int tileSize){
		String name = convertCatNameToFileName(catName);
		String path = iconPaths.get(name);
		PImage tile = CityGrid.p5.loadImage(path);
		tile.resize(tileSize, tileSize);
		int tilesX = (int)Math.ceil(sizeX/(float)tile.width);
		int tilesY = (int)Math.ceil(sizeY/(float)tile.height);
		PGraphics mask = CityGrid.p5.createGraphics(tilesX*tile.width, tilesY*tile.height, PApplet.JAVA2D);
		mask.beginDraw();
		mask.background(0);
		mask.fill(255);
		mask.beginShape();
		for (int i = 0; i < maskShape.length; i++) {
			mask.vertex(maskShape[i].x,maskShape[i].y);
		}
		mask.endShape();
		mask.endDraw();
		PImage piMask = mask.get(0, 0, mask.width, mask.height);
		
		PGraphics pattern = CityGrid.p5.createGraphics(tilesX*tile.width, tilesY*tile.height, PApplet.JAVA2D);
		pattern.beginDraw();
		pattern.background(255,255);
		TColor c = new TColor(iconColors.get(categoryParents.get(catName)));
		pattern.background(c.toARGB());
		pattern.smooth();
		pattern.tint(c.lighten(0.3f).toARGB());
		for (int i = 0; i < tilesX; i++) {
			for (int j = 0; j < tilesY; j++) {
				pattern.image(tile,tile.width*i,tile.height*j);	
			}	
		}
//		pattern.stroke(0,0,255);
//		pattern.strokeWeight(2);
//		pattern.noFill();
//		pattern.beginShape();
//		for (int i = 0; i < maskShape.length; i++) {
//			pattern.vertex(maskShape[i].x,maskShape[i].y);
//		}
//		pattern.endShape();
		pattern.endDraw();
		PImage piPattern = pattern.get();
		piPattern.mask(piMask);
		return piPattern;
	}
	
	private String convertCatNameToFileName(String catName){
		Matcher matcher = pattern.matcher(catName);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
		     matcher.appendReplacement(sb, "");
		    // s now contains "BAR"
		}
		matcher.appendTail(sb);
		System.out.println(this.getClass().getName()+": "+sb.toString());
		return sb.toString();
	}
	
	private void createColors(){
		TColor c_default = TColor.newRGBA(1,0,0,1);
		
		iconColors.put("Arts & Entertainment", TColor.newHex("854237"));
		iconColors.put("College & University", TColor.newHex("7F4156"));
		iconColors.put("Food", TColor.newHex("614D6D"));
		iconColors.put("Nightlife Spot", TColor.newHex("315870"));
		iconColors.put("Outdoors & Recreation", TColor.newHex("335772"));
		iconColors.put("Professional & Other Places", TColor.newHex("235D3D"));
		iconColors.put("Residence", TColor.newHex("485821"));
		iconColors.put("Shop & Service", TColor.newHex("684E1A"));
		iconColors.put("Travel & Transport", TColor.newHex("7C412C"));
		
		
		
		
		
		
//		iconColors.put("Plaza",c_default);
//		iconColors.put("Bus Station",c_default);
//		iconColors.put("Hotel",c_default);
//		iconColors.put("Bed & Breakfast",c_default);
//		iconColors.put("Home (private)",c_default);
//		iconColors.put("Playground",c_default);
//		iconColors.put("Hockey Arena",c_default);
//		iconColors.put("Gay Bar",c_default);
//		iconColors.put("Bar",c_default);
//		iconColors.put("Tech Startup",c_default);
//		iconColors.put("Historic Site",c_default);
//		iconColors.put("History Museum",c_default);
//		iconColors.put("Field",c_default);
//		iconColors.put("Park",c_default);
//		iconColors.put("Beach",c_default);
//		iconColors.put("Flea Market",c_default);
//		iconColors.put("Karaoke Bar",c_default);
//		iconColors.put("Mall",c_default);
//		iconColors.put("Train Station",c_default);
//		iconColors.put("Concert Hall",c_default);
//		iconColors.put("Airport",c_default);
//		iconColors.put("Hospital",c_default);
//		iconColors.put("Platform",c_default);
//		iconColors.put("Airport Gate",c_default);
//		iconColors.put("Subway",c_default);
//		iconColors.put("University",c_default);
//		iconColors.put("Café",c_default);
//		iconColors.put("Office",c_default);
//		iconColors.put("Event Space",c_default);
//		iconColors.put("Bank",c_default);
//		iconColors.put("Dentist's Office",c_default);
//		iconColors.put("French Restaurant",c_default);
//		iconColors.put("BBQ Joint",c_default);
//		iconColors.put("Convention Center",c_default);
//		iconColors.put("Deli / Bodega",c_default);
//		iconColors.put("Monument / Landmark",c_default);
//		iconColors.put("Clothing Store",c_default);
//		iconColors.put("Bus Line",c_default);
//		iconColors.put("Golf Course",c_default);
//		iconColors.put("Grocery Store",c_default);
//		iconColors.put("Light Rail",c_default);
//		iconColors.put("General Entertainment",c_default);
//		iconColors.put("Fast Food Restaurant",c_default);
//		iconColors.put("Scenic Lookout",c_default);
//		iconColors.put("Burger Joint",c_default);
//		iconColors.put("Italian Restaurant",c_default);
//		iconColors.put("Mexican Restaurant",c_default);
//		iconColors.put("Residential Building (Apartment / Condo)",c_default);
//		iconColors.put("Bridge",c_default);
//		iconColors.put("Neighborhood",c_default);
//		iconColors.put("Movie Theater",c_default);
//		iconColors.put("Asian Restaurant",c_default);
//		iconColors.put("Newsstand",c_default);
//		iconColors.put("Church",c_default);
//		iconColors.put("River",c_default);
//		iconColors.put("General Travel",c_default);
//		iconColors.put("Nightclub",c_default);
//		iconColors.put("Farmers Market",c_default);
//		iconColors.put("Vietnamese Restaurant",c_default);
//		iconColors.put("German Restaurant",c_default);
//		iconColors.put("Electronics Store",c_default);
//		iconColors.put("Other Great Outdoors",c_default);
//		iconColors.put("Boutique",c_default);
//		iconColors.put("Restaurant",c_default);
//		iconColors.put("Rock Club",c_default);
//		iconColors.put("Government Building",c_default);
//		iconColors.put("Moving Target",c_default);
//		iconColors.put("Japanese Restaurant",c_default);
//		iconColors.put("Art Museum",c_default);
//		iconColors.put("Diner",c_default);
//		iconColors.put("Tanning Salon",c_default);
//		iconColors.put("Turkish Restaurant",c_default);
//		iconColors.put("Building",c_default);
//		iconColors.put("Gas Station / Garage",c_default);
//		iconColors.put("College Academic Building",c_default);
//		iconColors.put("Miscellaneous Shop",c_default);
//		iconColors.put("Doctor's Office",c_default);
//		iconColors.put("Airport Terminal",c_default);
//		iconColors.put("Coworking Space",c_default);
//		iconColors.put("College Lab",c_default);
//		iconColors.put("Sushi Restaurant",c_default);
//		iconColors.put("Stable",c_default);
//		iconColors.put("Music Venue",c_default);
//		iconColors.put("College & University",c_default);
//		iconColors.put("School",c_default);
//		iconColors.put("Dive Bar",c_default);
//		iconColors.put("Hotel Bar",c_default);
//		iconColors.put("Coffee Shop",c_default);
//		iconColors.put("Mediterranean Restaurant",c_default);
//		iconColors.put("Courthouse",c_default);
//		iconColors.put("Farm",c_default);
//		iconColors.put("Strip Club",c_default);
//		iconColors.put("Furniture / Home Store",c_default);
//		iconColors.put("Gym",c_default);
//		iconColors.put("Pub",c_default);
//		iconColors.put("Lounge",c_default);
//		iconColors.put("Performing Arts Venue",c_default);
//		iconColors.put("Hostel",c_default);
//		iconColors.put("Steakhouse",c_default);
//		iconColors.put("General College & University",c_default);
//		iconColors.put("Pizza Place",c_default);
//		iconColors.put("Snack Place",c_default);
//		iconColors.put("City Hall",c_default);
//		iconColors.put("Military Base",c_default);
//		iconColors.put("Breakfast Spot",c_default);
//		iconColors.put("Motel",c_default);
//		iconColors.put("Cafeteria",c_default);
//		iconColors.put("Museum",c_default);
//		iconColors.put("College Library",c_default);
//		iconColors.put("Arcade",c_default);
//		iconColors.put("Hardware Store",c_default);
//		iconColors.put("Bakery",c_default);
//		iconColors.put("Stadium",c_default);
//		iconColors.put("Department Store",c_default);
//		iconColors.put("Housing Development",c_default);
//		iconColors.put("Capitol Building",c_default);
//		iconColors.put("Indie Movie Theater",c_default);
//		iconColors.put("Cocktail Bar",c_default);
//		iconColors.put("Garden",c_default);
//		iconColors.put("Travel Lounge",c_default);
//		iconColors.put("Butcher",c_default);
//		iconColors.put("Fire Station",c_default);
//		iconColors.put("Train",c_default);
//		iconColors.put("Food & Drink Shop",c_default);
//		iconColors.put("Drugstore / Pharmacy",c_default);
//		iconColors.put("Automotive Shop",c_default);
//		iconColors.put("Resort",c_default);
//		iconColors.put("American Restaurant",c_default);
//		iconColors.put("Sandwich Place",c_default);
//		iconColors.put("Brewery",c_default);
//		iconColors.put("Theater",c_default);
//		iconColors.put("Middle Eastern Restaurant",c_default);
//		iconColors.put("Art Gallery",c_default);
//		iconColors.put("Beer Garden",c_default);
//		iconColors.put("College Classroom",c_default);
//		iconColors.put("Zoo",c_default);
//		iconColors.put("Car Wash",c_default);
//		iconColors.put("Boat or Ferry",c_default);
//		iconColors.put("Arepa Restaurant",c_default);
//		iconColors.put("Indian Restaurant",c_default);
//		iconColors.put("Spa / Massage",c_default);
//		iconColors.put("Post Office",c_default);
//		iconColors.put("Chinese Restaurant", c_default);
//		iconColors.put("New American Restaurant",c_default);
//		iconColors.put("Jazz Club", c_default);
//		iconColors.put("Bowling Alley", c_default);
//		iconColors.put("Food Truck", c_default);
//		iconColors.put("Hot Dog Joint", c_default);
//		iconColors.put("Road", c_default);
//		iconColors.put("Tennis", c_default);
//		iconColors.put("Women's Store", c_default);
//		iconColors.put("Greek Restaurant", c_default);
//		iconColors.put("Thai Restaurant", c_default);
//		iconColors.put("Caribbean Restaurant", c_default);
//		iconColors.put("Latin American Restaurant", c_default);
//		iconColors.put("Surf Spot", c_default);
//		iconColors.put("Sports Bar", c_default);
//		iconColors.put("Hookah Bar", c_default);
//		iconColors.put("Track", c_default);
//		iconColors.put("Racetrack", c_default);
//		iconColors.put("Professional & Other Places", c_default);
//		iconColors.put("Pool", c_default);
//		iconColors.put("Dog Run", c_default);
//		iconColors.put("Harbor / Marina", c_default);
//		iconColors.put("Donut Shop", c_default);
//		iconColors.put("Airport Tram", c_default);
//		iconColors.put("College Residence Hall", c_default);
//		iconColors.put("Gym / Fitness Center", c_default);
//		iconColors.put("Salon / Barbershop", c_default);
//		iconColors.put("Medical Center", c_default);
//		iconColors.put("Multiplex", c_default);
//		iconColors.put("Bagel Shop", c_default);
//		iconColors.put("Ice Cream Shop", c_default);
//		iconColors.put("Dessert Shop", c_default);
//		iconColors.put("Car Dealership", c_default);
//		iconColors.put("Arts & Entertainment", c_default);
//		iconColors.put("Toy / Game Store", c_default);
//		iconColors.put("Football Stadium", c_default);
//		iconColors.put("Mobile Phone Shop", c_default);
//		iconColors.put("Baseball Field", c_default);
//		iconColors.put("Athletic & Sport", c_default);
//		iconColors.put("Shoe Store", c_default);
//		iconColors.put("Southern / Soul Food Restaurant", c_default);
//		iconColors.put("Brazilian Restaurant", c_default);
//		iconColors.put("Korean Restaurant", c_default);
//		iconColors.put("Peruvian Restaurant", c_default);
//		iconColors.put("Falafel Restaurant", c_default);
//		iconColors.put("Cuban Restaurant", c_default);
//		iconColors.put("Fried Chicken Joint", c_default);
//		iconColors.put("Taco Place", c_default);
//		iconColors.put("Ramen /  Noodle House", c_default);
//		iconColors.put("Speakeasy", c_default);
//		iconColors.put("Outdoors & Recreation", c_default);
//		iconColors.put("Laundry Service", c_default);
//		iconColors.put("Gastropub", c_default);
//		iconColors.put("Theme Park", c_default);
//		iconColors.put("Record Shop", c_default);
//		iconColors.put("Campground", c_default);
//		iconColors.put("Other Nightlife", c_default);
//		iconColors.put("Video Store", c_default);
//		iconColors.put("Rental Car Location", c_default);
//		iconColors.put("Hiking Trail", c_default);
//		iconColors.put("Basketball Stadium", c_default);
//		iconColors.put("Ski Chalet", c_default);
//		iconColors.put("Ski Area", c_default);
//		iconColors.put("Liquor Store", c_default);
//		iconColors.put("Baseball Stadium", c_default);
//		iconColors.put("Wine Bar", c_default);
//		iconColors.put("Voting Booth", c_default);
//		iconColors.put("Fraternity House", c_default);
//		iconColors.put("Sculpture Garden", c_default);
//		iconColors.put("Pier", c_default);
//		iconColors.put("College Technology Building", c_default);
//		iconColors.put("Design Studio", c_default);
//		iconColors.put("Emergency Room", c_default);
//		iconColors.put("Martial Arts Dojo", c_default);
//		iconColors.put("Jewelry Store", c_default);
//		iconColors.put("Men's Store", c_default);
//		iconColors.put("Tennis Court", c_default);
//		iconColors.put("Vegetarian / Vegan Restaurant", c_default);
//		iconColors.put("Moroccan Restaurant", c_default);
//		iconColors.put("Eastern European Restaurant", c_default);
//		iconColors.put("Argentinian Restaurant", c_default);
//		iconColors.put("Tapas Restaurant", c_default);
//		iconColors.put("Whisky Bar", c_default);
//		iconColors.put("Airport Lounge", c_default);
//		iconColors.put("Police Station", c_default);
//		iconColors.put("Candy Store", c_default);
//		iconColors.put("Shop & Service", c_default);
//		iconColors.put("Parking", c_default);
//		iconColors.put("Gym Pool", c_default);
//		iconColors.put("Soccer Stadium", c_default);
//		iconColors.put("Science Museum", c_default);
//		iconColors.put("Sporting Goods Shop", c_default);
//		iconColors.put("Cemetery", c_default);
//		iconColors.put("Dance Studio", c_default);
//		iconColors.put("Cupcake Shop", c_default);
//		iconColors.put("Cosmetics Shop", c_default);
//		iconColors.put("Casino", c_default);
//		iconColors.put("Lake", c_default);
//		iconColors.put("Nursery School", c_default);
//		iconColors.put("College Basketball Court", c_default);
//		iconColors.put("Tea Room", c_default);
//		iconColors.put("Temple", c_default);
//		iconColors.put("Internet Cafe", c_default);
//		iconColors.put("Soccer Field", c_default);
//		iconColors.put("Skate Park", c_default);
//		iconColors.put("Meeting Room", c_default);
//		iconColors.put("Residence", c_default);
//		iconColors.put("Camera Store", c_default);
//		iconColors.put("High School", c_default);
//		iconColors.put("Nail Salon", c_default);
//		iconColors.put("Accessories Store", c_default);
//		iconColors.put("Tattoo Parlor", c_default);
//		iconColors.put("Bookstore", c_default);
//		iconColors.put("Yoga Studio", c_default);
//		iconColors.put("Opera House", c_default);
//		iconColors.put("Conference Room", c_default);
//		iconColors.put("Library", c_default);
//		iconColors.put("Student Center", c_default);
//		iconColors.put("Gourmet Shop", c_default);
//		iconColors.put("Comedy Club", c_default);
//		iconColors.put("Flower Shop", c_default);
//		iconColors.put("College Theater", c_default);
//		iconColors.put("Convenience Store", c_default);
//		iconColors.put("Mountain", c_default);
//		iconColors.put("Plane", c_default);
//		iconColors.put("Trade School", c_default);
//		iconColors.put("College Science Building", c_default);
//		iconColors.put("Laboratory", c_default);
//		iconColors.put("Track Stadium", c_default);
//		iconColors.put("Embassy / Consulate", c_default);
//		iconColors.put("Antique Shop", c_default);
//		iconColors.put("Community College", c_default);
//		iconColors.put("College Administrative Building", c_default);
//		iconColors.put("Bike Shop", c_default);
//		iconColors.put("Non-Profit", c_default);
//		iconColors.put("Taxi", c_default);
//		iconColors.put("Rest Area", c_default);
//		iconColors.put("Shrine", c_default);
//		iconColors.put("Tourist Information Center", c_default);
//		iconColors.put("Video Game Store", c_default);
//		iconColors.put("Paper / Office Supplies Store", c_default);
//		iconColors.put("Winery", c_default);
//		iconColors.put("Lingerie Store", c_default);
//		iconColors.put("College Track", c_default);
//		iconColors.put("Pet Store", c_default);
//		iconColors.put("Sorority House", c_default);
//		iconColors.put("College Engineering Building", c_default);
//		iconColors.put("College Quad", c_default);
//		iconColors.put("College Bookstore", c_default);
//		iconColors.put("College Football Field", c_default);
//		iconColors.put("Gift Shop", c_default);
//		iconColors.put("College Rec Center", c_default);
//		iconColors.put("College Soccer Field", c_default);
//		iconColors.put("Skating Rink", c_default);
//		iconColors.put("Spiritual Center", c_default);
//		iconColors.put("Wings Joint", c_default);
//		iconColors.put("Cajun / Creole Restaurant", c_default);
//		iconColors.put("Filipino Restaurant", c_default);
//		iconColors.put("Dim Sum Restaurant", c_default);
//		iconColors.put("Burrito Place", c_default);
//		iconColors.put("Salad Place", c_default);
//		iconColors.put("Food", c_default);
//		iconColors.put("Food Court", c_default);
//		iconColors.put("Seafood Restaurant", c_default);
//		iconColors.put("Juice Bar", c_default);
	}
	

}

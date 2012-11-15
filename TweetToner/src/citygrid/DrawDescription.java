package citygrid;

import java.util.HashMap;

public class DrawDescription {
	DrawingType background, overlay;
	
	HashMap<DrawingType, String> catNameEnums = new HashMap<DrawingType, String>();
	
	public DrawDescription(DrawingType background, DrawingType overlay){
		this.background = background;
		this.overlay = overlay;
		
//		catNameEnums.put(DrawingType.FOOD,"Food");
//		catNameEnums.put(DrawingType.FOOD, "Spanish Restaurants");
//		catNameEnums.put(DrawingType.FOOD, "Food & Drink Shops");
//		catNameEnums.put(DrawingType.OTHER, "Travel & Transport");
//		catNameEnums.put(DrawingType.OTHER, "Professional & Other Places");
//		catNameEnums.put(DrawingType.OTHER, "Offices");
//		catNameEnums.put(DrawingType.OTHER, "Shops & Services");
//		catNameEnums.put(DrawingType.OTHER, "Outdoors & Recreation");
//		catNameEnums.put(DrawingType.OTHER, "Arts & Entertainment");
//		catNameEnums.put(DrawingType.OTHER, "Nightlife Spots");
//		catNameEnums.put(DrawingType.OTHER, "Residences");
//		catNameEnums.put(DrawingType.OTHER, "Airports");
//		catNameEnums.put(DrawingType.OTHER, "Colleges & Universities");
//		catNameEnums.put(DrawingType.OTHER, "Gyms or Fitness Centers");
//		catNameEnums.put(DrawingType.OTHER, "Stadiums");
//		catNameEnums.put(DrawingType.OTHER, "Clothing Stores");
//		catNameEnums.put(DrawingType.OTHER, "Government Buildings");
//		catNameEnums.put(DrawingType.OTHER, "Museums");
//		catNameEnums.put(DrawingType.OTHER, "Medical Centers");
//		catNameEnums.put(DrawingType.OTHER, "Bus Stations");
//		catNameEnums.put(DrawingType.OTHER, "Performing Arts Venues");
//		catNameEnums.put(DrawingType.OTHER, "Movie Theaters");
//		catNameEnums.put(DrawingType.OTHER, "Spiritual Centers");
//		catNameEnums.put(DrawingType.OTHER, "Train Stations");
//		catNameEnums.put(DrawingType.OTHER, "Ferries");
//		catNameEnums.put(DrawingType.OTHER, "Hotels");
//		catNameEnums.put(DrawingType.OTHER, "Music Venues");
//		catNameEnums.put("Athletics & Sports",DrawingType.OTHER);
//		catNameEnums.put("College Academic Buildings",DrawingType.OTHER);
//		catNameEnums.put("Schools",DrawingType.OTHER);
//		catNameEnums.put("College Stadiums",DrawingType.OTHER);
//		catNameEnums.put("Beaches",DrawingType.OTHER);
//		catNameEnums.put("Convention Centers",DrawingType.OTHER);
//		catNameEnums.put("Ski Areas",DrawingType.OTHER);
	}
}

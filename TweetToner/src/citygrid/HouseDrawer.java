package citygrid;

import java.util.HashMap;

import processing.core.PGraphics;
import processing.core.PVector;
import toxi.color.TColor;

public class HouseDrawer {
	HashMap<String, TColor> houseColors = new HashMap<String, TColor>();
	
	public HouseDrawer(){
		houseColors.put("Food",TColor.newRandom());
		houseColors.put("Spanish Restaurants",TColor.newRandom());
		houseColors.put("Food & Drink Shops",TColor.newRandom());
		houseColors.put("Travel & Transport",TColor.newRandom());
		houseColors.put("Professional & Other Places",TColor.newRandom());
		houseColors.put("Offices",TColor.newRandom());
		houseColors.put("Shops & Services",TColor.newRandom());
		houseColors.put("Outdoors & Recreation",TColor.newRGB(0.486f, 0.8f, 0.3215f));
		houseColors.put("Arts & Entertainment",TColor.newRandom());
		houseColors.put("Nightlife Spots",TColor.newRandom());
		houseColors.put("Residences",TColor.newRandom());
		houseColors.put("Airports",TColor.newRandom());
		houseColors.put("Colleges & Universities",TColor.newRandom());
		houseColors.put("Gyms or Fitness Centers",TColor.newRandom());
		houseColors.put("Stadiums",TColor.newRandom());
		houseColors.put("Clothing Stores",TColor.newRandom());
		houseColors.put("Government Buildings",TColor.newRandom());
		houseColors.put("Museums",TColor.newRandom());
		houseColors.put("Medical Centers",TColor.newRandom());
		houseColors.put("Bus Stations",TColor.newRandom());
		houseColors.put("Performing Arts Venues",TColor.newRandom());
		houseColors.put("Movie Theaters",TColor.newRandom());
		houseColors.put("Spiritual Centers",TColor.newRandom());
		houseColors.put("Train Stations",TColor.newRandom());
		houseColors.put("Ferries",TColor.newRandom());
		houseColors.put("Hotels",TColor.newRandom());
		houseColors.put("Music Venues",TColor.newRandom());
		houseColors.put("Athletics & Sports",TColor.newRandom());
		houseColors.put("College Academic Buildings",TColor.newRandom());
		houseColors.put("Schools",TColor.newRandom());
		houseColors.put("College Stadiums",TColor.newRandom());
		houseColors.put("Beaches",TColor.newRandom());
		houseColors.put("Convention Centers",TColor.newRandom());
		houseColors.put("Ski Areas",TColor.newRandom());
	}
	public void drawHouse(PVector bl, PVector br, PVector tr, PVector tl, String catName){
		PGraphics citymap = CityGrid.p5.citymap;
		try {
			citymap.fill(houseColors.get(catName).toARGB());
			
		} catch (Exception e) {
			// TODO: handle exception
			citymap.noFill();
			System.out.println(catName);
		}
		citymap.stroke(255);
		citymap.beginShape();
		citymap.vertex(bl.x, bl.y);
		citymap.vertex(br.x, br.y);
		citymap.vertex(tr.x, tr.y);
		citymap.vertex(tl.x, tl.y);
		citymap.endShape();
	}
}

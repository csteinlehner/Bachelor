package citygrid;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import toxi.color.TColor;

public class HouseDrawer {
	HashMap<String, TColor> houseColors = new HashMap<String, TColor>();
	HashMap<String, String> houseFunctions = new HashMap<String, String>();
	
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
		
		houseFunctions.put("Food","food");
		houseFunctions.put("Spanish Restaurants","food");
		houseFunctions.put("Food & Drink Shops","food");
		houseFunctions.put("Travel & Transport","other");
		houseFunctions.put("Professional & Other Places","other");
		houseFunctions.put("Offices","other");
		houseFunctions.put("Shops & Services","other");
		houseFunctions.put("Outdoors & Recreation","other");
		houseFunctions.put("Arts & Entertainment","other");
		houseFunctions.put("Nightlife Spots","other");
		houseFunctions.put("Residences","other");
		houseFunctions.put("Airports","other");
		houseFunctions.put("Colleges & Universities","other");
		houseFunctions.put("Gyms or Fitness Centers","other");
		houseFunctions.put("Stadiums","other");
		houseFunctions.put("Clothing Stores","other");
		houseFunctions.put("Government Buildings","other");
		houseFunctions.put("Museums","other");
		houseFunctions.put("Medical Centers","other");
		houseFunctions.put("Bus Stations","other");
		houseFunctions.put("Performing Arts Venues","other");
		houseFunctions.put("Movie Theaters","other");
		houseFunctions.put("Spiritual Centers","other");
		houseFunctions.put("Train Stations","other");
		houseFunctions.put("Ferries","other");
		houseFunctions.put("Hotels","other");
		houseFunctions.put("Music Venues","other");
		houseFunctions.put("Athletics & Sports","other");
		houseFunctions.put("College Academic Buildings","other");
		houseFunctions.put("Schools","other");
		houseFunctions.put("College Stadiums","other");
		houseFunctions.put("Beaches","other");
		houseFunctions.put("Convention Centers","other");
		houseFunctions.put("Ski Areas","other");
		
	}
	public void drawHouse(PVector bl, PVector br, PVector tr, PVector tl, String catName){
		
		try {
			java.lang.reflect.Method method = HouseDrawer.class.getMethod(houseFunctions.get(catName),PVector.class, PVector.class, PVector.class, PVector.class, String.class); 
			method.invoke(this, bl, br, tr, tl, catName);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}
	
	
	// Food Category
	public void food(PVector bl, PVector br, PVector tr, PVector tl, String catName){
		PGraphics citymap = CityGrid.p5.citymap;
		PVector mid = PVector.sub(br, tl);
		mid.div(2);
		mid.add(tl);
		
		citymap.stroke(255);

		citymap.fill(255);
		citymap.beginShape();
		citymap.vertex(bl.x, bl.y);
		citymap.vertex(br.x, br.y);
		citymap.vertex(tr.x, tr.y);
		citymap.vertex(tl.x, tl.y);
		citymap.endShape();
		citymap.fill(0);
		citymap.ellipseMode(PApplet.CENTER);
		citymap.ellipse(mid.x,mid.y,10,10);
	}
	
	// alle anderen
	public void other(PVector bl, PVector br, PVector tr, PVector tl, String catName){
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

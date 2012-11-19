package citygrid;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.sun.org.apache.bcel.internal.generic.NEW;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PGraphics2D;
import processing.core.PShape;
import processing.core.PVector;
import toxi.color.TColor;

public class HouseDrawer {
	HashMap<String, TColor> houseColors = new HashMap<String, TColor>();
	HashMap<String, DrawDescription> houseFunctions = new HashMap<String, DrawDescription>();
	SymbolManager symbolManager;
	
	public HouseDrawer(){
		symbolManager = new SymbolManager();
		
//		houseColors.put("Food",TColor.newRandom());
////		houseColors.put("Spanish Restaurants",TColor.newRandom());
////		houseColors.put("Food & Drink Shops",TColor.newRandom());
//		houseColors.put("Travel & Transport",TColor.newRandom());
//		houseColors.put("Professional & Other Places",TColor.newRandom());
//		houseColors.put("Offices",TColor.newRandom());
//		houseColors.put("Shops & Services",TColor.newRandom());
//		houseColors.put("Outdoors & Recreation",TColor.newRGB(0.486f, 0.8f, 0.3215f));
//		houseColors.put("Arts & Entertainment",TColor.newRandom());
//		houseColors.put("Nightlife Spots",TColor.newRandom());
//		houseColors.put("Residences",TColor.newRandom());
//		houseColors.put("Airports",TColor.newRandom());
//		houseColors.put("Colleges & Universities",TColor.newRandom());
//		houseColors.put("Gyms or Fitness Centers",TColor.newRandom());
//		houseColors.put("Stadiums",TColor.newRandom());
//		houseColors.put("Clothing Stores",TColor.newRandom());
//		houseColors.put("Government Buildings",TColor.newRandom());
//		houseColors.put("Museums",TColor.newRandom());
//		houseColors.put("Medical Centers",TColor.newRandom());
//		houseColors.put("Bus Stations",TColor.newRandom());
//		houseColors.put("Performing Arts Venues",TColor.newRandom());
//		houseColors.put("Movie Theaters",TColor.newRandom());
//		houseColors.put("Spiritual Centers",TColor.newRandom());
//		houseColors.put("Train Stations",TColor.newRandom());
//		houseColors.put("Ferries",TColor.newRandom());
//		houseColors.put("Hotels",TColor.newRandom());
//		houseColors.put("Music Venues",TColor.newRandom());
//		houseColors.put("Athletics & Sports",TColor.newRandom());
//		houseColors.put("College Academic Buildings",TColor.newRandom());
//		houseColors.put("Schools",TColor.newRandom());
//		houseColors.put("College Stadiums",TColor.newRandom());
//		houseColors.put("Beaches",TColor.newRandom());
//		houseColors.put("Convention Centers",TColor.newRandom());
//		houseColors.put("Ski Areas",TColor.newRandom());
		
		houseFunctions.put("Plaza",new DrawDescription(DrawingType.PLAZA, DrawingType.NONE));
		houseFunctions.put("Bus Station",new DrawDescription(DrawingType.NEUTRAL, DrawingType.BUS));
		houseFunctions.put("Hotel",new DrawDescription(DrawingType.HOTEL, DrawingType.HOTEL));
		houseFunctions.put("Home (private)",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Playground",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Hockey Arena",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Gay Bar",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Bar",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Tech Startup",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Historic Site",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("History Museum",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Field",new DrawDescription(DrawingType.PARK, DrawingType.NONE));
		houseFunctions.put("Park",new DrawDescription(DrawingType.PARK, DrawingType.NONE));
		houseFunctions.put("Beach",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Flea Market",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Karaoke Bar",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Mall",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Train Station",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Concert Hall",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Airport",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Hospital",new DrawDescription(DrawingType.WHITE, DrawingType.HOSPITAL));
		houseFunctions.put("Platform",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Airport Gate",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Subway",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("University",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Café",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Office",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Event Space",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Bank",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Dentist's Office",new DrawDescription(DrawingType.WHITE, DrawingType.HOSPITAL));
		houseFunctions.put("French Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("BBQ Joint",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Convention Center",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Deli / Bodega",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Monument / Landmark",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Clothing Store",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Bus Line",new DrawDescription(DrawingType.NEUTRAL, DrawingType.BUS));
		houseFunctions.put("Golf Course",new DrawDescription(DrawingType.PARK, DrawingType.NONE));
		houseFunctions.put("Grocery Store",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Light Rail",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("General Entertainment",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Fast Food Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Scenic Lookout",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Burger Joint",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Italian Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Mexican Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Residential Building (Apartment / Condo)",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Bridge",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Neighborhood",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Movie Theater",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Asian Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Newsstand",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Church",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("River",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("General Travel",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Nightclub",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Farmers Market",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Vietnamese Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("German Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Electronics Store",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Other Great Outdoors",new DrawDescription(DrawingType.PARK, DrawingType.NONE));
		houseFunctions.put("Boutique",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Rock Club",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Government Building",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Moving Target",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Japanese Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Art Museum",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Diner",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Tanning Salon",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Turkish Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Building",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Gas Station / Garage",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("College Academic Building",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Miscellaneous Shop",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Doctor's Office",new DrawDescription(DrawingType.WHITE, DrawingType.HOSPITAL));
		houseFunctions.put("Airport Terminal",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Coworking Space",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("College Lab",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Sushi Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Stable",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Music Venue",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("College & University",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("School",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Dive Bar",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Hotel Bar",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Coffee Shop",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Mediterranean Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Courthouse",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Farm",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Strip Club",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Furniture / Home Store",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Gym",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Pub",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Lounge",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Performing Arts Venue",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Hostel",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Steakhouse",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("General College & University",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Pizza Place",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Snack Place",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("City Hall",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Military Base",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Breakfast Spot",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Motel",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Cafeteria",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Museum",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("College Library",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Arcade",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Hardware Store",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Bakery",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Stadium",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Department Store",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Housing Development",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Capitol Building",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Indie Movie Theater",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Cocktail Bar",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Garden",new DrawDescription(DrawingType.PARK, DrawingType.NONE));
		houseFunctions.put("Travel Lounge",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Butcher",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Fire Station",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Train",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Food & Drink Shop",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Drugstore / Pharmacy",new DrawDescription(DrawingType.WHITE, DrawingType.HOSPITAL));
		houseFunctions.put("Automotive Shop",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Resort",new DrawDescription(DrawingType.PARK, DrawingType.NONE));
		houseFunctions.put("American Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Sandwich Place",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Brewery",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Theater",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Middle Eastern Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Art Gallery",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Beer Garden",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("College Classroom",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Zoo",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Car Wash",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Boat or Ferry",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Arepa Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Indian Restaurant",new DrawDescription(DrawingType.NEUTRAL, DrawingType.FOOD));
		houseFunctions.put("Spa / Massage",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
		houseFunctions.put("Post Office",new DrawDescription(DrawingType.NEUTRAL, DrawingType.NONE));
	}
	public void drawHouseBackground(PVector bl, PVector br, PVector tr, PVector tl, String catName){
		try {
//				DrawDescription d = new DrawDescription(DrawingType.OTHER, DrawingType.NONE); 
//			try{
//			//		d = houseFunctions.get(catName);
//			}catch (Exception e){
//				d = new DrawDescription(DrawingType.OTHER, DrawingType.NONE);
//			}
			java.lang.reflect.Method method = HouseDrawer.class.getMethod(houseFunctions.get(catName).background.toString().toLowerCase()+"BG",PVector.class, PVector.class, PVector.class, PVector.class, String.class); 
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
	
	public void drawHouseOverlay(PVector bl, PVector br, PVector tr, PVector tl, String catName, int size){
		//DrawDescription d = new DrawDescription(DrawingType.OTHER, DrawingType.NONE);
		
		//DrawingType dt = d.overlay;
		DrawingType dt = houseFunctions.get(catName).overlay;
		
		if(dt!=DrawingType.NONE){
			String drawCat = dt.toString().toLowerCase()+"Overlay";
		try {
			java.lang.reflect.Method method = HouseDrawer.class.getMethod(drawCat,PVector.class, PVector.class, PVector.class, PVector.class, String.class, Integer.class); 
			method.invoke(this, bl, br, tr, tl, catName, size);
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
	}
	
	
	/***
	 * 	background drawing functions
	 */
	
	// OTHER
		public void otherBG(PVector bl, PVector br, PVector tr, PVector tl, String catName){
			PGraphics citymap = CityGrid.p5.citymap;
			try {
			//citymap.fill(houseColors.get(catName).toARGB());
				citymap.fill(225,220,210);
			
		} catch (Exception e) {
			// TODO: handle exception
			citymap.noFill();
			System.out.println(catName);
		}
			citymap.pushStyle();
			//citymap.stroke(255);
			citymap.noStroke();
			drawQuad(citymap, bl, br, tr, tl, 0);
			citymap.popStyle();
		}
		
		public void plazaBG(PVector bl, PVector br, PVector tr, PVector tl, String catName){
			PGraphics citymap = CityGrid.p5.citymap;
			citymap.pushStyle();
			citymap.fill(225,220,210);
			citymap.noStroke();
			drawQuad(citymap, bl, br, tr, tl, 0);
			PVector mid = calcMid(br, tl);
			PVector size = calcSize(bl, br, tr, tl);
			citymap.fill(100);
			drawQuad(citymap, bl, br, tr, tl, 0.2f);
			citymap.fill(225,220,210);
			drawQuad(citymap, bl, br, tr, tl, 0.4f);
//			citymap.stroke(100);
//			citymap.strokeWeight(5);
//			PVector ttl = interpolateTo(tl, mid, 0.4f);
//			PVector ttr = interpolateTo(tr, mid, 0.4f);
//			PVector tbr = interpolateTo(br, mid, 0.4f);
//			PVector tbl = interpolateTo(bl, mid, 0.4f);
//			citymap.line(tbr.x,tbr.y,ttl.x,ttl.y);
//			citymap.line(tbl.x,tbl.y,ttr.x,ttr.y);
//			citymap.noStroke();
			citymap.fill(100);
			drawQuad(citymap, bl, br, tr, tl, 0.6f);
			citymap.fill(225,220,210);
			drawQuad(citymap, bl, br, tr, tl, 0.8f);
			citymap.popStyle();
		}
		public void parkBG(PVector bl, PVector br, PVector tr, PVector tl, String catName){
			PGraphics citymap = CityGrid.p5.citymap;
			citymap.pushStyle();
			citymap.fill(90, 178, 78);
//			citymap.stroke(255);
			citymap.noStroke();
			drawQuad(citymap, bl, br, tr, tl, 0);
			citymap.popStyle();
		}
		
		// NEUTRAL
		public void neutralBG(PVector bl, PVector br, PVector tr, PVector tl, String catName){
			PGraphics citymap = CityGrid.p5.citymap;
			citymap.pushStyle();
//			citymap.stroke(255);
			citymap.noStroke();
			citymap.fill(225,220,210);
			drawQuad(citymap, bl, br, tr, tl, 0);
			citymap.popStyle();
		}
		
		// HOTEL
		public void hotelBG(PVector bl, PVector br, PVector tr, PVector tl, String catName){
			PGraphics citymap = CityGrid.p5.citymap;
			citymap.pushStyle();
//			citymap.stroke(255);
			citymap.noStroke();
			citymap.fill(225,220,210);
			drawQuad(citymap, bl, br, tr, tl, 0);
			citymap.fill(210,200,200);
			citymap.stroke(180);
			citymap.strokeWeight(1);
			drawQuad(citymap, bl, br, tr, tl, .2f);
			drawQuad(citymap, bl, br, tr, tl, .3f);
			citymap.noStroke();
			citymap.popStyle();
		}
		
		public void whiteBG(PVector bl, PVector br, PVector tr, PVector tl, String catName){
			PGraphics citymap = CityGrid.p5.citymap;
			citymap.pushStyle();
//			citymap.stroke(255);
			citymap.noStroke();
			citymap.fill(255);
			drawQuad(citymap, bl, br, tr, tl, 0);
			citymap.popStyle();
		}
				
				
	
	/***
	 * 	
	 * Overlay Drawing Functions
	 */
			
		public void symbolOverlay(PVector bl, PVector br, PVector tr, PVector tl, String catName, Integer size){
			PGraphics citymap = CityGrid.p5.citymap;
			PVector mid =calcMid(br, tl);
			citymap.pushStyle();
			citymap.fill(0);
			citymap.shapeMode(PApplet.CENTER);
			PShape tSymbol = symbolManager.getSymbol(houseFunctions.get(catName).overlay);
			citymap.shape(tSymbol,mid.x,mid.y, (float)Math.sqrt((float)size)*tSymbol.width/10, (float)Math.sqrt((float)size)*tSymbol.height/10);
			//citymap.ellipse(mid.x,mid.y,10,10);
			citymap.popStyle();
		}
	// FOOD OVERLAY
	public void foodOverlay(PVector bl, PVector br, PVector tr, PVector tl, String catName, Integer size){
		symbolOverlay(bl, br, tr, tl, catName, size);
	}
	// BUS OVERLAY
		public void busOverlay(PVector bl, PVector br, PVector tr, PVector tl, String catName, Integer size){
			symbolOverlay(bl, br, tr, tl, catName, size);
		}
	// HOSPITAL OVERLAY
	public void hospitalOverlay(PVector bl, PVector br, PVector tr, PVector tl, String catName, Integer size){
		symbolOverlay(bl, br, tr, tl, catName, size);
	}
	// HOTEL OVERLAY
	public void hotelOverlay(PVector bl, PVector br, PVector tr, PVector tl, String catName, Integer size){
		symbolOverlay(bl, br, tr, tl, catName, size);
	}
	
	
	public PVector calcMid(PVector br, PVector tl){
		PVector mid = PVector.sub(br, tl);
		mid.div(2);
		mid.add(tl);
		return mid;
	}
	
	public PVector calcSize(PVector bl, PVector br, PVector tr, PVector tl){
		float x = br.x-bl.x;
		float y = br.y-tr.y;
		return new PVector(x,y);
	}
	
	public void drawQuad(PGraphics target, PVector bl, PVector br, PVector tr, PVector tl, float scaleF){
//		PVector bltr = PVector.sub(tr, bl);
//		PVector brtl = PVector.sub(tl, br);
//		bltbltr.mult(scaleF);
//		brtl.mult(scaleF);
		PVector mid = calcMid(br, tl);
		PVector tbl = interpolateTo(bl, mid, scaleF);
		PVector tbr = interpolateTo(br, mid, scaleF);
		PVector ttr = interpolateTo(tr, mid, scaleF);
		PVector ttl = interpolateTo(tl, mid, scaleF);
		target.beginShape();
		target.vertex(tbl.x, tbl.y);
		target.vertex(tbr.x, tbr.y);
		target.vertex(ttr.x, ttr.y);
		target.vertex(ttl.x, ttl.y);
		target.vertex(tbl.x, tbl.y);
		target.endShape();
	}
	
	public PVector interpolateTo(PVector v1, PVector v2, float f){
		  return new PVector(v1.x + (v2.x - v1.x) * f, v1.y + (v2.y - v1.y) * f);
	}
}

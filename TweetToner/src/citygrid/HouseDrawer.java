package citygrid;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.sun.org.apache.bcel.internal.generic.NEW;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;
import toxi.color.TColor;

public class HouseDrawer {
	HashMap<String, TColor> houseColors = new HashMap<String, TColor>();
	HashMap<String, DrawDescription> houseFunctions = new HashMap<String, DrawDescription>();
	SymbolManager symbolManager;
	
	public HouseDrawer(){
		symbolManager = new SymbolManager();
		
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
		
		houseFunctions.put("Food",new DrawDescription(DrawingType.WHITE, DrawingType.FOOD));
		houseFunctions.put("Spanish Restaurants", new DrawDescription(DrawingType.WHITE, DrawingType.FOOD));
		houseFunctions.put("Food & Drink Shops",new DrawDescription(DrawingType.WHITE, DrawingType.FOOD));
		houseFunctions.put("Travel & Transport",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Professional & Other Places",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Offices",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Shops & Services",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Outdoors & Recreation",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Arts & Entertainment",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Nightlife Spots",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Residences",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Airports",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Colleges & Universities",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Gyms or Fitness Centers",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Stadiums",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Clothing Stores",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Government Buildings",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Museums",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Medical Centers",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Bus Stations",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Performing Arts Venues",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Movie Theaters",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Spiritual Centers",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Train Stations",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Ferries",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Hotels",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Music Venues",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Athletics & Sports",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("College Academic Buildings",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Schools",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("College Stadiums",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Beaches",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Convention Centers",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
		houseFunctions.put("Ski Areas",new DrawDescription(DrawingType.OTHER, DrawingType.NONE));
	}
	public void drawHouseBackground(PVector bl, PVector br, PVector tr, PVector tl, String catName){
		try {
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
			citymap.fill(houseColors.get(catName).toARGB());
			
		} catch (Exception e) {
			// TODO: handle exception
			citymap.noFill();
			System.out.println(catName);
		}
			citymap.pushStyle();
			citymap.stroke(255);
			citymap.beginShape();
			citymap.vertex(bl.x, bl.y);
			citymap.vertex(br.x, br.y);
			citymap.vertex(tr.x, tr.y);
			citymap.vertex(tl.x, tl.y);
			citymap.endShape();
			citymap.popStyle();
		}
		
		// WHITE
		public void whiteBG(PVector bl, PVector br, PVector tr, PVector tl, String catName){
			PGraphics citymap = CityGrid.p5.citymap;
//			citymap.stroke(255);
			citymap.pushStyle();
			citymap.fill(255);
			citymap.beginShape();
			citymap.vertex(bl.x, bl.y);
			citymap.vertex(br.x, br.y);
			citymap.vertex(tr.x, tr.y);
			citymap.vertex(tl.x, tl.y);
			citymap.endShape();
			citymap.popStyle();
		}
		
				
				
	
	/***
	 * 	
	 * Overlay Drawing Functions
	 */
				
	// FOOD OVERLAY
	public void foodOverlay(PVector bl, PVector br, PVector tr, PVector tl, String catName, Integer size){
		PGraphics citymap = CityGrid.p5.citymap;
		PVector mid = PVector.sub(br, tl);
		mid.div(2);
		mid.add(tl);
		citymap.pushStyle();
		citymap.fill(0);
		citymap.shapeMode(PApplet.CENTER);
		PShape tSymbol = symbolManager.getSymbol(houseFunctions.get(catName).overlay);
		citymap.shape(tSymbol,mid.x,mid.y, (float)Math.sqrt((float)size)*tSymbol.width/10, (float)Math.sqrt((float)size)*tSymbol.height/10);
		//citymap.ellipse(mid.x,mid.y,10,10);
		citymap.popStyle();
	}
}

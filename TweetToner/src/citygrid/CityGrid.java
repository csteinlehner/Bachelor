package citygrid;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import com.csvreader.CsvReader;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class CityGrid extends PApplet{

	static final Boolean SAVE_PDF = true;		// true to save this as pdf
	
	
	protected static final String CITY = "berlin";
	
	private static String csvPath = "data/tweetcount_matrix_60_"+CITY+".csv";
	private static String fsqCsvPath = "data/fsq_timecount_30_"+CITY+".csv";
	
	public static CityGrid p5;

	
	private PFont font;

	float dayStreetSize = 12f;
	float hourStreetSize = 6f;
	
	PGraphics citymap; 
	
	private static final int  HOURS_INTERVAL = 30;
	private static final int DAY_MINUTES = 60*24-HOURS_INTERVAL;


	private Integer maxHours;
	
	int start = 0;
	int end = start+24;
	
	/// zoom & pan
	float zoom;
	PVector offset;
	PVector poffset;
	PVector mouse;
	float wheel = 1.0f;
	float wheelMult =0.10f;
	
	int mapWidth;
	int mapHeight;
	int bottomPoint;
	float sizeFactor = 2.2f;		// skalierung der karte insgesamt, je größer, desto kleiner die karte
	float heightFactor;				// wert zur normalisierung der höhenausbreitung
	private float hourSize = 200/sizeFactor;	// breite der stunde

	HouseDrawer houseDrawer;
	
	// holds the tweetCount with schema day (0-6), time(0-23), t_count
	Table<Integer, Integer, Integer> tweetCount = HashBasedTable.create();
	Table<Integer, Integer, Integer> tweetCountAdded = HashBasedTable.create();
	
	TreeBasedTable<Integer, Integer, FsqData> fsqCount = TreeBasedTable.create();
	
	TreeBasedTable<Integer, Integer, PVector> coordinates = TreeBasedTable.create();

	Vector<HouseCoordinate> houseCoordinates = new Vector<HouseCoordinate>();
	
	public void setup(){
		p5 = this;
		size(1200,900);
		frameRate(30);
		
		houseDrawer = new HouseDrawer();
		
		zoom = 1.0f;
		offset = new PVector(0, 0);
		poffset = new PVector(0, 0);
		addMouseWheelListener(new java.awt.event.MouseWheelListener() {
		    public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
		      mouseWheel(evt.getWheelRotation());
		  }});

		
		smooth();
		font = createFont("Axel-Bold",20);
			try {
			
			CsvReader csvData = new CsvReader(csvPath,',',Charset.forName("UTF-8"));
		
			csvData.readHeaders();
			int day = 0;
			int time = 0;
			int hCount = csvData.getHeaderCount();
			while (csvData.readRecord())
			{
				for (int i = 0; i < hCount; i++) {
					tweetCount.put(i, time, Integer.parseInt(csvData.get(i)));
				}
//				tweetCount.put();
				time++;
			}
			csvData.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			Map<Integer, Map<Integer, Integer>> rMap = tweetCount.rowMap();
			for (int i = 0; i < rMap.size(); i++) {
				Map<Integer, Integer> tMap = rMap.get(i);
				for (int j = 0; j < tMap.size(); j++) {
					if(j>0){
						tweetCountAdded.put(i, j, tMap.get(j)+tweetCountAdded.get(i,j-1));
					}else{
						tweetCountAdded.put(i, j, tMap.get(j));
					}
				}
			}
			
			
			try {
				CsvReader fsqData = new CsvReader(fsqCsvPath,',',Charset.forName("UTF-8"));
				fsqData.readHeaders();
				while (fsqData.readRecord())
				{
//						tweetCount.put(i, time, Integer.parseInt(csvData.get(i)));
					int day = Integer.parseInt(fsqData.get("day"));
					int minute = Integer.parseInt(fsqData.get("minute"));
					int hour = Integer.parseInt(fsqData.get("hour"));
					int t_count = Integer.parseInt(fsqData.get("count"));
					int f_count = Integer.parseInt(fsqData.get("fsq_count"));
					String catString = fsqData.get("Categories");
					String catParentString = fsqData.get("CategorieParents");
					
					fsqCount.put(day,minute,new FsqData(day, minute, hour, t_count, f_count));
					
					//// set the categories
					if(!catString.isEmpty()){
						HashMap<String, Integer> categories = new HashMap<String, Integer>();
						String[] catSplit = catString.split("_");
						for (int i = 0; i < catSplit.length; i++) {
							String[] tCatSplit = catSplit[i].split("=");
							categories.put(tCatSplit[0], Integer.parseInt(tCatSplit[1]));
						}
						fsqCount.get(day, minute).setCategories(categories);
					}
					
					//// set the categoryParents
					if(!catParentString.isEmpty()){
						HashMap<String, Integer> categoryParents = new HashMap<String, Integer>();
						String[] catParentSplit = catParentString.split("_");
						for (int i = 0; i < catParentSplit.length; i++) {
							String[] tCatSplit = catParentSplit[i].split("=");
							categoryParents.put(tCatSplit[0], Integer.parseInt(tCatSplit[1]));
						}
						fsqCount.get(day, minute).setCategoryParents(categoryParents);
					}
				}
				fsqData.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int hourLength = fsqCount.columnKeySet().size();
			int dayLength = fsqCount.rowKeySet().size();
			for (int h = 0; h < hourLength; h++) {
				for (int d = 0; d < dayLength; d++) {
					FsqData e = fsqCount.get(d, h*HOURS_INTERVAL);
					if(d!=0){
						e.t_count_added = e.t_count + fsqCount.get(d-1, h*HOURS_INTERVAL).t_count_added;
					}else{
						e.t_count_added=e.t_count;
					}
				}
			}
	
			maxHours = Collections.max(tweetCountAdded.values());
			heightFactor = 2000f/maxHours;
			
//			for (int i = 0; i < 7; i++) {
//				daystreets.add(new DayStreet(i+1, weekdaydata.get(i).t_count));
//			}
			
			textFont(font);
			mapHeight = (int)(maxHours*heightFactor/sizeFactor+200);
			mapWidth = (int)(hourSize*24+200);
			bottomPoint = mapHeight-50;
			
			createCoordinates();
			
			drawMap();
	}
	
	private void drawMap(){
		if(SAVE_PDF){
			citymap = createGraphics(mapWidth,mapHeight,PDF,"citygrid_shots/"+CITY+"_"+year()+month()+day()+"__"+hour()+"_"+minute()+"_"+second()+".pdf");
		}else{
			citymap = createGraphics(mapWidth,mapHeight,JAVA2D);
		}
		citymap.beginDraw();
		citymap.smooth();
//		citymap.background(220,230,220);
		citymap.background(255);
		citymap.translate(10,10);
		
		
		calcHouseArea();
		for(HouseCoordinate c : houseCoordinates){
			drawHousesBackground(c.bl, c.br, c.tr, c.tl, c.entry);
		}
		//// draw Streets
		drawHourStreets(color(110,100,100), hourStreetSize);
		drawDayStreets(color(110,100,100), dayStreetSize);
		drawHourStreets(color(255,240,230), hourStreetSize-2f);
		drawDayStreets(color(255,253,139), dayStreetSize-2f);
		for(HouseCoordinate c : houseCoordinates){
			drawHousesOverlay(c.bl, c.br, c.tr, c.tl, c.entry);
		}
		if(SAVE_PDF){
			citymap.dispose();
		}
		citymap.endDraw();
		if(SAVE_PDF){
			exit();
		}
	}
	
	public void draw(){
		background(0);
		zoom = wheel;
//		translate(100,30);
		pushMatrix();
		scale(zoom);
		translate((int)(offset.x/zoom), (int)(offset.y/zoom));
		image(citymap,0,0);
		popMatrix();
	}
	
	private void createCoordinates(){
		for (int d = 0; d < 7; d++) {
			for (int h = 0; h < 24; h++) {
//				if(d>0){
					coordinates.put(d,h, new PVector(h*hourSize,bottomPoint-tweetCountAdded.get(h, d)*heightFactor/sizeFactor-dayStreetSize*2*d));
//				}else{
//					coordinates.put(d,h, new PVector(h*hourSize,bottomPoint));
//				}
			}
		}
	}
	
	private void calcHouseArea(){
		citymap.fill(150,150,150);
		int hourLength = fsqCount.columnKeySet().size();
		int dayLength = fsqCount.rowKeySet().size();
		for (int d = 0; d < dayLength; d++) {
			for (int h = 0; h < hourLength; h++) {
				FsqData entry = fsqCount.get(d, h*HOURS_INTERVAL);
				if(entry.hasCategories){
					HashMap<String, Integer> categories = entry.categories;
					Set<Map.Entry<String, Integer>> set = categories.entrySet();
					Iterator<Entry<String,Integer>> it = set.iterator();
					PVector bl = new PVector();
					PVector br = new PVector();
					PVector tl = new PVector();
					PVector tr = new PVector();
				//	while(it.hasNext()){
						Map.Entry<String,Integer> me = it.next();
						/// calculates the total cell
							/// bottom left
						float bl_x = 0;
						if(h%2==0){
							bl_x = entry.minute/60f*hourSize;
						}else{
							bl_x = entry.minute/60f*hourSize-hourSize/2;
						}
							float bl_y;
							if(d>0){
								bl_y = coordinates.get(d-1, entry.hour).y;
							}else{
								bl_y = bottomPoint;
							}
							bl = new PVector(bl_x,bl_y);
							
							/// bottom right
							float br_x = 0; 
							if(h%2==0){
								br_x = entry.minute/60f*hourSize+hourSize;
							}else{
								br_x = entry.minute/60f*hourSize+hourSize-hourSize/2;
							}
							float br_y;
							float t_hourMove;
							if(h%2==0){
								t_hourMove = hourLength-2;
							}else{
								t_hourMove = hourLength-1;
							}
							if(d>0 && h<t_hourMove){
								br_y = coordinates.get(d-1, entry.hour+1).y;
							}else if (d>0 && h==t_hourMove){
						 		br_y = coordinates.get(d-1, entry.hour).y;
							}else{
								br_y = bottomPoint;
							}
							br = new PVector(br_x,br_y);
							
							/// top left
							float tl_x = bl_x;
							float tl_y = coordinates.get(d,entry.hour).y;
							tl = new PVector(tl_x,tl_y);
							
							/// top right
							float tr_x = br_x;
							float tr_y;
							if(h<hourLength-2){
								tr_y = coordinates.get(d, entry.hour+1).y;
							}else{
								tr_y = coordinates.get(d, entry.hour).y;
							}
							tr = new PVector(tr_x, tr_y);
							
							
						PVector d_br_1;
						PVector d_tr_1;
						PVector d_bl_2;
						PVector d_tl_2;
						
						//// draw left half
						
						if(h%2==0){
							d_br_1 = PVector.sub(br, bl);
							d_br_1.div(2);
							d_br_1.add(bl);
							
							d_tr_1 = PVector.sub(tr,tl);
							d_tr_1.div(2);
							d_tr_1.add(tl);
							
							houseCoordinates.add(new HouseCoordinate(bl, d_br_1, d_tr_1, tl, entry));
						}
						//// draw right half
						else{
							d_bl_2 = PVector.sub(br, bl);
							d_bl_2.div(2);
							d_bl_2.add(bl);
							
							d_tl_2 = PVector.sub(tr,tl);
							d_tl_2.div(2);
							d_tl_2.add(tl);
							
							houseCoordinates.add(new HouseCoordinate(d_bl_2, br, tr, d_tl_2, entry));
						}
				}
			}
		}
	}

	private void drawHousesBackground(PVector bl, PVector br, PVector tr, PVector tl, FsqData entry){
		PVector tbl = new PVector(bl.x, bl.y);
		PVector tbr = new PVector(br.x, br.y);
		PVector ttr = new PVector(tr.x, tr.y);
		PVector ttl = new PVector(tl.x, tl.y);
		float lLength = tbl.y-ttl.y;
		float rLength = tbr.y-ttr.y;
		
		citymap.noFill();
//		System.out.println(entry.categoryParents + " : " + lLength + " / " + lPart);
		float oTly = tbl.y;
		float oTry = tbr.y;
		for( String key : entry.categories.keySet()){
			ttl.y = oTly - entry.categoryPercent.get(key)*lLength;
			ttr.y = oTry - entry.categoryPercent.get(key)*rLength;
			houseDrawer.drawHouseBackground(tbl, tbr, ttr, ttl, key);
			tbl.y = ttl.y;
			tbr.y = ttr.y;
			oTly = ttl.y;
			oTry = ttr.y;
		}
	}
	private void drawHousesOverlay(PVector bl, PVector br, PVector tr, PVector tl, FsqData entry){
		PVector tbl = new PVector(bl.x, bl.y);
		PVector tbr = new PVector(br.x, br.y);
		PVector ttr = new PVector(tr.x, tr.y);
		PVector ttl = new PVector(tl.x, tl.y);
		float lLength = tbl.y-ttl.y;
		float rLength = tbr.y-ttr.y;
		
		
		citymap.noFill();
//		System.out.println(entry.categoryParents + " : " + lLength + " / " + lPart);
		float oTly = tbl.y;
		float oTry = tbr.y;
		for( String key : entry.categories.keySet()){
			ttl.y = oTly - entry.categoryPercent.get(key)*lLength;
			ttr.y = oTry - entry.categoryPercent.get(key)*rLength;
			houseDrawer.drawHouseOverlay(tbl, tbr, ttr, ttl, key, entry.categories.get(key));
			tbl.y = ttl.y;
			tbr.y = ttr.y;
			oTly = ttl.y;
			oTry = ttr.y;
		}
	}
	
	private void drawHourStreets(int color, float thickness){
		citymap.pushStyle();
		citymap.noFill();
		citymap.stroke(color);
		citymap.strokeWeight(thickness);
		citymap.strokeCap(SQUARE);
		for (int h = 0; h < 24; h++) {
//			map.beginShape();
//			map.vertex(h*hourSize,bottomPoint);
//			println(tweetCountAdded.get(0,1));
//			println("--"+i);
			for (int d = 0; d < 7; d++) {
				if(d>0){
					PVector p1 = coordinates.get(d-1,h);
					PVector p2 = coordinates.get(d,h);
					citymap.line(p1.x,p1.y,p2.x,p2.y);
				}else{
					PVector p2 = coordinates.get(d,h);
					citymap.line(p2.x,bottomPoint,p2.x,p2.y);
				}
//				println(tweetCountAdded.get(i,j));
//				PVector p = coordinates.get(d, h);
//				map.vertex(p.x, p.y);
			}	
//			map.endShape();
		}
		citymap.popStyle();
	}
	private void drawDayStreets(int color, float thickness){
		citymap.pushStyle();
		citymap.noFill();
		citymap.stroke(color);
		citymap.strokeWeight(thickness);
		citymap.strokeCap(SQUARE);
		for (int d = 0; d < 7; d++) {
			for (int h = 0; h <= 24; h++) {
				if(h==24){
					PVector p2 = coordinates.get(d, h-1);
					citymap.line(p2.x,p2.y,p2.x+hourSize,p2.y);
				}else if(h>0){
					PVector p1 = coordinates.get(d, h-1);
					PVector p2 = coordinates.get(d, h);
					citymap.line(p1.x,p1.y,p2.x,p2.y);
				}
			}
		}
		citymap.popStyle();
	}
	
	
	public static void main(String args[])
	{
		//PApplet.main(new String[] { "--present", mailgod.MailGod.class.getName()});
		PApplet.main(new String[] {  citygrid.CityGrid.class.getName()});
	}
	
	public void mousePressed() {
		  mouse = new PVector(mouseX, mouseY);
		  poffset.set(offset);
		}

		// Calculate the new offset based on change in mouse vs. previous offsey
	public void mouseDragged() {
		  offset.x = mouseX - mouse.x + poffset.x;
		  offset.y = mouseY - mouse.y + poffset.y;
		}
	
	void mouseWheel(int step) {
	      wheel=(constrain(wheel+step*wheelMult*-1f,0.1f,50f));
	}

	public void keyPressed() {
	  if (key == CODED) {
	    if (keyCode == UP) {
	     start += 24;
	     end += 24;
	    } else if (keyCode == DOWN) {
	    	start -= 24;
		     end -= 24;
	    } 
	  }
	}
}

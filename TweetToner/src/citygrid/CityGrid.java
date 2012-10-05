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

import com.csvreader.CsvReader;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import processing.core.PApplet;
import processing.core.PFont;

public class CityGrid extends PApplet{
	
	protected static final String CITY = "berlin";
	
	private static String csvPath = "data/tweetcount_matrix_60_"+CITY+".csv";
	private static String fsqCsvPath = "data/fsq_timecount_30_"+CITY+".csv";
	
	public static PApplet p5; 
	
	private PFont font;
	
	private float fontSize = 12f;
	
	private static final int  HOURS_INTERVAL = 30;
	private static final int DAY_MINUTES = 60*24-HOURS_INTERVAL;
	
	private float hourSize = 40f;
	private Integer maxHours;
	private Integer maxWidth = 800;
	private Integer bottomPoint = 800;
	
	int start = 0;
	int end = start+24;
	
	// holds the tweetCount with schema day (0-6), time(0-23), t_count
	Table<Integer, Integer, Integer> tweetCount = HashBasedTable.create();
	Table<Integer, Integer, Integer> tweetCountAdded = HashBasedTable.create();
	
	TreeBasedTable<Integer, Integer, FsqData> fsqCount = TreeBasedTable.create();

	
	public void setup(){
		p5 = this;
		size(1200,900);
		frameRate(30);
		smooth();
		font = createFont("Axel-Bold",20);
			try {
			
			CsvReader csvData = new CsvReader(csvPath,';',Charset.forName("UTF-8"));
		
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
					
					if(!catString.isEmpty()){
					HashMap<String, Integer> categories = new HashMap<String, Integer>();
					String[] catSplit = catString.split("_");
						for (int i = 0; i < catSplit.length; i++) {
							String[] tCatSplit = catSplit[i].split("=");
							categories.put(tCatSplit[0], Integer.parseInt(tCatSplit[1]));
						}
							fsqCount.put(day,minute,new FsqData(day, minute, hour, t_count, f_count, categories));
					}else{
						fsqCount.put(day,minute,new FsqData(day, minute, hour, t_count, f_count));	
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
			
			
//			for (int i = 0; i < 7; i++) {
//				daystreets.add(new DayStreet(i+1, weekdaydata.get(i).t_count));
//			}
			
			textFont(font);
	}
	
	public void draw(){
		background(220,230,220);
		
		translate(100,30);
		
		//// draw houses
		fill(255,0,0);
		
		int hourLength = fsqCount.columnKeySet().size();
		int dayLength = fsqCount.rowKeySet().size();
		for (int d = 0; d < dayLength; d++) {
			for (int h = 0; h < hourLength; h++) {
				FsqData entry = fsqCount.get(d, h*HOURS_INTERVAL);
				if(entry.hasCategories){
					HashMap<String, Integer> categories = entry.categories;
					Set<Map.Entry<String, Integer>> set = categories.entrySet();
					Iterator<Entry<String,Integer>> it = set.iterator();
					while(it.hasNext()){
						Map.Entry<String,Integer> me = it.next();
						if(d>0){
							rect(entry.minute/60f*hourSize,bottomPoint-map(tweetCountAdded.get(entry.hour,d-1),0,maxHours,0,maxWidth)-me.getValue()*10-10,10,me.getValue()*10);
						}else{
							rect(h/2*(hourLength/2),bottomPoint-15,10,10);
						}
					}
				}
			}
		}
//		for (int d = 0; d < 7; d++) {
//			for (int h = 0; h < 24; h++) {
//				if(d>0){
//					rect(h*hourSize,bottomPoint-map(tweetCountAdded.get(h, d-1),0,maxHours,0,maxWidth)-15,10,10);
//				}else{
//					rect(h*hourSize,bottomPoint-15,10,10);
//				}
//			}
//		}
		
		//// draw Streets
		drawHourStreets(color(30,30,30), 5f);
		drawDayStreets(color(30,30,30), 7f);
		drawHourStreets(color(100,200,200), 3f);
		drawDayStreets(color(300,200,200), 5f);

	}
	
	private void drawHourStreets(int color, float thickness){
		pushStyle();
		noFill();
		stroke(color);
		strokeWeight(thickness);
		strokeCap(SQUARE);
		for (int h = 0; h < 24; h++) {
			beginShape();
			vertex(h*hourSize,bottomPoint);
//			println(tweetCountAdded.get(0,1));
//			println("--"+i);
			for (int d = 0; d < 7; d++) {
//				println(tweetCountAdded.get(i,j));
				vertex(h*hourSize, bottomPoint-map(tweetCountAdded.get(h, d),0,maxHours,0,maxWidth));
			}	
			endShape();
		}
		popStyle();
	}
	private void drawDayStreets(int color, float thickness){
		pushStyle();
		noFill();
		stroke(color);
		strokeWeight(thickness);
		strokeCap(SQUARE);
		for (int i = 0; i < 7; i++) {
			beginShape();
			for (int j = 0; j < 24; j++) {
				if(i>0){
					vertex(j*hourSize,bottomPoint-map(tweetCountAdded.get(j, i-1),0,maxHours,0,maxWidth));
				}else{
					vertex(j*hourSize,bottomPoint);
				}
			}
			endShape();
		}
		popStyle();
	}
	
	
	public static void main(String args[])
	{
		//PApplet.main(new String[] { "--present", mailgod.MailGod.class.getName()});
		PApplet.main(new String[] {  citygrid.CityGrid.class.getName()});
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

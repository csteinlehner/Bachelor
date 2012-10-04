package citygrid;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import citygrid.WeekDay;

import com.csvreader.CsvReader;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import processing.core.PApplet;
import processing.core.PFont;

public class CityGrid extends PApplet{
	
	protected static final String CITY = "berlin";
	
	private static String csvPath_h = "data/tweetcount_matrix_60_"+CITY+".csv";
	private static String csvPath_d = "data/tweetcount_matrix_60_d"+CITY+".csv";
	
	public static PApplet p5; 
	
	private PFont font;
	
	private float fontSize = 12f;
	
	private float hourSize = 100f;
	private Integer maxHours_h;
	private Integer maxSize_h = 800;
	private Integer bottomPoint = 800;
	
	int start = 0;
	int end = start+24;
	
	// holds the tweetCount with schema day (0-6), time(0-23), t_count
	Table<Integer, Integer, Integer> tweetCount_h = HashBasedTable.create();
	Table<Integer, Integer, Integer> tweetCountAdded_h = HashBasedTable.create();

	
	Vector<WeekDay> weekdaydata = new Vector<WeekDay>();
	Vector<DayStreet> daystreets = new Vector<DayStreet>();
	
	public void setup(){
		p5 = this;
		size(1200,900);
		frameRate(30);
		smooth();
		font = createFont("Axel-Bold",20);
			try {
			
			CsvReader csvData_h = new CsvReader(csvPath_h,';',Charset.forName("UTF-8"));
		
			csvData_h.readHeaders();
			int day = 0;
			int time = 0;
			int hCount = csvData_h.getHeaderCount();
			while (csvData_h.readRecord())
			{
				for (int i = 0; i < hCount; i++) {
					tweetCount_h.put(i, time, Integer.parseInt(csvData_h.get(i)));
				}
//				tweetCount.put();
				time++;
			}
			csvData_h.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
			// calculate hour maps
			Map<Integer, Map<Integer, Integer>> rMap = tweetCount_h.columnMap();
			for (int i = 0; i < rMap.size(); i++) {
				Map<Integer, Integer> tMap = rMap.get(i);
				for (int j = 0; j < tMap.size(); j++) {
					if(j>0){
						tweetCountAdded_h.put(i, j, tMap.get(j)+tweetCountAdded_h.get(i,j-1));
					}else{
						tweetCountAdded_h.put(i, j, tMap.get(j));
					}
				}
			}
			maxHours_h = Collections.max(tweetCountAdded_h.values());
			
//			for (int i = 0; i < 7; i++) {
//				daystreets.add(new DayStreet(i+1, weekdaydata.get(i).t_count));
//			}
			
			textFont(font);
	}
	
	public void draw(){
		background(220,230,220);
		
		float step = 2*PI/(end-start);
		
		float weekstep = 2*PI/7;
		translate(100,30);
		
		//// draw day lines
		stroke(0,0,255);
		for (int d = 0; d < 7; d++) {
			beginShape();
			vertex(d*hourSize,bottomPoint);
//			println(tweetCountAdded.get(0,1 ));
//			println("--"+i);
			for (int h = 0; h < 24; h++) {
//				println(tweetCountAdded.get(i,j));
				vertex(d*hourSize, bottomPoint-map(tweetCountAdded_h.get(d, h),0,maxHours_h,0,maxSize_h));
			}	
			endShape();
		}
		stroke(0);
		//// draw hour lines
		for (int h = 0; h < 24; h++) {
			beginShape();
			for (int d = 0; d < 7; d++) {
				 if(h>0){
					vertex(d*hourSize,bottomPoint-map(tweetCountAdded_h.get(d, h-1),0,maxHours_h,0,maxSize_h));
				}else{
					vertex(d*hourSize,bottomPoint);
				}
			}
			endShape();
		}
		
//		
//		pushMatrix();
//	translate(100,height/4);
////		rotate(HALF_PI);
//		stroke(0);
//		noFill();
//		for (int i = 0; i < 7; i++) {
////			WeekDay d = weekdaydata.get(i);
////			rotate(weekstep);
//			pushMatrix();
//			rotate(HALF_PI);
//			daystreets.get(i).draw();
//			popMatrix();
//			CityGrid.p5.translate(140,0);
//		}
//		popMatrix();
		
//		pushMatrix();
//		translate(width/2,height/2);
//		int cRadius = 150;
//		int j = 0;
//		for (int i = start; i < end; i++, j++) {
//			  float x1 = cos(step*j) * cRadius;
//			  float y1 = sin(step*j) * cRadius;
//			  SingleData d = data.get(i);
//			  if(d.hasColor){
//			  stroke(d.red, d.green, d.red);
//			  strokeWeight(20);
//			  strokeCap(SQUARE);
//			  noFill();
//			  arc(0,0,300,300,step*j,step*(j+1));
////			  ellipse(x1,y1,20,20);
//			  }
//		}
//		popMatrix();
		
//		fill(0,150,0);
		
//		float tCountRadius = 100;
		stroke(200,150,150);
		noFill();
		strokeWeight(1);

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

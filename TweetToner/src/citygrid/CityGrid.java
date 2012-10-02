package citygrid;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Vector;

import citygrid.WeekDay;

import com.csvreader.CsvReader;

import processing.core.PApplet;
import processing.core.PFont;

public class CityGrid extends PApplet{
	
	protected static final String CITY = "berlin";
	
	private static String csvPath = "data/fsq_timecount_days_"+CITY+".csv";
	
	public static PApplet p5; 
	
	private PFont font;
	
	private float fontSize = 12f;
	
	int start = 0;
	int end = start+24;
	
	Vector<WeekDay> weekdaydata = new Vector<WeekDay>();
	Vector<DayStreet> daystreets = new Vector<DayStreet>();
	
	public void setup(){
		p5 = this;
		size(1200,900);
		frameRate(30);
		smooth();
		font = createFont("Axel-Bold",20);
			try {
			
			CsvReader csvData = new CsvReader(csvPath,',',Charset.forName("UTF-8"));
		
			csvData.readHeaders();

			while (csvData.readRecord())
			{
				weekdaydata.add(new WeekDay(csvData.get("time"), Integer.parseInt(csvData.get("count"))));
			}
	
			csvData.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			for (int i = 0; i < 7; i++) {
				daystreets.add(new DayStreet(i+1, weekdaydata.get(i).t_count));
			}
			
			textFont(font);
	}
	
	public void draw(){
		background(220,230,220);
		
		float step = 2*PI/(end-start);
		
		float weekstep = 2*PI/7;
		
		pushMatrix();
	translate(100,height/4);
//		rotate(HALF_PI);
		stroke(0);
		noFill();
		for (int i = 0; i < 7; i++) {
//			WeekDay d = weekdaydata.get(i);
//			rotate(weekstep);
			pushMatrix();
			rotate(HALF_PI);
			daystreets.get(i).draw();
			popMatrix();
			CityGrid.p5.translate(140,0);
		}
		popMatrix();
		
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

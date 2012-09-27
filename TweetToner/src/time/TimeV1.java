package time;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.Vector;

import com.csvreader.CsvReader;

import foursquare.FoursquarePoint;
import processing.core.PApplet;
import processing.core.PFont;

public class TimeV1 extends PApplet{
	
	private static final String CITY = "berlin";
	
	private static String csvPath = "data/fsq_timecount_60_"+CITY+".csv";
	
	private PFont font;
	
	private float fontSize = 12f;
	
	Vector<SingleData> data = new Vector<SingleData>();
	
	public void setup(){
		size(1200,900);
		smooth();
		font = createFont("Axel-Bold",20);
			try {
			
			CsvReader csvData = new CsvReader(csvPath,',',Charset.forName("UTF-8"));
		
			csvData.readHeaders();

			while (csvData.readRecord())
			{
				data.add(new SingleData(csvData.get("time"), Float.parseFloat(csvData.get("count")), Float.parseFloat(csvData.get("fsq_count")), csvData.get("CategorieParents"),csvData.get("Categories")));
			}
	
			csvData.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			textFont(font);
	}
	
	public void draw(){
		background(220,230,220);
		int start = 0;
		int end = start+24;
		float step = 2*PI/(end-start);
		 
//		fill(0,150,0);
		
		float tCountRadius = 100;
		stroke(200,150,150);
		noFill();
		pushMatrix();
//		strokeCap(SQUARE);
		translate(width/2,height/2);
		rotate(-HALF_PI);
		float lastx = cos(0) * tCountRadius;
		float lasty = sin(0) * tCountRadius;
		for (int i = start; i < end; i++) {
			  float offset = data.get(i).t_count * 0.1f;
			  strokeWeight(offset);
			  float x = cos(step*i) * (tCountRadius);
			  float y = sin(step*i) * (tCountRadius);
			  line(lastx,lasty,x,y);
			  lastx = x;
			  lasty = y;
		}
		popMatrix();
		
		float radius = 200;
		// foursquare cat names
		noStroke();
		for (int i = start; i < end; i++) {
			SingleData dp = data.get(i);
			pushMatrix();
			translate(width/2,height/2);
			rotate(step*i);
			translate(0,-radius);
//			float sizeB = 10*sqrt(data.get(i).fsq_count);
			fill(40,30,30);
			if(dp.hasCats){
				rotate(-HALF_PI);
				float sizeF = sqrt(dp.categories.descendingMultiset().firstEntry().getCount())*10;
				textSize(fontSize+sizeF);
				text(dp.categories.descendingMultiset().firstEntry().getElement(),0,0);
			}
			popMatrix();
		}
		// foursquare quads
		rectMode(CENTER);
		noStroke();
		for (int i = start; i < end; i++) {
			pushMatrix();
			translate(width/2,height/2);
			rotate(step*i);
			translate(0,-150);
			float sizeB = 7*sqrt(data.get(i).fsq_count);
			fill(40,30,30);
			rect(0,0,sizeB,sizeB);
			popMatrix();
		}

	}
	public static void main(String args[])
	{
		//PApplet.main(new String[] { "--present", mailgod.MailGod.class.getName()});
		PApplet.main(new String[] {  time.TimeV1.class.getName()});
	}
}

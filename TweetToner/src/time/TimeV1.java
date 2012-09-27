package time;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Vector;

import com.csvreader.CsvReader;

import foursquare.FoursquarePoint;
import processing.core.PApplet;

public class TimeV1 extends PApplet{
	
	private static final String CITY = "berlin";
	
	private static String csvPath = "data/fsq_timecount_60_"+CITY+".csv";
	
	Vector<SingleData> data = new Vector<SingleData>();
	
	public void setup(){
		size(1200,900);
		smooth();
		
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

	}
	
	public void draw(){
		background(220,230,220);
		int start = 0;
		int end = 24;
		float step = 2*PI/(end-start);
		noStroke();
		fill(40,30,30);
		rectMode(CENTER);
		for (int i = start; i < end; i++) {
			pushMatrix();
			translate(width/2,height/2);
			rotate(step*i);
			translate(0,-200);
			float size = 10*sqrt(data.get(i).fsq_count);
			rect(0,0,size,size);
			popMatrix();
		}
		stroke(255);
		strokeWeight(3);
		noFill();
		pushMatrix();
		translate(width/2,height/2);
		rotate(-HALF_PI);
		beginShape();
//		float r = 10;
		for (int i = start; i < end; i++) {
			  float x = cos(step*i) * data.get(i).t_count;
			  float y = sin(step*i) * data.get(i).t_count;
			vertex(x,y);
		}
		endShape(CLOSE);
		popMatrix();
	}
	public static void main(String args[])
	{
		//PApplet.main(new String[] { "--present", mailgod.MailGod.class.getName()});
		PApplet.main(new String[] {  time.TimeV1.class.getName()});
	}
}

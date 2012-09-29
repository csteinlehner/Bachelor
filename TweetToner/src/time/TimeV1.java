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
		frameRate(30);
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
		 
		// skala
		pushMatrix();
		float skalaRadius = 200;
		float skalaLineLenght = 10;
		translate(width/2,height/2);
		stroke(100,90,90);
		strokeWeight(1);
		noFill();
		for (int i = start; i < end; i++) {
			  float x1 = cos(step*i) * skalaRadius;
			  float y1 = sin(step*i) * skalaRadius;
			  float x2 = cos(step*i) * (skalaRadius+skalaLineLenght);
			  float y2 = sin(step*i) * (skalaRadius+skalaLineLenght);
			  line(x1,y1,x2,y2);
		}
		popMatrix();
		
		
//		fill(0,150,0);
		
//		float tCountRadius = 100;
		stroke(200,150,150);
		noFill();
		strokeWeight(1);
//		noStroke();
		pushMatrix();
//		strokeCap(SQUARE);
		translate(width/2,height/2);
		rotate(-HALF_PI);
		rotate(step/2);
		float tweetCRadius = 100;
		ellipse(0,0,tweetCRadius*2,tweetCRadius*2);
		beginShape();
		curveVertex(cos(step)*( map(data.get(0).t_count,0,400,0,100))+tweetCRadius,0);
		int j = 0;
		for (int i = start; i < end; i++, j++) {
			  float offset = map(data.get(i).t_count,0,300,0,100) + tweetCRadius;
			  float x = cos(step*j) * offset;
			  float y = sin(step*j) * offset;
			  curveVertex(x,y);
//			  ellipse(x,y,10,10);
		}
		curveVertex(cos(step*end) * ( map(data.get(end).fsq_count,0,400,0,100)+tweetCRadius),sin(step*end) * (map(data.get(end).fsq_count,0,400,0,100)+tweetCRadius));
		endShape();
		beginShape();
		curveVertex(cos(step)*( map(data.get(0).fsq_count,0,400,0,100))+tweetCRadius,0);
		j = 0;
		for (int i = start; i < end; i++, j++) {
			  float offset = map(data.get(i).fsq_count,0,300,0,100) + tweetCRadius;
			  float x = cos(step*j) * offset;
			  float y = sin(step*j) * offset;
			  curveVertex(x,y);
//			  ellipse(x,y,10,10);
		}
		curveVertex(cos(step*end) * ( map(data.get(end).fsq_count,0,400,0,100)+tweetCRadius),sin(step*end) * (map(data.get(end).fsq_count,0,400,0,100)+tweetCRadius));
		endShape();
		popMatrix();
		
		float radius = 200;
		// foursquare cat names
//		noStroke();
//		for (int i = start; i < end; i++) {
//			SingleData dp = data.get(i);
//			pushMatrix();
//			translate(width/2,height/2);
//			rotate(step*i);
//			translate(0,-radius);
////			float sizeB = 10*sqrt(data.get(i).fsq_count);
//			fill(40,30,30);
//			if(dp.hasCats){
//				rotate(-HALF_PI);
//				float sizeF = sqrt(dp.categories.descendingMultiset().firstEntry().getCount())*10;
//				textSize(fontSize+sizeF);
//				text(dp.categories.descendingMultiset().firstEntry().getElement(),0,0);
//			}
//			popMatrix();
//		}
		// foursquare quads
//		rectMode(CENTER);
//		noStroke();
//		for (int i = start; i < end; i++) {
//			pushMatrix();
//			translate(width/2,height/2);
//			rotate(step*i);
//			translate(0,-150);
//			float sizeB = 7*sqrt(data.get(i).fsq_count);
//			fill(40,30,30);
//			rect(0,0,sizeB,sizeB);
//			popMatrix();
//		}

	}
	public static void main(String args[])
	{
		//PApplet.main(new String[] { "--present", mailgod.MailGod.class.getName()});
		PApplet.main(new String[] {  time.TimeV1.class.getName()});
	}
}

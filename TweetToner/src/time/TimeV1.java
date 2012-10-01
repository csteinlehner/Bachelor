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
		float tweetCRadius = 100;
		int j = 0;
		for (int i = start; i < end; i++, j++) {
			  float t_offset = map(data.get(i).t_count,0,300,0,500);
			  float fsq_offset = t_offset-map(data.get(i).fsq_count,0,300,0,500);
			  float x = cos(step) * t_offset;
			  float y = sin(step) * t_offset;
			  arc(0,0,t_offset,t_offset,step*j,step*(j+1));
			  arc(0,0,fsq_offset,fsq_offset,step*j,step*(j+1));
			  line(0,0,cos(step*j) * (t_offset/2),sin(step*j) * (t_offset/2));
			  line(0,0,cos(step*(j+1)) * (t_offset/2),sin(step*(j+1)) * (t_offset/2));
//			  ellipse(x,y,10,10);
		}
		popMatrix();


	}
	public static void main(String args[])
	{
		//PApplet.main(new String[] { "--present", mailgod.MailGod.class.getName()});
		PApplet.main(new String[] {  time.TimeV1.class.getName()});
	}
}

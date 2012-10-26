package analyse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.Vector;

import com.csvreader.CsvReader;

import foursquare.FoursquarePoint;
import processing.core.PApplet;
import processing.core.PFont;

public class UserMovement extends PApplet{
	
	private static final String CITY = "berlin";
	
	private static String csvPath = "data/user_individual_count/user_individual_count_60_"+CITY+".csv";
	
	private PFont font;
	
	private float fontSize = 12f;
	
	int start = 0;
	int end = start+24;
	
	Float[] mov_size = new Float[24*7];
	
	public void setup(){
		size(1200,900);
		frameRate(30);
		smooth();
//		println(PFont.list());
		font = createFont("MuseoSlab-500",20);
			try {
			
			CsvReader csvData = new CsvReader(csvPath,',',Charset.forName("UTF-8"));
		
			csvData.readHeaders();
			background(255);
			int i = 0;
			while (csvData.readRecord())
			{
//				data.add(new SingleData(csvData.get("time"), Float.parseFloat(csvData.get("count")), csvData.get("red"), csvData.get("green"),csvData.get("blue")));
				mov_size[i] = Float.parseFloat(csvData.get("distance_median"));
				i++;
			}
	
			csvData.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			textFont(font);
			textAlign(CENTER);
	}
	
	public void draw(){
		background(255);
		float step = 2*PI/(end-start);
		start = 0;
		end = start+24;
		// skala
		pushMatrix();
		float skalaRadius = 142;
		float skalaLineLenght = 200;
		translate(width/2,height/2);
		rotate(-HALF_PI);
		stroke(120);
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

		  pushMatrix();
		  float digitRadius = 350;
		  fill(120);
		  //rotate(HALF_PI);
		  translate(width/2,height/2);
		  rotate(-HALF_PI);
		  for (int i = start; i < end; i++) {
			  pushMatrix();
			  translate(cos(step*i) * digitRadius, sin(step*i) * digitRadius);
			  rotate(HALF_PI+TWO_PI/24*i);
			  text(i,0,0);
			  popMatrix();
		  }
		  noFill();
		  popMatrix();

	
		int cRadius = 150;
		int k = 0;
		while(end<=168){
		pushMatrix();
		translate(width/2,height/2);
		rotate(-HALF_PI);
			int j = 0;
			noFill();
//			println("aussen: "+end);
			beginShape();
			curveVertex(cos(step*j) * cRadius+mov_size[start],sin(step*j) * cRadius+mov_size[start]);
			for (int i = start; i < end; i++, j++) {
				  float x1 = cos(step*j) * (cRadius+mov_size[i]*10+20*k);
				  float y1 = sin(step*j) * (cRadius+mov_size[i]*10+20*k);
				  curveVertex(x1,y1);
//				  SingleData d = data.get(i);
//				  if(d.hasColor){
//					  stroke(d.red, d.green, d.red);
//					  strokeWeight(20);
//					  strokeCap(SQUARE); 
//					  arc(0,0,300+50*k,300+50*k,step*j,step*(j+1));
//		//			  ellipse(x1,y1,20,20);
//				  }else{
//					  stroke(120);
//					  strokeWeight(1);
//					  strokeCap(SQUARE); 
//					  arc(0,0,300+50*k,300+50*k,step*j,step*(j+1));					  
//				  }
				  
			}
			curveVertex(cos(0) * (cRadius+mov_size[start]*10+20*k),sin(0) * (cRadius+mov_size[start]*10+20*k));
			curveVertex(cos(0) * (cRadius+mov_size[start]*10+20*k),sin(0) * (cRadius+mov_size[start]*10+20*k));
			endShape();
			start +=24;
			end +=24;
			k++;
			popMatrix();
		}
	
		
//		fill(0,150,0);
		
//		float tCountRadius = 100;
		stroke(200,150,150);
		noFill();
		strokeWeight(1);
	}
	
	public static void main(String args[])
	{
		//PApplet.main(new String[] { "--present", mailgod.MailGod.class.getName()});
		PApplet.main(new String[] {  analyse.UserMovement.class.getName()});
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

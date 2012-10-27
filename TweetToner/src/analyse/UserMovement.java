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
	
	private static final String CITY = "sanfrancisco";
	
	private static String csvPath = "data/user_individual_count/user_individual_count_60_"+CITY+".csv";
	
	private PFont font;
	
	private float fontSize = 12f;
	
	int start = 0;
	int end = start+24;
	
	Float[] mov_size = new Float[24*7];
	Float[] mov_size_max = new Float[24*7];
	
	public void setup(){
//		size(1200,800);
		size(4000, 2500, PDF, "user_movement_"+CITY+".pdf");
		frameRate(30);
		smooth();
//		println(PFont.list());
		font = createFont("Axel-Regular",20);
			try {
			
			CsvReader csvData = new CsvReader(csvPath,',',Charset.forName("UTF-8"));
		
			csvData.readHeaders();
			background(255);
			int i = 0;
			while (csvData.readRecord())
			{
//				data.add(new SingleData(csvData.get("time"), Float.parseFloat(csvData.get("count")), csvData.get("red"), csvData.get("green"),csvData.get("blue")));
				mov_size[i] = Float.parseFloat(csvData.get("distance_median"));
				mov_size_max[i] = Float.parseFloat(csvData.get("distance_max"));
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
			strokeCap(SQUARE);
	}
	
	public void draw(){
		background(255);
	
		int bottom = height-50;
		int stepSize = 90;
		int colSize = 300;
		start = 0;
		end = start+24;
		int k = 0;
		
		translate(30,0);
		
		textSize(12);

		// draw skala
		stroke(200);
		
		for(int kk = 0; kk < 7; kk++) {
			for (int i = 0; i <= 20; i++) {
				line(kk*colSize+i*10, bottom, kk*colSize+i*10, bottom-stepSize*24+stepSize/2);	
			}
			for (int i = 0; i < 24; i++) {
				line(kk*colSize, bottom - i*stepSize, kk*colSize + 220, bottom - i * stepSize);
				text(i, colSize * 7 + 15, bottom - i*stepSize + 4);
			}
			pushMatrix();
			fill(0);
			text("0",kk*colSize,bottom+15);
			text("10 km",kk*colSize + 100 + 10,bottom+15);
			fill(255,0,0);
			text("20 km",kk*colSize + 100 + 10,bottom+30);
			popMatrix();
		}
		while(end<=168){
		
			

			
		// draw the curves
		
		pushMatrix();
			int j = 0;
			int jj=0;
			stroke(0);
			noFill();
//			println("aussen: "+end);

			// draw median line
			stroke(0);
			beginShape();
			if(start==0){
				vertex(k * colSize + (mov_size[start] * 10),bottom - j*stepSize);
			}else{
				vertex(k * colSize + (mov_size[start-1] * 10),bottom - j*stepSize);
			}
			for (int i = start; i < end; i++, j++) {
				float x = k * colSize + (mov_size[i] * 10);
				float y = bottom - j*stepSize - stepSize/2;
				vertex(x,y);
			}
			endShape();

			// draw max line
			stroke(255,0,0);
			beginShape();
			if(start==0){
				vertex(k * colSize + (mov_size_max[start] * 2.5f),bottom - jj*stepSize);
			}else{
				vertex(k * colSize + (mov_size_max[start-1] * 2.5f),bottom - jj*stepSize);
			}
			for (int i = start; i < end; i++, jj++) {
				float x = k * colSize + (mov_size_max[i] * 2.5f);
				float y = bottom - jj*stepSize - stepSize/2;
				vertex(x,y);
			}
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
		exit();
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

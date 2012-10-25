package analyse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import processing.pdf.*;
import com.csvreader.CsvReader;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PShape;

public class KloutUser extends PApplet{
	
	HashMap<String,Integer> kloutuser = new HashMap<String, Integer>();
	String city = "Berlin";
	
public void setup(){
	background(255);
//	size(800,800, PDF, "top_words.pdf");
	size(1200,800);
	smooth();
	PFont font = createFont("Axel-Bold",50);
	textFont(font);
	PShape logo = loadShape("data/klout/logo.svg");
//	shape(logo);
	try {
		CsvReader csvData = new CsvReader("data/klout/"+city+".csv",',',Charset.forName("UTF-8"));
		csvData.readHeaders();
		translate(10,10);
		fill(0);
		int xmove = 100;
		int xCount = 0;
		fill(255);
		while (csvData.readRecord())
		{
			
			int score = Integer.parseInt(csvData.get("score")); 
			logo.scale(score/100f);
			logo.width = 20000;
			shape(logo,10,xmove*xCount);
			textSize(score/2);
			text(csvData.get("name"),10,xmove*xCount);
//			for (Iterator<String> iterator = words.keySet().iterator(); iterator.hasNext();) {
//				String word =  iterator.next();
//			}
			xCount++;
		}
		csvData.close();
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

//	 exit();
	
}

public void draw(){
	
	
}
public static void main(String args[])
{
	//PApplet.main(new String[] { "--present", mailgod.MailGod.class.getName()});
	PApplet.main(new String[] {  analyse.KloutUser.class.getName()});
}
}



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

public class FoursquareCategorys extends PApplet{
	
	HashMap<String,CityMass> cities = new HashMap<String, CityMass>();
	String city = "berlin";
	
public void setup(){
	background(255);
	size(4020,2970, PDF, "fsq_category_"+city+".pdf");
//	size(800,800);
	smooth();
	println(PFont.list());
	PFont font = createFont("Axel",50);
	textFont(font);
	translate(160,180);
	try {
		CsvReader csvData = new CsvReader("data/fsq_time/fsq_timecount_60_"+city+".csv",',',Charset.forName("UTF-8"));
		csvData.readHeaders();
		int hCount = csvData.getHeaderCount();
		int xmove = 10, ymove = 10;
		int xspace = 300, yspace = 90;
		int yCount = 1;
		translate(10,10);
		fill(0);
		while (csvData.readRecord())
		{
			
			HashMap<String, Integer> words = splitWords(csvData.get("Categories"));
			for (Iterator<String> iterator = words.keySet().iterator(); iterator.hasNext();) {
				String word =  iterator.next();
				int fSize = (int)(sqrt(words.get(word)*10)+7);
				println(fSize);
//				if(fSize<6){
//					fSize=6;
//				}
				textSize(fSize);
				text(word,xmove,ymove);
			}
			if(yCount<24){
				ymove+=yspace;
			}else{
				ymove = 10;
				yCount=0;
				xmove+=xspace;
			}
			yCount++;
		}
		csvData.close();
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

//	CityMass city = cities.get("NewYork");
	 exit();
	
}

public void draw(){
	
	
}

private HashMap<String, Integer> splitWords(String top_string){
	HashMap<String, Integer> topWords = new HashMap<String, Integer>();
	if(top_string.length()>0){
	String[] w1 = top_string.split("_");
	if(w1.length>0){
		for (int i = 0; i < w1.length; i++) {
			String[] w2 = w1[i].split("=");
			topWords.put(w2[0], Integer.parseInt(w2[1]));
		}
	}else{
		String[] w2 = top_string.split("=");
		topWords.put(w2[0], Integer.parseInt(w2[1]));
	}
	}
	return topWords;
}


public static void main(String args[])
{
	//PApplet.main(new String[] { "--present", mailgod.MailGod.class.getName()});
	PApplet.main(new String[] {  analyse.FoursquareCategorys.class.getName()});
}
}



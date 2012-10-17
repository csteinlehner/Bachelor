package analyse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import processing.pdf.*;
import com.csvreader.CsvReader;

import processing.core.PApplet;

public class MassQuads extends PApplet{
	
	HashMap<String,CityMass> cities = new HashMap<String, CityMass>();
	
public void setup(){
	background(255);
	size(800,800, PDF, "mass_quads.pdf");
	smooth();
	try {
		CsvReader csvData = new CsvReader("data/citytweets_table.csv",';',Charset.forName("UTF-8"));
		csvData.readHeaders();
		int hCount = csvData.getHeaderCount();
		
		while (csvData.readRecord())
		{
			cities.put(csvData.get("City"), new CityMass(csvData.get("City"), Integer.parseInt(csvData.get("tweets")), Integer.parseInt(csvData.get("deleted")), Integer.parseInt(csvData.get("fsq")), Integer.parseInt(csvData.get("insta"))));
//			tweetCount.put();
		}
		csvData.close();
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	translate(height/2,width/2);
	rotate(-HALF_PI/2);
	int c = 1;
	for (Iterator<CityMass> iterator = cities.values().iterator(); iterator.hasNext();) {
		CityMass city= iterator.next();
		fill(0);
		pushMatrix();
		translate(0,-50);
		text(city.city);
		popMatrix();
		noStroke();
		int bound = 10;
		rect(bound,bound,sqrt(city.all),sqrt(city.all));
		fill(100);
		rect(bound,bound,sqrt(city.fsq),sqrt(city.fsq));
		fill(200);
		rect(bound+sqrt(city.all)-sqrt(city.insta),bound,sqrt(city.insta),sqrt(city.insta));
		fill(255);
		rect(bound+sqrt(city.all)-sqrt(city.deleted),bound+sqrt(city.all)-sqrt(city.deleted),sqrt(city.deleted),sqrt(city.deleted));
		translate(500*c,0);
		c++;
	}
//	CityMass city = cities.get("NewYork");
	 exit();
	
}

public void draw(){
	
	
}
public static void main(String args[])
{
	//PApplet.main(new String[] { "--present", mailgod.MailGod.class.getName()});
	PApplet.main(new String[] {  analyse.MassQuads.class.getName()});
}
}



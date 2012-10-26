package tweettoner;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.crypto.Data;

import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import processing.core.PFont;


public class TweetCollectedMass extends PApplet  {
	
	private static final String CITY = "rosenheim";
	private static final float pointF = 2.5f;
	
	private static final int MODE_WORD = 1;
	private static final int MODE_SENTIMENT = 2;
	private static final int MODE_USER = 3;
	
	private static int MODE = MODE_SENTIMENT;
	
	
	public static final String JDBC_CONN_STRING_MAC = "jdbc:sqlite:data/toner"+CITY+".mbtiles";
	public static final int sentimentChunks = 3;
	public static final int maxSentiment = 15;
	private static PFont font;
	
	// positve colors from dark to light
	private static final String[] posVals = {"0xFF73020E","0xFF951F1D","0xFFB33C31","0xFFCD5B48","0xFFE27C64"};
											
	// negative colors from darl to light
	private static final String[] negVals = {"0xFF0A2A73","0xFF314398","0xFF575DB6","0xFF7F7ACB", "0xFFA699D7"};
	
	
	Map map;
	
	private static final HashMap<String, Location> MIDPOINTS = new HashMap<String, Location>();
	static{
		MIDPOINTS.put("berlin",new Location(52.516071f,13.376980f));
		MIDPOINTS.put("rosenheim", new Location(47.856522f,12.128750f));
		MIDPOINTS.put("london", new Location(51.506321f,-0.127140f));
		MIDPOINTS.put("potsdam", new Location(52.399349f,13.047930f));
		MIDPOINTS.put("munchen", new Location(48.136410f,11.577530f));
		MIDPOINTS.put("newyork", new Location(40.714550f,-74.007118f));
		MIDPOINTS.put("sanfrancisco", new Location(37.779598f,-122.419998f));
		MIDPOINTS.put("cupertino", new Location(37.318840f,-122.029243f));
		MIDPOINTS.put("menlopark", new Location(37.454689f, -122.177902f));
	}
	String csvPath = "data/col_"+CITY+".csv";
	
	private Vector<DataPoint> dataPoints = new Vector<DataPoint>();
	
	

	public void setup() {
		size(3000,3000);
		smooth();
		font = createFont("Axel-Bold", 14);
		textFont(font);
		map = new Map(this, 0, 0, width, height, new MBTilesMapProvider(JDBC_CONN_STRING_MAC));
		MapUtils.createDefaultEventDispatcher(this, map);
		map.setZoomRange(8, 16);
		map.zoom(9);
		System.out.println(map.getZoom());
		map.setTweening(false);
		map.panTo(MIDPOINTS.get(CITY));
		
		String lines[] = loadStrings(csvPath);
		for (int i = 1; i < lines.length; i++) {
			String chars[] = PApplet.split(lines[i],",");
			dataPoints.add(new DataPoint(Float.parseFloat(chars[0]), Float.parseFloat(chars[1]), Float.parseFloat(chars[2]),chars[3],chars[4], chars[5], Float.parseFloat(chars[6]), Float.parseFloat(chars[7])));
		}
		Float maxVal = 0f;
		Float minVal = 0f;		
		for(Iterator<DataPoint> it = dataPoints.iterator(); it.hasNext();){
			DataPoint dp = it.next();
			Float tVal = dp.getSentimentAverage();
			if(tVal>maxVal){
				maxVal = tVal;
			}else if(tVal<minVal){
				minVal = tVal;
			}
		}
		println("max: "+maxVal);
		println("min: "+minVal);
		
		/* output all top Words*/
//		for (Iterator<DataPoint> it = dataPoints.iterator(); it.hasNext();) {
//			DataPoint dp = it.next();
//			if(dp.hasWords()){
//				String firstKey = dp.getWords().keySet().iterator().next();
//				Integer firstVal = dp.getWords().values().iterator().next();
//				if(firstVal>1){
//					println(firstKey+" "+firstVal);
//				}
//			}
//		}
	}

	public void draw() {
		map.draw();
		textAlign(CENTER);
		for (Iterator<DataPoint> it = dataPoints.iterator(); it.hasNext();) {
			DataPoint dp = it.next();
			float xy[] = map.getScreenPositionFromLocation(dp);
			
			if(MODE==MODE_SENTIMENT){
				//fill(getColorForSentiment(dp.getSentimentAverage()),200);
				fill(150,150,150,150);
//				stroke(255,100);
				noStroke();
				strokeWeight(2);
				ellipse(xy[0], xy[1], sqrt(dp.getSize()*pointF), sqrt(dp.getSize()*pointF));
			}
			
			if(MODE==MODE_WORD){
				fill(255,0,0);
				noStroke();
				if(dp.hasWords()){
					String firstKey = dp.getWords().keySet().iterator().next();
					Integer firstVal = dp.getWords().values().iterator().next();
					if(firstVal>1){
						text(firstKey,xy[0],xy[1]);
					}
				}
			}
			if(MODE==MODE_USER){
				fill(255,0,0);
				noStroke();
				if(dp.hasUsers()){
					Iterator<String> users_keys = dp.getUsers().keySet().iterator();
					Iterator<Integer> users_vals = dp.getUsers().values().iterator();
					String firstKey = users_keys.next();
					Integer firstVal = users_vals.next();
					if(firstVal>1){
						text(firstKey+" / "+firstVal,xy[0],xy[1]);
					}
					if(users_keys.hasNext() && users_vals.hasNext()){
						String secondKey = users_keys.next();
						Integer secondVal = users_vals.next();
						if(secondVal>1){
							text(secondKey+" / "+secondVal,xy[0],xy[1]+20);
						}
					}
				}
			}
			// color palette
//			for(int i=1;i<=15;i++){
//				fill(getColorForSentiment((float)i),200);
//				int a = 20;
//				rect(a*i,0,a,a);
//				fill(getColorForSentiment((float)(i*-1)),200);
//				rect(a*i,a,a,a);
//			}
		}
	}
	public void keyPressed(){
		if(key=='w'){
			MODE = MODE_WORD;
		}
		if(key=='s'){
			MODE = MODE_SENTIMENT;
		}
		if(key=='u'){
			MODE=MODE_USER;
		}
	}
	private int convertColor(String col){
		return Integer.parseInt(col.substring(4), 16) | 0xFF000000;
	}
	private int getColorForSentiment(Float sentiment){
		int col = 150;
		if(sentiment>0){
			sentiment = (sentiment>maxSentiment) ? maxSentiment : sentiment;
			col = convertColor(posVals[(int)((sentiment-1)/sentimentChunks)]);
		}else if(sentiment<0){
			sentiment = (sentiment<(maxSentiment*-1)) ? (maxSentiment*-1) : sentiment;
			col = convertColor(negVals[(int)((Math.abs(sentiment)-1)/sentimentChunks)]);
		}
		return col;
	}
	public static void main(String args[])
	{
		//PApplet.main(new String[] { "--present", mailgod.MailGod.class.getName()});
		PApplet.main(new String[] {  tweettoner.TweetCollectedMass.class.getName()});
	}
}

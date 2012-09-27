package foursquare;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import com.csvreader.CsvReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import processing.core.PFont;

import geomerative.*;


public class FoursquareVenues extends PApplet  {
	
	private static final String CITY = "cupertino";
	
	public static final String JDBC_CONN_STRING_MAC = "jdbc:sqlite:data/toner"+CITY+".mbtiles";
	
	private PFont font;
	
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
	String csvPath = "data/fsq_"+CITY+".csv";
	
	private Vector<FoursquarePoint> fsqPoints = new Vector<FoursquarePoint>();
	
	float pointF = 1f;
	
	

	public void setup() {
		size(1200,900);
		smooth();
		RG.init(this);
		font = createFont("Axel-Bold",20);
		textAlign(PFont.CENTER);
		map = new Map(this, 0, 0, width, height, new MBTilesMapProvider(JDBC_CONN_STRING_MAC));
		MapUtils.createDefaultEventDispatcher(this, map);
		map.setZoomRange(10, 16);
		map.zoom(12);
		System.out.println(map.getZoom());
		map.setTweening(false);
		map.panTo(MIDPOINTS.get(CITY));
//		String lines[] = loadStrings(csvPath);
//		for (int i = 1; i < lines.length; i++) {
//			String chars[] = PApplet.split(lines[i],",");
//			println(chars[0]+","+chars[1]+","+chars[2]+","+chars[3]);
//			FoursquarePoints.add(new FoursquarePoint(Float.parseFloat(chars[0]), Float.parseFloat(chars[1]), chars[2], Float.parseFloat(chars[3])));
//		}	
		
try {
			
			CsvReader fsqData = new CsvReader(csvPath,',',Charset.forName("UTF-8"));
		
			fsqData.readHeaders();

			while (fsqData.readRecord())
			{
				fsqPoints .add(new FoursquarePoint(Float.parseFloat(fsqData.get("latitude")), Float.parseFloat(fsqData.get("longitude")), fsqData.get("fsq_venueName"), Float.parseFloat(fsqData.get("fsq_venueCount"))));
			}
	
			fsqData.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void draw() {
		background(0);
		map.draw();
		textFont(font);
		fill(255,0,0);
		for (Iterator<FoursquarePoint> it = fsqPoints.iterator(); it.hasNext();) {
			FoursquarePoint dp = it.next();
			float xy[] = map.getScreenPositionFromLocation(dp);
			fill(255,0,0);
			textSize(12+dp.getSize()/2);
			text(dp.getVenue(),xy[0],xy[1]);
		}
				}
	public static void main(String args[])
	{
		//PApplet.main(new String[] { "--present", mailgod.MailGod.class.getName()});
		PApplet.main(new String[] {  foursquare.FoursquareVenues.class.getName()});
	}
}

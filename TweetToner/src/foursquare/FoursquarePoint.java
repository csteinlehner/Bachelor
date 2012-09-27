package foursquare;

import java.util.TreeMap;


import de.fhpotsdam.unfolding.geo.Location;

public class FoursquarePoint extends Location {

	private float size;
	private String venue;
	
	public FoursquarePoint(float lat, float lon, String venue, float size) {
		super(lat, lon);
		this.size = size;
		this.venue = venue;
//		System.out.println(lat+","+lon+","+venue+","+size);
	}
		
	public float getSize() {return size;}
	public String getVenue(){return venue;}
}
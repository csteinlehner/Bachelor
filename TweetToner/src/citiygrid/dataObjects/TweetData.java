package citiygrid.dataObjects;

import java.util.Vector;

public class TweetData {
	public int minute, day, hour;
	public String user, tweet;
	
	public TweetData(int day, int minute, int hour, String user, String tweet) {
		this.minute = minute;
		this.day = day;
		this.hour = hour;
		this.user = user;
		this.tweet = tweet;
	}
}

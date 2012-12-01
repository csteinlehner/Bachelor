package citygrid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Vector;

import citiygrid.dataObjects.TweetData;

import com.csvreader.CsvReader;
import com.google.common.collect.TreeBasedTable;

public class TweetsDrawer {
	public Vector<TweetData> tweets = new Vector<TweetData>();
	
	public void init(){
		try {
			CsvReader data = new CsvReader("data/top_tweets/top_tweets_"+CityGrid.CITY+".csv",',',Charset.forName("UTF-8"));
			data.readHeaders();
			while(data.readRecord()){
				int day = Integer.parseInt(data.get("day"));
				int minute = Integer.parseInt(data.get("minute"));
				int hour = Integer.parseInt(data.get("hour"));
				String user = data.get("user");
				String tweet = data.get("text");
				//tweets.put(day,minute,new TweetData(day, minute, hour, user, tweet));
				tweets.add(new TweetData(day, minute, hour, user, tweet));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

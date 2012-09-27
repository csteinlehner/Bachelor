package tweettoner;

import java.util.LinkedHashMap;



import de.fhpotsdam.unfolding.geo.Location;

public class DataPoint extends Location {

	private float size;
	private LinkedHashMap<String, Integer> lang;
	private LinkedHashMap<String, Integer> users;
	private LinkedHashMap<String, Integer> words;
	
	private Boolean hasLang = false;
	private Boolean hasUsers = false;
	private Boolean hasWords = false;
	private float sentiment_average;
	private float sentiment_mean;
	
	public DataPoint(float lat, float lon, float size, String lang, String users, String words, float sentiment_average, float sentiment_mean) {
		super(lat, lon);
		this.size = size;
		if(!lang.equalsIgnoreCase("\"\"") && !lang.equals("")){
			this.lang = csvtring_to_hashmap(lang);
			hasLang=true;
		}
		if(!users.equalsIgnoreCase("\"\"") && !users.equals("")){
			this.users = csvtring_to_hashmap(users);
			hasUsers=true;
		}
		if(!words.equalsIgnoreCase("\"\"") && !words.equals("")){
			this.words = csvtring_to_hashmap(words);
			hasWords=true;
		}
		this.sentiment_average = sentiment_average;
		this.sentiment_mean = sentiment_mean;
	}
	private LinkedHashMap<String,Integer> csvtring_to_hashmap(String s){
		LinkedHashMap<String, Integer> hm = new LinkedHashMap<String, Integer>();
		String[] pairs = s.split(" ");
		for (int i = 0; i < pairs.length; i++) {
			String[] pair = pairs[i].split("=");
			hm.put(pair[0], Integer.parseInt(pair[1]));
		}
		return hm;
	}
	
	
	public float getSize() {
		return size;
	}
	public Boolean hasLang(){ return hasLang;}
	public Boolean hasUsers(){ return hasUsers;}
	public Boolean hasWords(){ return hasWords;}
	public LinkedHashMap<String,Integer> getLang(){return lang;}
	public LinkedHashMap<String,Integer> getUsers(){return users;}
	public LinkedHashMap<String,Integer> getWords(){return words;}
	public Float getSentimentAverage(){return sentiment_average;}
	public Float getSentimentMean(){return sentiment_mean;}

}
package citygrid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import citiygrid.dataObjects.TopWordData;

import com.csvreader.CsvReader;
import com.google.common.collect.TreeBasedTable;

public class StreetNameDrawer {
	public TreeBasedTable<Integer, Integer, TopWordData> topWords = TreeBasedTable.create();
	
	private static Vector<String> streetSuffixes = new Vector<String>();
	private static Random random = new Random();
	
	public void init(){
		fillStreetSuffixes();
		try {
			CsvReader streetData = new CsvReader("data/top_words_timecoded/top_words_60_"+CityGrid.CITY+".csv",',',Charset.forName("UTF-8"));
			streetData.readHeaders();
			while(streetData.readRecord()){
				int day = Integer.parseInt(streetData.get("day"));
				int minute = Integer.parseInt(streetData.get("minute"));
				int hour = Integer.parseInt(streetData.get("hour"));
				String wordString = streetData.get("top_words");
				topWords.put(day,minute,new TopWordData(day, minute, hour));
				if(!wordString.isEmpty()){
					Vector<String> words = new Vector<String>();
					String[] splitted = wordString.split("_");
					int size = 0;
					for (int i = 0; i < splitted.length; i++) {
						String[] tSplitted = splitted[i].split("=");
						words.add(tSplitted[0]);
						size = Integer.parseInt(tSplitted[1]);
					}
					topWords.get(day, minute).size = size;
					topWords.get(day, minute).setWords(words);
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private String getFirstWord(int day, int minute){
		return topWords.get(day,minute).getFirstWord();
	}
	
	private int getStreetNameSize(int day, int minute){
		return topWords.get(day, minute).size;
	}
	
	public String getStreetName(int day, int minute){
		String firstWord = getFirstWord(day, minute);
		if(firstWord.length()>1){
			firstWord = firstWord.substring(0,1).toUpperCase() + firstWord.substring(1);
		}
		if(firstWord!=""){
		return firstWord+" "+streetSuffixes.get(random.nextInt(streetSuffixes.size()));
		}else{
			return "";
		}
	}
	
	private void fillStreetSuffixes(){
		streetSuffixes.add("Highway");
		streetSuffixes.add("Freeway");
		streetSuffixes.add("Avenue");
		streetSuffixes.add("Boulevard");
		streetSuffixes.add("Road");
		streetSuffixes.add("Street");
		streetSuffixes.add("Alley");
		streetSuffixes.add("Lane");
		streetSuffixes.add("Way");
	}
}

package citiygrid.dataObjects;

import java.util.Vector;

public class TopWordData {
	public int minute, day, hour;
	public Vector<String> words;
	public int size;
	private  Boolean hasWords = false;
	
	public TopWordData(int day, int minute, int hour) {
		this.minute = minute;
		this.day = day;
		this.hour = hour;
	}
	
	public void setWords(Vector<String> words){
		this.words = words;
		hasWords = true;
	}
	
	public String getFirstWord(){
		if(hasWords){
			return words.firstElement();
		}else{
			return "";
		}
	}
}

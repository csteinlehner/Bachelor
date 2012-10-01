package color;

import java.util.Comparator;

import com.google.common.collect.TreeMultiset;
public class SingleData {
	Float i_count;
	Integer red, green, blue;
	Boolean hasColor = false;
	
	public SingleData(String date, Float i_count, String red, String green, String blue){
		this.i_count = i_count;
		try{
		this.red = Integer.parseInt(red);
		this.green = Integer.parseInt(green);
		this.blue = Integer.parseInt(blue);
		hasColor = true;
		}catch(NumberFormatException nfe){
			hasColor = false;
		}
	}
}
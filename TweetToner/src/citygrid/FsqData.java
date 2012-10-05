package citygrid;

import java.util.HashMap;

public class FsqData {
	int minute, day;
	int t_count, t_count_added, f_count;
	HashMap<String, Integer> categories;
	Boolean hasCategories = false;
	public FsqData(int day, int minute, int t_count, int f_count){
		this.day = day;
		this.minute = minute;
		this.t_count = t_count;
		this.f_count = f_count;
	}
	public FsqData(int day, int minute, int t_count, int f_count, HashMap<String, Integer> categories){
		this.day = day;
		this.minute = minute;
		this.t_count = t_count;
		this.f_count = f_count;
		this.categories = categories;
		hasCategories = true;
	}
}

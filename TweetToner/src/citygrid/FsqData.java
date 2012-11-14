package citygrid;

import java.util.HashMap;

public class FsqData {
	int minute, day, hour;
	int t_count, t_count_added, f_count;
	int categoryParentsParts = 0;
	HashMap<String, Integer> categories;
	HashMap<String, Integer> categoryParents;
	Boolean hasCategories = false;
	Boolean hasCategoryParents = false;
	public FsqData(int day, int minute, int hour, int t_count, int f_count){
		this.day = day;
		this.minute = minute;
		this.hour = hour;
		this.t_count = t_count;
		this.f_count = f_count;
	}
	public void setCategories(HashMap<String, Integer> categories) {
		this.categories = categories;
		hasCategories = true;
	}
	public void setCategoryParents(HashMap<String, Integer> categoryParents) {
		this.categoryParents = categoryParents;
		hasCategoryParents = true;
		for( int val : categoryParents.values()){
			categoryParentsParts+=val;
		}
	}
	
	
}

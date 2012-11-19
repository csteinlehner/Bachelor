package citygrid;

import java.util.HashMap;

public class FsqData {
	int minute, day, hour;
	int t_count, t_count_added, f_count;
	float categoryParentsParts = 0;
	float categoryParts = 0;
	HashMap<String, Integer> categories;
	HashMap<String, Integer> categoryParents;
	HashMap<String, Float> categoryParentsPercent = new HashMap<String, Float>();
	HashMap<String, Float> categoryPercent = new HashMap<String, Float>();
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
		for( int val : categories.values()){
			categoryParts+=Math.sqrt((float)val);
		}
		for(String key : categories.keySet()){
			float p = (float)Math.sqrt(categories.get(key))/(float)categoryParts;
			categoryPercent.put(key,p);
		}
//		System.out.println(categories);
	}
	public void setCategoryParents(HashMap<String, Integer> categoryParents) {
		this.categoryParents = categoryParents;
		hasCategoryParents = true;
		for( int val : categoryParents.values()){
			categoryParentsParts+=Math.sqrt((float)val);
		}
		for(String key : categoryParents.keySet()){
			float p = (float)Math.sqrt(categoryParents.get(key))/(float)categoryParentsParts;
			categoryParentsPercent.put(key,p);
		}
	}
	
	
}

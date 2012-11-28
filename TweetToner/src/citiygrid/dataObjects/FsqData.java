package citiygrid.dataObjects;

import java.util.HashMap;

public class FsqData {
	public int minute, day, hour;
	public int t_count, t_count_added, f_count;
	public float categoryParentsParts = 0;
	public float categoryParts = 0;
	public HashMap<String, Integer> categories;
	public HashMap<String, Integer> categoryParents;
	public HashMap<String, Float> categoryParentsPercent = new HashMap<String, Float>();
	public HashMap<String, Float> categoryPercent = new HashMap<String, Float>();
	public HashMap<String, String> coords = new HashMap<String, String>();
	public Boolean hasCategories = false;
	public Boolean hasCategoryParents = false;
	public Boolean hasCoords = false;
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
	
	public void setCoords(HashMap<String, String> coords){
		this.coords = coords;
		hasCoords = true;
	}
	
}

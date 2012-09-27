package time;

import com.google.common.collect.TreeMultiset;
public class SingleData {
	Float t_count;
	Float fsq_count;
	TreeMultiset<String> categories = TreeMultiset.create();
	TreeMultiset<String> parentCategories = TreeMultiset.create();
	private Boolean hasCats = false;
	private Boolean hasParentCats = false;
	
	public SingleData(String date, Float t_count, Float fsq_count, String cat_parents, String cat){
		this.t_count = t_count;
		this.fsq_count = fsq_count;
		if(cat_parents.length()>0){
			stringToMultiset(parentCategories, cat_parents);
			hasParentCats = true;
		}
		if(cat.length()>0){
			stringToMultiset(categories, cat);
			hasCats = true;
		}
	}
	private void stringToMultiset(TreeMultiset<String> mSet, String str){
		String[] entries = str.split("_");
		if(entries.length>0){
		for (int i = 0; i < entries.length; i++) {
			String[] entry = entries[i].split("=");
//			System.out.println(entry[0]);
			mSet.add(entry[0],Integer.parseInt(entry[1]));
		}
		}
	}
}

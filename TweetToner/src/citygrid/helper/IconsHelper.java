package citygrid.helper;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IconsHelper {
	public static final HashMap<String, String> ICON_PATHS = creatIconPaths();
	private static Pattern pattern = Pattern.compile("\\s+|\\/|&|\\(|\\)|'");
	
	public static String convertCatNameToFileName(String catName){
		Matcher matcher = pattern.matcher(catName);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
		     matcher.appendReplacement(sb, "");
		    // s now contains "BAR"
		}
		matcher.appendTail(sb);
		//System.out.println(this.getClass().getName()+": "+sb.toString());
		return sb.toString();
	}
	
	private static HashMap<String, String> creatIconPaths(){
		HashMap<String, String> iconPaths = new HashMap<String, String>();
		File file = new File("data/fsq_icons");
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			String[] splitted = files[i].getPath().split("/"); 
			String filename = splitted[splitted.length-1].split("\\.")[0];
			iconPaths.put(filename, files[i].getPath());
	    }
		return iconPaths;
	} 
	
}

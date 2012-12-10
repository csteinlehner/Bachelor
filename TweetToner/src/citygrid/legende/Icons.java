package citygrid.legende;

import java.util.HashMap;
import java.util.Map.Entry;

import citygrid.helper.ColorsHelper;
import citygrid.helper.FsqNameHelper;
import citygrid.helper.IconsHelper;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;

public class Icons extends PApplet{
	
	private PFont font;
	private int heightSpace = 100;
	private Boolean export = false; 
	
	public void setup(){
		if(export){
			size(5000,5000,PDF,"legende/icons.pdf");
		}else{
			size(500,500);
		}
		font = createFont("data/fonts/Axel-Regular.ttf", 12);
		
		background(255);
		smooth();
		textFont(font);
		fill(0);
		noStroke();
		translate(20,20);
		imageMode(CENTER);
		HashMap<String, String> cat_parents = new HashMap<String, String>();
		HashMap<String, String> t_cat_parents = (HashMap<String, String>) FsqNameHelper.CATEGORY_PARENTS;
		 for (Entry<String, String> entry : t_cat_parents.entrySet()){
			 cat_parents.put(IconsHelper.convertCatNameToFileName(entry.getKey()), entry.getValue());
		 }
		HashMap<String, String> icons = IconsHelper.ICON_PATHS;
		icons.remove("");
		int d = 0;
		int dd = 1;
		pushMatrix();
		 for (Entry<String, String> entry : icons.entrySet())
		 {
			 String key = trim(entry.getKey().replaceAll("([A-Z]){1,1}", " $1"));
			 
			 text(key,300*dd,0);
			 PImage icon = loadImage(entry.getValue(), ARGB);
			 if(icon.width>0){
				 PGraphics iconG = createGraphics(icon.width, icon.height, JAVA2D);
				 iconG.beginDraw();
				 iconG.background(255);
				 iconG.tint((ColorsHelper.ICON_COLORS.get(cat_parents.get(entry.getKey()))).toARGB());
				 iconG.image(icon,0,0);
				 iconG.endDraw();
//				 PImage iconD = createImage(icon.width, icon.height, ARGB);
				 PImage iconGI = createImage(icon.width, icon.height, ARGB);
				 iconGI.set(0, 0, iconG);
				 iconGI.save("legende/export/"+entry.getKey()+".png");
				 //iconD.set(0, 0, iconG.get());
				 image(iconGI,300*dd+200,0);
			 }
			 translate(0,heightSpace);
		     //System.out.println( + "/" + entry.getValue());
			 d++;
			 if(d>20){
				 dd++;
				 d=0;
				 popMatrix();
				 pushMatrix();
			 }
		 }
		exit();
	}
	
	public void draw(){
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main(new String[] {  citygrid.legende.Icons.class.getName()});
	}

}

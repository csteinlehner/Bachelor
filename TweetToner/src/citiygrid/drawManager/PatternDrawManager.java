package citiygrid.drawManager;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gicentre.handy.HandyRenderer;

import citygrid.CityGrid;
import citygrid.FsqNameHelper;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import toxi.color.TColor;

public class PatternDrawManager implements DrawManager{
	Pattern pattern = Pattern.compile("\\s+|\\/|&|\\(|\\)|'");
	
	HashMap<String, TColor> iconColors = new HashMap<String, TColor>();
	HashMap<String, String> categoryParents = new HashMap<String, String>();
	
	File file = new File("data/fsq_icons");
	File[] files = file.listFiles();
	HashMap<String, String> iconPaths = new HashMap<String, String>();
	public PatternDrawManager(){
		for (int i = 0; i < files.length; i++) {
			String[] splitted = files[i].getPath().split("/"); 
			String filename = splitted[splitted.length-1].split("\\.")[0];
			iconPaths.put(filename, files[i].getPath());
	    }
		categoryParents = FsqNameHelper.CATEGORY_PARENTS;
		createColors();
	}
	
	public PImage createPattern(String catName, int sizeX, int sizeY,  PVector[] maskShape, int tileSize){
		String name = convertCatNameToFileName(catName);
		String path = iconPaths.get(name);
		PImage tile = CityGrid.p5.loadImage(path);
		tile.resize(tileSize, tileSize);
		int tilesX = (int)Math.ceil(sizeX/(float)tile.width);
		int tilesY = (int)Math.ceil(sizeY/(float)tile.height);
		PGraphics mask = CityGrid.p5.createGraphics(tilesX*tile.width, tilesY*tile.height, PApplet.JAVA2D);
		mask.beginDraw();
		mask.background(0);
		mask.fill(255);
		mask.beginShape();
		for (int i = 0; i < maskShape.length; i++) {
			mask.vertex(maskShape[i].x,maskShape[i].y);
		}
		mask.endShape();
		mask.endDraw();
		PImage piMask = mask.get(0, 0, mask.width, mask.height);
		
		PGraphics pattern = CityGrid.p5.createGraphics(tilesX*tile.width, tilesY*tile.height, PApplet.JAVA2D);
		pattern.beginDraw();
		HandyRenderer h = CityGrid.SKETCH_RENDER;
		
		//pattern.background(255,255);
		TColor c = new TColor(iconColors.get(categoryParents.get(catName)));
		pattern.fill(c.toARGB());
		pattern.noStroke();
		//pattern.background(c.toARGB());
		pattern.smooth();
		h.setGraphics(pattern);
		h.beginShape();
		for (int i = 0; i < maskShape.length; i++) {
			h.vertex(maskShape[i].x,maskShape[i].y);
		}
		h.endShape();
		//pattern.tint(c.getDarkened(0.3f).toARGB());
		for (int i = 0; i < tilesX; i++) {
			for (int j = 0; j < tilesY; j++) {
				pattern.image(tile,tile.width*i,tile.height*j);	
			}	
		}
//		pattern.stroke(0,0,255);
//		pattern.strokeWeight(2);
//		pattern.noFill();
//		pattern.beginShape();
//		for (int i = 0; i < maskShape.length; i++) {
//			pattern.vertex(maskShape[i].x,maskShape[i].y);
//		}
//		pattern.endShape();
		pattern.endDraw();
		PImage piPattern = pattern.get();
		piPattern.mask(piMask);
		return piPattern;
	}
	
	private String convertCatNameToFileName(String catName){
		Matcher matcher = pattern.matcher(catName);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
		     matcher.appendReplacement(sb, "");
		    // s now contains "BAR"
		}
		matcher.appendTail(sb);
		System.out.println(this.getClass().getName()+": "+sb.toString());
		return sb.toString();
	}
	
	private void createColors(){
//		TColor c_default = TColor.newRGBA(1,0,0,1);
		
		iconColors.put("Arts & Entertainment", TColor.newHex("F18B8E"));
		iconColors.put("College & University", TColor.newHex("E2B8D7"));
		iconColors.put("Food", TColor.newHex("FFF7B2"));
		iconColors.put("Nightlife Spot", TColor.newHex("505297"));
		iconColors.put("Outdoors & Recreation", TColor.newHex("4DB05B"));
		iconColors.put("Professional & Other Places", TColor.newHex("83D0F5"));
		iconColors.put("Residence", TColor.newHex("BFD2BE"));
		iconColors.put("Shop & Service", TColor.newHex("83D0F5"));
		iconColors.put("Travel & Transport", TColor.newHex("F5A057"));

		
		//// http://tristen.ca/hcl-picker/#/hcl/9/1.06/F2A18A/F1A37D
//		iconColors.put("Arts & Entertainment", TColor.newHex("F2A18A"));
//		iconColors.put("College & University", TColor.newHex("EBA1AC"));
//		iconColors.put("Food", TColor.newHex("CBACC6"));
//		iconColors.put("Nightlife Spot", TColor.newHex("9FB9CA"));
//		iconColors.put("Outdoors & Recreation", TColor.newHex("82C1B6"));
//		iconColors.put("Professional & Other Places", TColor.newHex("8CC292"));
//		iconColors.put("Residence", TColor.newHex("AFBC73"));
//		iconColors.put("Shop & Service", TColor.newHex("D6B06A"));
//		iconColors.put("Travel & Transport", TColor.newHex("F1A37D"));
		
		//// http://tristen.ca/hcl-picker/#/hcl/9/0.95/F6816F/F08760
//		iconColors.put("Arts & Entertainment", TColor.newHex("F6816F"));
//		iconColors.put("College & University", TColor.newHex("ED82A5"));
//		iconColors.put("Food", TColor.newHex("BA96CD"));
//		iconColors.put("Nightlife Spot", TColor.newHex("69AAD3"));
//		iconColors.put("Outdoors & Recreation", TColor.newHex("1EB5B2"));
//		iconColors.put("Professional & Other Places", TColor.newHex("51B67C"));
//		iconColors.put("Residence", TColor.newHex("92AD4C"));
//		iconColors.put("Shop & Service", TColor.newHex("CA9C3F"));
//		iconColors.put("Travel & Transport", TColor.newHex("F08760"));
		
		
		//// http://tristen.ca/hcl-picker/#/hcl/9/0.71/BE6354/B96748
//		iconColors.put("Arts & Entertainment", TColor.newHex("BE6354"));
//		iconColors.put("College & University", TColor.newHex("B7637E"));
//		iconColors.put("Food", TColor.newHex("8E739E"));
//		iconColors.put("Nightlife Spot", TColor.newHex("4F83A3"));
//		iconColors.put("Outdoors & Recreation", TColor.newHex("158B89"));
//		iconColors.put("Professional & Other Places", TColor.newHex("3D8C5F"));
//		iconColors.put("Residence", TColor.newHex("6F8539"));
//		iconColors.put("Shop & Service", TColor.newHex("9B772F"));
//		iconColors.put("Travel & Transport", TColor.newHex("B96748"));

		
//		iconColors.put("Arts & Entertainment", TColor.newHex("854237"));
//		iconColors.put("College & University", TColor.newHex("7F4156"));
//		iconColors.put("Food", TColor.newHex("614D6D"));
//		iconColors.put("Nightlife Spot", TColor.newHex("315870"));
//		iconColors.put("Outdoors & Recreation", TColor.newHex("335772"));
//		iconColors.put("Professional & Other Places", TColor.newHex("235D3D"));
//		iconColors.put("Residence", TColor.newHex("485821"));
//		iconColors.put("Shop & Service", TColor.newHex("684E1A"));
//		iconColors.put("Travel & Transport", TColor.newHex("7C412C"));
	}
	

}

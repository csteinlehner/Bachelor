package patternDrawer;

import java.io.File;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class PatternDrawManager {
	File file = new File("data/fsq_icons");
	File[] files = file.listFiles();
	HashMap<String, String> iconPaths = new HashMap<String, String>();
	public PatternDrawManager(){
		for (int i = 0; i < files.length; i++) {
			String[] splitted = files[i].getPath().split("/"); 
			String filename = splitted[splitted.length-1].split("\\.")[0];
			iconPaths.put(filename, files[i].getPath());
	    }
	}
	
	public PImage createPattern(String name, int sizeX, int sizeY, PVector[] maskShape){
		String path = iconPaths.get(name);
		PImage tile = PatternDrawTest.p5.loadImage(path);
		
		PGraphics mask = PatternDrawTest.p5.createGraphics(tile.width*sizeX, tile.height*sizeY, PApplet.JAVA2D);
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
		
		PGraphics pattern = PatternDrawTest.p5.createGraphics(tile.width*sizeX, tile.height*sizeY, PApplet.JAVA2D);
		pattern.beginDraw();
		pattern.background(255);
		pattern.smooth();
		pattern.tint(255,0,0,255);
		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				pattern.image(tile,tile.width*i,tile.height*j);	
			}	
		}
		pattern.endDraw();
		PImage piPattern = pattern.get();
		piPattern.mask(piMask);
		return piPattern;
	}
}

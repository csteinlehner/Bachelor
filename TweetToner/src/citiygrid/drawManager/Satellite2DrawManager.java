package citiygrid.drawManager;

import java.util.HashMap;

import citiygrid.dataObjects.FsqData;
import citygrid.CityGrid;
import citygrid.FsqNameHelper;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import toxi.color.TColor;

public class Satellite2DrawManager implements DrawManager{
	String satellitePicturePath = "nokia_img/"+CityGrid.CITY+"/";
	HashMap<String, String> satellitePictures = new HashMap<String, String>();
	HashMap<String, String> categoryParents = new HashMap<String, String>();
	HashMap<String, TColor> iconColors = new HashMap<String, TColor>();
	
	public Satellite2DrawManager() {
//		satellitePictures.put("Arts & Entertainment", satellitePicturePath+"art.png");
//		satellitePictures.put("College & University", satellitePicturePath+"college.png");
//		satellitePictures.put("Food", satellitePicturePath+"food.png");
//		satellitePictures.put("Nightlife Spot", satellitePicturePath+"nightlife.png");
//		satellitePictures.put("Outdoors & Recreation", satellitePicturePath+"outdoors.png");
//		satellitePictures.put("Professional & Other Places", satellitePicturePath+"professional.png");
//		satellitePictures.put("Residence", satellitePicturePath+"residence.png");
//		satellitePictures.put("Shop & Service", satellitePicturePath+"shop.png");
//		satellitePictures.put("Travel & Transport", satellitePicturePath+"travel.png");
		categoryParents = FsqNameHelper.CATEGORY_PARENTS;
	}
	
	public PImage createPattern(String catName, int sizeX, int sizeY,  PVector[] maskShape, int tileSize, FsqData entry){
		System.out.println(catName);
		String filename = "";
		if(CityGrid.SMALL){
			filename = entry.coords.get(catName).replace(" ", "_")+"_s.png";
		}else{
			filename = entry.coords.get(catName).replace(" ", "_")+".png";
		}
		String path = satellitePicturePath+filename;
		PImage tile = CityGrid.p5.loadImage(path);
		int maxSize = (sizeX > sizeY) ? sizeX : sizeY;
		tile.resize(maxSize, maxSize);
		PGraphics mask = CityGrid.p5.createGraphics(tile.width, tile.height, PApplet.JAVA2D);
		
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
		tile.mask(piMask);
		return tile;
	}
	
}

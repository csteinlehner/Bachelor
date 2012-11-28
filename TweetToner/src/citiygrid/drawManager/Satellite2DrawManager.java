package citiygrid.drawManager;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import citiygrid.dataObjects.FsqData;
import citygrid.CityGrid;
import citygrid.PVectorCalc;
import citygrid.helper.ColorsHelper;
import citygrid.helper.FsqNameHelper;
import citygrid.helper.IconsHelper;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import toxi.color.TColor;

public class Satellite2DrawManager implements DrawManager{
	private String satellitePicturePath = "nokia_img/"+CityGrid.CITY+"/";
	private HashMap<String, String> categoryParents = new HashMap<String, String>();
	
	
	public Satellite2DrawManager() {
		categoryParents = FsqNameHelper.CATEGORY_PARENTS;
	}
	
	public PImage createPattern(String catName, int sizeX, int sizeY,  PVector[] maskShape, int tileSize, FsqData entry){
		System.out.println(this.getClass().getName()+" : "+catName);
		String filename = "";
		if(CityGrid.SMALL){
			filename = entry.coords.get(catName).replace(" ", "_")+"_s.png";
		}else{
			filename = entry.coords.get(catName).replace(" ", "_")+".png";
		}
		String path = satellitePicturePath+filename;
		PImage satellitePic = CityGrid.p5.loadImage(path);
		int maxSize = (sizeX > sizeY) ? sizeX : sizeY;
		satellitePic.resize(maxSize, maxSize);
		
		PGraphics mask = CityGrid.p5.createGraphics(satellitePic.width, satellitePic.height, PApplet.JAVA2D);
		
		mask.beginDraw();
		mask.smooth();
		mask.background(0);
		mask.fill(255);
		mask.beginShape();
		for (int i = 0; i < maskShape.length; i++) {
			mask.vertex(maskShape[i].x,maskShape[i].y);
		}
		mask.endShape();
		mask.endDraw();
		//// draw Icon
		String name = IconsHelper.convertCatNameToFileName(catName);
		String iconPath = IconsHelper.ICON_PATHS.get(name);
		
		PImage piMask = CityGrid.p5.createImage(satellitePic.width, satellitePic.height, PApplet.ARGB);
		piMask.set(0, 0, mask);
		//satellitePic.mask(piMask);
		
		PGraphics tile = CityGrid.p5.createGraphics(satellitePic.width, satellitePic.height, PApplet.JAVA2D);
		tile.beginDraw();
		tile.smooth();
		tile.image(satellitePic,0,0);
		
		TColor c = new TColor(ColorsHelper.ICON_COLORS.get(FsqNameHelper.CATEGORY_PARENTS.get(catName)));
		PGraphics overlay = CityGrid.p5.createGraphics(satellitePic.width, satellitePic.height, PApplet.JAVA2D);
		overlay.beginDraw();
		overlay.smooth();
		overlay.fill(c.toARGB());
//		overlay.beginShape();
//		for (int i = 0; i < maskShape.length; i++) {
//			tile.vertex(maskShape[i].x,maskShape[i].y);
//		}
//		overlay.endShape();
		overlay.rect(0,0,overlay.width, overlay.height);
		overlay.endDraw();
		PImage overlayImg = CityGrid.p5.createImage(overlay.height,overlay.height, PApplet.ARGB);
		overlayImg.set(0,0, overlay);
		
		
		PImage icon= CityGrid.p5.loadImage(iconPath);
		int iconSize = (int)(CityGrid.ICON_SIZE_SKETCH/CityGrid.SIZE_FACTOR);
		
		icon.resize(iconSize, iconSize);
		PVector br = maskShape[1];
		PVector tl = maskShape[3];
		PVector mid = PVectorCalc.calcMid(br, tl);
		PGraphics overlayMask = CityGrid.p5.createGraphics(satellitePic.width, satellitePic.height, PApplet.JAVA2D);
		overlayMask.beginDraw();
		overlayMask.smooth();
		// overlay.background(0,150);
		overlayMask.fill(CityGrid.OVERLAY_TRANSPARENCY);
		overlayMask.rect(0,0,overlayMask.width, overlayMask.height);
		overlayMask.fill(0);
		overlayMask.ellipse(mid.x, mid.y, iconSize, iconSize);
		overlayMask.tint(255,CityGrid.ICON_TRANPARENCY);
		overlayMask.image(icon,mid.x-icon.width/2,mid.y-icon.height/2);
		overlayMask.endDraw();
		PImage overlayMaskImg = CityGrid.p5.createImage(overlayMask.width, overlayMask.height, PApplet.ARGB);
		overlayMaskImg.set(0, 0, overlayMask);
		overlayImg.mask(overlayMaskImg);
		//tile.tint(255,120);
		tile.image(overlayImg,0,0);
		tile.endDraw();
		//c.alpha=0.5f;
		//satellitePic.tint(c.toARGB());
		
		PImage tileImg = CityGrid.p5.createImage(tile.width, tile.height, PApplet.ARGB);
		tileImg.set(0, 0, tile);
		tileImg.mask(piMask);
		return tileImg;
	}

}

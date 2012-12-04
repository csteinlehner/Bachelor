package citiygrid.drawManager;


import citiygrid.dataObjects.FsqData;
import citygrid.CityGrid;
import citygrid.CloudGenerator;
import citygrid.helper.ColorsHelper;
import citygrid.helper.FsqNameHelper;
import citygrid.helper.IconsHelper;
import citygrid.helper.PVectorCalc;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import toxi.color.TColor;

public class Satellite2DrawManager implements DrawManager{
	private static String satellitePicturePath = createPath();
	private CloudGenerator cg = new CloudGenerator();
	

	private static String createPath(){
		String path;
		if(CityGrid.BIG_IMG && !CityGrid.SMALL){
			path = "nokia_img2000/"+CityGrid.CITY+"/";
		}else{
			path = "nokia_img/"+CityGrid.CITY+"/";
		}
		return path;
	} 
	public PImage createPattern(String catName, int sizeX, int sizeY,  PVector[] maskShape, int tileSize, FsqData entry){
		System.out.println(this.getClass().getName()+" "+entry.day+" "+entry.hour+" : "+catName);
		String filename = "";
		if(CityGrid.SMALL){
			filename = entry.coords.get(catName).replace(" ", "_")+"_s.png";
		}else{
			filename = entry.coords.get(catName).replace(" ", "_")+".png";
		}
		String path = satellitePicturePath+filename;
		PImage satellitePic = CityGrid.p5.loadImage(path);
//		int maxSize = (sizeX > sizeY) ? sizeX : sizeY;
		int maxSize = CityGrid.MAX_HOUSE_SIZE;
		satellitePic.resize(maxSize, maxSize);

		PVector mid = PVectorCalc.calcMid(maskShape[1], maskShape[3]);
		
		PGraphics mask = CityGrid.p5.createGraphics(satellitePic.width, satellitePic.height, PApplet.JAVA2D);
		
		mask.beginDraw();
		mask.smooth();
		mask.background(0);
		mask.noStroke();
		mask.fill(255);
		mask.beginShape();
		for (int i = 0; i < maskShape.length; i++) {
			//PVector tPoint = PVectorCalc.interpolateTo(maskShape[i], mid, -0.1f);
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
		int iconSize = (int)(CityGrid.ICON_SIZE_SKETCH/CityGrid.SIZE_FACTOR);
		int maxHeight = PVectorCalc.calcMaxHeight(maskShape[0], maskShape[1], maskShape[2], maskShape[3]);
		iconSize = (iconSize < maxHeight) ? iconSize : maxHeight;

		PGraphics tile = CityGrid.p5.createGraphics(satellitePic.width, satellitePic.height, PApplet.JAVA2D);
		tile.beginDraw();
		tile.smooth();
		tile.image(satellitePic,-((maxSize-sizeX)/2),-((maxSize-sizeY)/2));		// move to center drawing
		tile.ellipseMode(PApplet.CENTER);
		tile.noStroke();
		tile.fill(255,CityGrid.ICON_BG_TRANSPARENCY);
		tile.ellipse(mid.x,mid.y, iconSize, iconSize);
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
		icon.resize(iconSize, iconSize);
		
		PGraphics overlayMask = CityGrid.p5.createGraphics(satellitePic.width, satellitePic.height, PApplet.JAVA2D);
		overlayMask.beginDraw();
		overlayMask.smooth();
		// overlay.background(0,150);
//		overlayMask.fill(CityGrid.OVERLAY_TRANSPARENCY);
		overlayMask.fill(ColorsHelper.OVERLAY_ALPHAS.get(FsqNameHelper.CATEGORY_PARENTS.get(catName)));
		overlayMask.noStroke();
		//overlayMask.stroke(CityGrid.OVERLAY_TRANSPARENCY);
		overlayMask.rect(-1,-1,overlayMask.width+2, overlayMask.height+2);
		overlayMask.fill(0);
		overlayMask.ellipse(mid.x, mid.y, iconSize, iconSize);
		overlayMask.tint(255,CityGrid.ICON_TRANSPARENCY);
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
	
	public PImage createEmptyPattern(int sizeX, int sizeY,  PVector[] maskShape, int tileSize){
		String path;
		if(CityGrid.SMALL){
			path = "data/nokia_img/blank_clouds_s.png";
		}else{
			path = "data/nokia_img/blank_clouds.png";
		}
		//PImage satellitePic = CityGrid.p5.loadImage(path);
		PImage satellitePic = cg.createCloudImage(3);
//		int maxSize = (sizeX > sizeY) ? sizeX : sizeY;
		int maxSize = CityGrid.MAX_HOUSE_SIZE;
		satellitePic.resize(maxSize, maxSize);
		
		//PVector mid = PVectorCalc.calcMid(maskShape[1], maskShape[3]);
		
		PGraphics mask = CityGrid.p5.createGraphics(satellitePic.width, satellitePic.height, PApplet.JAVA2D);
		
		mask.beginDraw();
		mask.smooth();
		mask.background(0);
		mask.fill(255);
		mask.beginShape();
		for (int i = 0; i < maskShape.length; i++) {
			//PVector tPoint = PVectorCalc.interpolateTo(maskShape[i], mid, -0.1f);
			mask.vertex(maskShape[i].x,maskShape[i].y);
		}
		mask.endShape();
		mask.endDraw();		
		PImage piMask = CityGrid.p5.createImage(satellitePic.width, satellitePic.height, PApplet.ARGB);
		piMask.set(0, 0, mask);
		//satellitePic.mask(piMask);
		
		PGraphics tile = CityGrid.p5.createGraphics(satellitePic.width, satellitePic.height, PApplet.JAVA2D);
		tile.beginDraw();
		tile.smooth();
		tile.image(satellitePic,-((maxSize-sizeX)/2),-((maxSize-sizeY)/2));		// move to center drawing
		tile.endDraw();
		//c.alpha=0.5f;
		//satellitePic.tint(c.toARGB());
		
		PImage tileImg = CityGrid.p5.createImage(tile.width, tile.height, PApplet.ARGB);
		tileImg.set(0, 0, tile);
		tileImg.mask(piMask);
		return tileImg;
	}

}

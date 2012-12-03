package citygrid.test;

import java.util.HashMap;
import java.util.Map.Entry;

import citygrid.CityGrid;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import toxi.color.TColor;

public class ColorGrid extends PApplet {
private static final Boolean EXPORT = true;
	public void setup(){
		if(EXPORT){
			size(2124,2124,PDF,"colorgrid/colorgrid"+"_"+year()+month()+day()+"__"+hour()+"_"+minute()+"_"+second()+".pdf");
		}else{
			size(2124,2124,JAVA2D);
		}
		HashMap<String, TColor> iconColors = new HashMap<String, TColor>();
		iconColors.put("Arts & Entertainment", TColor.newHex("E62733"));
		iconColors.put("College & University", TColor.newHex("E0006D"));
		iconColors.put("Food", TColor.newHex("917A2C"));
		iconColors.put("Nightlife Spot", TColor.newHex("582583"));
		iconColors.put("Outdoors & Recreation", TColor.newHex("00A44B"));
		iconColors.put("Professional & Other Places", TColor.newHex("00A385"));
		iconColors.put("Residence", TColor.newHex("FFED00"));
		iconColors.put("Shop & Service", TColor.newHex("0067B2"));
		iconColors.put("Travel & Transport", TColor.newHex("F39200"));
		TColor[] colors = iconColors.values().toArray(new TColor[0]);
		PImage img = loadImage("data/nokia_img/berlin/52.513_13.320402.png");
		img.resize(236, 236);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				pushMatrix();
				translate(236*i,236*j);
				PGraphics containerG = createGraphics(236, 236, JAVA2D);
				containerG.beginDraw();
				containerG.smooth();
				containerG.image(img,0,0);
				PGraphics colG = createGraphics(236, 236, JAVA2D);
				colG.beginDraw();
				colG.noStroke();
				colG.fill(colors[j].toARGB());
				colG.rect(0,0,236,236);
				colG.endDraw();
				PGraphics maskG = createGraphics(236, 236, JAVA2D);
				maskG.beginDraw();
				maskG.noStroke();
				int maskFill = (int)map(0.1f*(i+1),0,1,0,255);
				println(i+" : "+maskFill);
				maskG.fill(maskFill);
				maskG.rect(0,0,236,236);
				maskG.endDraw();
				PImage piMask = createImage(236, 236, PApplet.ARGB);
				piMask.set(0, 0, maskG);
				PImage colImg = createImage(236, 236, ARGB);
				colImg.set(0,0,colG);
				colImg.mask(piMask);
				containerG.image(colImg,0,0);
				containerG.endDraw();
				PImage container = createImage(236, 236, ARGB);
				container.set(0, 0, containerG);
				image(container,0,0);
				popMatrix();
			}
		}
		
		if(EXPORT){
			exit();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main(new String[] {  citygrid.test.ColorGrid.class.getName()});
	}

}

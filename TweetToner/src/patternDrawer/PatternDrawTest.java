package patternDrawer;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class PatternDrawTest extends PApplet {

	public static PApplet p5;
	
	PImage tile;
	PImage pattern;
	
	public PatternDrawTest() {
		// TODO Auto-generated constructor stub
	}
	
	public void setup(){
		p5 = this;
		size(800,800);
		frameRate(30);
		smooth();
//		tile = loadImage("data/fsq_icons/AccessoriesStore.png");
//		pattern = createGraphics(width, height,P2D);
//		drawPattern();
		PatternDrawManager pdm = new PatternDrawManager();
		pattern = pdm.createPattern("VideoGameStore", 10, 10, new PVector[]{new PVector(10,10),new PVector(500,10), new PVector(500,500), new PVector(10,250)});
	}
	public void draw(){
		background(100);
		pushStyle();
		fill(130);
		ellipse(width/2,height/2,width,height);
		popStyle();
		scale(0.5f);
		image(pattern,0,0);
	}
	
//	public void drawPattern(){
//		pattern.beginDraw();
//		pattern.background(255);
//		pattern.tint(255,0,0);
//		for (int i = 0; i < 10; i++) {
//			for (int j = 0; j < 10; j++) {
//				pattern.image(tile,tile.width*i,tile.height*j);	
//			}	
//		}
//		pattern.endDraw();
//	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main(new String[] {  patternDrawer.PatternDrawTest.class.getName()});
	}

}

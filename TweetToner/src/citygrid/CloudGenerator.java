package citygrid;

import java.util.Vector;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;


public class CloudGenerator {
	private PImage bg;
	private Vector<PImage> clouds = new Vector<PImage>();
	public CloudGenerator(){
		if(CityGrid.SMALL){
		  bg = CityGrid.p5.loadImage("data/clouds/bg_s.png");
		  clouds.add(CityGrid.p5.loadImage("data/clouds/cloud_1_s.png"));
		  clouds.add(CityGrid.p5.loadImage("data/clouds/cloud_2_s.png"));
		  clouds.add(CityGrid.p5.loadImage("data/clouds/cloud_3_s.png"));
		}else{
			  bg = CityGrid.p5.loadImage("data/clouds/bg.png");
			  clouds.add(CityGrid.p5.loadImage("data/clouds/cloud_1_s.png"));
			  clouds.add(CityGrid.p5.loadImage("data/clouds/cloud_2_s.png"));
			  clouds.add(CityGrid.p5.loadImage("data/clouds/cloud_3_s.png"));
			
		}
	} 
	public PImage createCloudImage(int cloudCount){
		  PGraphics gccomp = CityGrid.p5.createGraphics(bg.width, bg.height, PApplet.JAVA2D);
		  gccomp.image(bg,0,0);
		  gccomp.imageMode(PApplet.CENTER);
		  for(int i = 0; i < cloudCount; i++){
		    gccomp.image(clouds.get((int)(CityGrid.p5.random(clouds.size()))),(int)CityGrid.p5.random(bg.width),(int)CityGrid.p5.random(bg.height));
		  }
		  return gccomp.get();
		}
}

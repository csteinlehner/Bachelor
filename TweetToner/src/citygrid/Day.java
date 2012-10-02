package citygrid;

import processing.core.PApplet;

public class Day {
	String time;
	Integer t_count; 
	public Day(String time, Integer t_count) {
		this.time = time;
		this.t_count = t_count;
	}
	void draw(){
		CityGrid.p5.pushMatrix();
//		CityGrid.p5.translate(0, -t_count/2);
//		CityGrid.p5.translate(0,-t_count/4);
		CityGrid.p5.line(0, 0, 0, t_count/2);
		CityGrid.p5.popMatrix();
	}
}

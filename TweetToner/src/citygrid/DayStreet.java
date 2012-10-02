package citygrid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Vector;

import com.csvreader.CsvReader;

import processing.core.PApplet;

public class DayStreet {
	Integer t_count;
	private String csvPath;
	private Vector<Day> days = new Vector<Day>();
	public DayStreet(Integer weekday, Integer t_count){
		this.t_count = t_count;
		csvPath = "data/"+weekday+"_fsq_timecount_60_"+CityGrid.CITY+".csv";
		loadCsv();
	}
	public void draw(){
		CityGrid.p5.pushMatrix();
		// moves out of the middle
		CityGrid.p5.translate(100, 0);
		CityGrid.p5.line(0,0,PApplet.map(t_count,0,5000,0,400),0);
		CityGrid.p5.pushMatrix();
		float streetmove = PApplet.map(t_count,0,5000,0,400)/(24-1);
		for (Iterator<Day> iterator = days.iterator(); iterator.hasNext();) {
			Day day = iterator.next();
			// moves the street
			CityGrid.p5.pushMatrix();
			day.draw();
			CityGrid.p5.popMatrix();
			CityGrid.p5.translate(streetmove,0);
		}
		CityGrid.p5.popMatrix();
		CityGrid.p5.popMatrix();
	}
	
	private void loadCsv(){
try {
			
			CsvReader csvData = new CsvReader(csvPath,',',Charset.forName("UTF-8"));
		
			csvData.readHeaders();

			while (csvData.readRecord())
			{
				days.add(new Day(csvData.get("time"), Integer.parseInt(csvData.get("count"))));
			}
	
			csvData.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

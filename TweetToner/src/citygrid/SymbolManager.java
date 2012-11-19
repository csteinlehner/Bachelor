package citygrid;

import java.util.HashMap;

import processing.core.PShape;

public class SymbolManager {
	HashMap<DrawingType, PShape> symbols = new HashMap<DrawingType, PShape>();
	public SymbolManager() {
		symbols.put(DrawingType.FOOD, CityGrid.p5.loadShape("data/symbols/food.svg"));
		symbols.put(DrawingType.BUS, CityGrid.p5.loadShape("data/symbols/bus.svg"));
		symbols.put(DrawingType.HOSPITAL, CityGrid.p5.loadShape("data/symbols/hospital.svg"));
		symbols.put(DrawingType.HOTEL, CityGrid.p5.loadShape("data/symbols/hotel.svg"));
	}
	public PShape getSymbol(DrawingType dt){
			return symbols.get(dt);
	}

}

package citygrid;

import java.util.HashMap;

import processing.core.PShape;

public class SymbolManager {
	HashMap<DrawingType, PShape> symbols = new HashMap<DrawingType, PShape>();
	public SymbolManager() {
		symbols.put(DrawingType.FOOD, CityGrid.p5.loadShape("data/symbols/food.svg"));
		symbols.put(DrawingType.TRAVEL, CityGrid.p5.loadShape("data/symbols/bus.svg"));
	}
	public PShape getSymbol(DrawingType dt){
			return symbols.get(dt);
	}

}

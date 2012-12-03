package citygrid.helper;

import java.util.HashMap;
import java.util.Map.Entry;

import toxi.color.TColor;

public class ColorsHelper {
	public static final HashMap<String, TColor> ICON_COLORS = createColors();
	private static HashMap<String, TColor> createColors(){
//		TColor c_default = TColor.newRGBA(1,0,0,1);
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
//		for(Entry<String, TColor> e: iconColors.entrySet()){
//			e.getValue().saturate(0.15f);
//		}
		return iconColors;
	}
}

package citygrid.helper;

import java.util.HashMap;
import java.util.Map.Entry;

import toxi.color.TColor;

public class ColorsHelper {
	public static final HashMap<String, TColor> ICON_COLORS = createColors();
	public static final HashMap<String, Integer> OVERLAY_ALPHAS = createOverlayAlphas(); 
	private static HashMap<String, TColor> createColors(){
//		TColor c_default = TColor.newRGBA(1,0,0,1);
		HashMap<String, TColor> iconColors = new HashMap<String, TColor>();
		iconColors.put("Arts & Entertainment", TColor.newHex("E62733"));
		iconColors.put("College & University", TColor.newHex("E0006D"));
		iconColors.put("Food", TColor.newHex("917A2C"));
		iconColors.put("Nightlife Spot", TColor.newHex("582583"));
		iconColors.put("Outdoors & Recreation", TColor.newHex("00A43C"));
		iconColors.put("Professional & Other Places", TColor.newHex("00A38C"));
		iconColors.put("Residence", TColor.newHex("FFED00"));
		iconColors.put("Shop & Service", TColor.newHex("0067B2"));
		iconColors.put("Travel & Transport", TColor.newHex("F39200"));
//		for(Entry<String, TColor> e: iconColors.entrySet()){
//			e.getValue().saturate(0.15f);
//		}
		return iconColors;
	}
	
	private static HashMap<String, Integer> createOverlayAlphas(){
		HashMap<String, Integer> overlayAlpha = new HashMap<String, Integer>();
		overlayAlpha.put("Arts & Entertainment", 75);
		overlayAlpha.put("College & University", 75);
		overlayAlpha.put("Food", 75);
		overlayAlpha.put("Nightlife Spot", 100);
		overlayAlpha.put("Outdoors & Recreation", 75);
		overlayAlpha.put("Professional & Other Places", 75);
		overlayAlpha.put("Residence", 75);
		overlayAlpha.put("Shop & Service", 100);
		overlayAlpha.put("Travel & Transport",75);
		return overlayAlpha;
	}
}

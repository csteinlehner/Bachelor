package citygrid;

import java.util.HashMap;

import toxi.color.TColor;

public class ColorsHelper {
	public static final HashMap<String, TColor> ICON_COLORS = createColors();
	private static HashMap<String, TColor> createColors(){
//		TColor c_default = TColor.newRGBA(1,0,0,1);
		HashMap<String, TColor> iconColors = new HashMap<String, TColor>();
		iconColors.put("Arts & Entertainment", TColor.newHex("F18B8E"));
		iconColors.put("College & University", TColor.newHex("E2B8D7"));
		iconColors.put("Food", TColor.newHex("FFF7B2"));
		iconColors.put("Nightlife Spot", TColor.newHex("505297"));
		iconColors.put("Outdoors & Recreation", TColor.newHex("4DB05B"));
		iconColors.put("Professional & Other Places", TColor.newHex("83D0F5"));
		iconColors.put("Residence", TColor.newHex("BFD2BE"));
		iconColors.put("Shop & Service", TColor.newHex("83D0F5"));
		iconColors.put("Travel & Transport", TColor.newHex("F5A057"));
		return iconColors;
	}
}

package citygrid;

import citiygrid.dataObjects.FsqData;
import processing.core.PVector;

public class HouseCoordinate {
	PVector bl, br, tr, tl;
	FsqData entry;
	public HouseCoordinate(PVector bl, PVector br, PVector tr, PVector tl, FsqData entry) {
		this.bl = bl;
		this.br = br;
		this.tr = tr;
		this.tl = tl;
		this.entry = entry;
	}

}

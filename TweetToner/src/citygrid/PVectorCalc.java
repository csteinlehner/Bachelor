package citygrid;

import processing.core.PVector;

public class PVectorCalc {
	public static PVector interpolateTo(PVector v1, PVector v2, float f){
		  return new PVector(v1.x + (v2.x - v1.x) * f, v1.y + (v2.y - v1.y) * f);
	}
	
	public static PVector[] calcNormalizedQuad(PVector bl, PVector br, PVector tr, PVector tl){
		PVector origin = calcOrigin(bl, br, tr, tl);
		float yMove = tl.y-origin.y;
		PVector ttl = new PVector(0,0+yMove);
		PVector ttr = new PVector(tr.x-tl.x,(tr.y-tl.y)+yMove);
		PVector tbl = new PVector(0,bl.y-tl.y+yMove);
		PVector tbr = new PVector(br.x-bl.x,br.y-tr.y+ttr.y);
		return new PVector[]{tbl, tbr, ttr, ttl};
	}
	
	public static PVector calcMid(PVector br, PVector tl){
		PVector mid = PVector.sub(br, tl);
		mid.div(2);
		mid.add(tl);
		return mid;
	}
	
	public static PVector calcSize(PVector bl, PVector br, PVector tr, PVector tl){
		float x = br.x-bl.x;
		float y = br.y-tr.y;
		return new PVector(x,y);
	}
	public static PVector calcCompleteSize(PVector bl, PVector br, PVector tr, PVector tl){
		
		float x = br.x-bl.x;
		float y1 = bl.y-tr.y;
		float y2 = br.y-tl.y;
		
		return new PVector(x,Math.max(y1,y2));
	}
	
	public static PVector calcOrigin(PVector bl, PVector br, PVector tr, PVector tl){
		float x = Math.min(bl.x, tl.x);
		float y = Math.min(tl.y, tr.y);
		return new PVector(x, y);
	}
	
}

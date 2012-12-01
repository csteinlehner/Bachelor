package citygrid.helper;

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
		Float[] ys = new Float[4];
		ys[0] = bl.y-tr.y;
		ys[1] = br.y-tl.y;
		ys[2] = br.y-tr.y;
		ys[3] = bl.y-tl.y;
		float yMax = Integer.MIN_VALUE;
		for (float f : ys) if (f > yMax) yMax = f;
		return new PVector(x,yMax);
	}
		
	public static PVector calcOrigin(PVector bl, PVector br, PVector tr, PVector tl){
		float x = Math.min(bl.x, tl.x);
		float y = Math.min(tl.y, tr.y);
		return new PVector(x, y);
	}
	
	public static PVector calcOriginMidMove(PVector bl, PVector br, PVector tr, PVector tl, int sizeX, int sizeY){
		float x = Math.min(bl.x, tl.x);
		float y = Math.min(tl.y, tr.y);
		if(sizeX < sizeY){	// move left
			
		}else{	// move top
			
		}
		return new PVector(x, y);
	}
	
	public static int calcMaxHeight(PVector bl, PVector br, PVector tr, PVector tl){
		float ySize = bl.y-tl.y;
		float xSize = br.y-tr.y;
		int max = (int)((ySize > xSize) ? xSize : ySize);
		return max;
	}
	
}

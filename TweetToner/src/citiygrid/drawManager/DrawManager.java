package citiygrid.drawManager;

import citiygrid.dataObjects.FsqData;
import processing.core.PImage;
import processing.core.PVector;

public interface DrawManager {
	public PImage createPattern(String catName, int sizeX, int sizeY,  PVector[] maskShape, int tileSize, FsqData entry);
}

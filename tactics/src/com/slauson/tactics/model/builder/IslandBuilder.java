package com.slauson.tactics.model.builder;

import com.badlogic.gdx.math.Vector2;
import com.slauson.tactics.model.Island;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.utils.Utils;

public class IslandBuilder {
	
	public enum EdgeType {
		DISCONNECTED, CONNECTED, RANDOM;
		
		static EdgeType getEdgeType(boolean disconnected, boolean random) {
			if (disconnected) {
				return DISCONNECTED;
			} else if (random) {
				return RANDOM;
			} else {
				return CONNECTED;
			}
		}
	}
	
	private Region[][] regionsArray;
	
	// size offsets, assuming MIN_WIDTH/MIN_HEIGHT
	public int offsetWidth, offsetHeight;
	
	public IslandBuilder() {
		offsetWidth = 0;
		offsetHeight = 0;
	}
	
	public IslandBuilder(Region[][] regionsArray) {
		this.regionsArray = regionsArray;
	}
	
	public Island build(int regionOffsetX, int regionOffsetY, int maxIslandWidth, int maxIslandHeight, EdgeType edgeNorth, EdgeType edgeEast, EdgeType edgeSouth, EdgeType edgeWest) {
		
		int islandRegionOffsetStartY = regionOffsetY;
		int islandRegionOffsetStartX = regionOffsetX;
		int islandRegionOffsetEndY = regionOffsetY + maxIslandHeight - 1;
		int islandRegionOffsetEndX = regionOffsetX + maxIslandWidth - 1;
		
		// set min width/height
		// for connected edges, need to have width/height greater than half
		int minIslandWidth = (edgeNorth == EdgeType.CONNECTED || edgeSouth == EdgeType.CONNECTED) ? maxIslandWidth/2 + 1 : 1;
		int minIslandHeight = (edgeWest == EdgeType.CONNECTED || edgeEast == EdgeType.CONNECTED) ? maxIslandHeight/2 + 1 : 1;
		
		// first, adjust offsets/width/height
		if (edgeNorth == EdgeType.DISCONNECTED && (islandRegionOffsetEndY - islandRegionOffsetStartY + 1) > minIslandHeight) {
			islandRegionOffsetStartY++;
		}
		if (edgeWest == EdgeType.DISCONNECTED && (islandRegionOffsetEndX - islandRegionOffsetStartX + 1) > minIslandWidth) {
			islandRegionOffsetStartX++;
		}
		if (edgeSouth == EdgeType.DISCONNECTED && (islandRegionOffsetEndY - islandRegionOffsetStartY + 1) > minIslandHeight) {
			islandRegionOffsetEndY--;
		}
		if (edgeWest == EdgeType.DISCONNECTED && (islandRegionOffsetEndX - islandRegionOffsetStartX + 1) > minIslandWidth) {
			islandRegionOffsetEndX--;
		}
		
		System.out.println(String.format("island build 1: %d,%d - %d,%d - %s,%s,%s,%s - %d,%d - %d,%d - %d, %d", regionOffsetX, regionOffsetY, maxIslandWidth, maxIslandHeight, edgeNorth, edgeEast, edgeSouth, edgeWest, islandRegionOffsetStartX, islandRegionOffsetStartY, islandRegionOffsetEndX, islandRegionOffsetEndY, minIslandWidth, minIslandHeight));
		
		switch (edgeNorth) {
		case CONNECTED:
			// do nothing
			break;
		case DISCONNECTED:
		case RANDOM:
			islandRegionOffsetStartY += Utils.random().nextInt(islandRegionOffsetEndY - islandRegionOffsetStartY + 1 - minIslandHeight + 1);
			break;
		}
		
		switch (edgeWest) {
		case CONNECTED:
			// do nothing
			break;
		case DISCONNECTED:
		case RANDOM:
			islandRegionOffsetStartX += Utils.random().nextInt(islandRegionOffsetEndX - islandRegionOffsetStartX + 1 - minIslandWidth + 1);
			break;
		}
		
		System.out.println(String.format("island build 2: %d,%d - %d,%d - %s,%s,%s,%s - %d,%d - %d,%d - %d, %d", regionOffsetX, regionOffsetY, maxIslandWidth, maxIslandHeight, edgeNorth, edgeEast, edgeSouth, edgeWest, islandRegionOffsetStartX, islandRegionOffsetStartY, islandRegionOffsetEndX, islandRegionOffsetEndY, minIslandWidth, minIslandHeight));
		
		switch (edgeSouth) {
		case CONNECTED:
			// do nothing
			break;
		case DISCONNECTED:
		case RANDOM:
			islandRegionOffsetEndY -= Utils.random().nextInt(islandRegionOffsetEndY - islandRegionOffsetStartY + 1 - minIslandHeight + 1);
			break;
		}
		
		switch (edgeEast) {
		case CONNECTED:
			// do nothing
			break;
		case DISCONNECTED:
		case RANDOM:
			islandRegionOffsetEndX -= Utils.random().nextInt(islandRegionOffsetEndX - islandRegionOffsetStartX + 1 - minIslandWidth + 1);
			break;
		}
		
		System.out.println(String.format("island build 3: %d,%d - %d,%d - %s,%s,%s,%s - %d,%d - %d,%d - %d, %d", regionOffsetX, regionOffsetY, maxIslandWidth, maxIslandHeight, edgeNorth, edgeEast, edgeSouth, edgeWest, islandRegionOffsetStartX, islandRegionOffsetStartY, islandRegionOffsetEndX, islandRegionOffsetEndY, minIslandWidth, minIslandHeight));

		Island island = new Island(islandRegionOffsetEndX - islandRegionOffsetStartX + 1, islandRegionOffsetEndY - islandRegionOffsetStartY + 1);
		
		// construct regions
		for (int column = islandRegionOffsetStartX; column <= islandRegionOffsetEndX; column++) {
			for (int row = islandRegionOffsetStartY; row <= islandRegionOffsetEndY; row++) {
				regionsArray[column][row] = new Region(new Vector2(column, row), island);
				island.regions.add(regionsArray[column][row]);
			}
		}
		
		return island;
	}
	
	private static int MIN_WIDTH = 2, MIN_HEIGHT = 2;
	
	public Island buildVersion2(Region[][] regionsArray, int regionOffsetX, int regionOffsetY) {
		
		System.out.println(String.format("Island build (%s): %d, %d", this, regionOffsetX, regionOffsetY));
		
		Island island = new Island(MIN_WIDTH + offsetWidth, MIN_HEIGHT + offsetHeight);
		
		for (int column = regionOffsetX; column < regionOffsetX + MIN_WIDTH + offsetWidth; column++) {
			for (int row = regionOffsetY; row < regionOffsetY + MIN_HEIGHT + offsetHeight; row++) {
				regionsArray[column][row] = new Region(new Vector2(column, row));
				island.regions.add(regionsArray[column][row]);
			}
		}
		
		return island;
	}
	
	public Island buildVersion1(int x, int y, int width, int height) {
		
		System.out.println("Island build: " + x + ", " + y + "; " + width + ", " + height);
		
		if (width < MIN_WIDTH || height < MIN_HEIGHT) {
			throw new UnsupportedOperationException();
		}
		
		// region positions are with respect to lower left
		Island island = new Island(0, 0);
		
		// northwest
		regionsArray[x][y+height-1] = new Region(new Vector2(x, y+height-1));
		island.regions.add(regionsArray[x][y+height-1]);

		// north
		for (int i = 1; i < width-1; i++) {
			regionsArray[x+i][y+height-1] = new Region(new Vector2(x+i, y+height-1));
			island.regions.add(regionsArray[x+i][y+height-1]);
		}
		
		// northeast
		regionsArray[x+width-1][y+height-1] = new Region(new Vector2(x+width-1, y+height-1));
		island.regions.add(regionsArray[x+width-1][y+height-1]);
		
		// east
		for (int i = 1; i < height-1; i++) {
			regionsArray[x+width-1][y+i] = new Region(new Vector2(x+width-1, y+i));
			island.regions.add(regionsArray[x+width-1][y+i]);
		}
		
		// southeast
		regionsArray[x+width-1][y] = new Region(new Vector2(x+width-1, y));
		island.regions.add(regionsArray[x+width-1][y]);
		
		// south
		for (int i = 1; i < width-1; i++) {
			regionsArray[x+i][y] = new Region(new Vector2(x+i, y));
			island.regions.add(regionsArray[x+i][y]);
		}
		
		// southwest
		regionsArray[x][y] = new Region(new Vector2(x, y));
		island.regions.add(regionsArray[x][y]);
		
		// west
		for (int i = 1; i < height-1; i++) {
			regionsArray[x][y+i] = new Region(new Vector2(x, y+i));
			island.regions.add(regionsArray[x][y+i]);
		}
		
		// interior
		for (int i = 1; i < width-1; i++) {
			for (int j = 1; j < height-1; j++) {
				regionsArray[x+i][y+j] = new Region(new Vector2(x+i, y+j));
				island.regions.add(regionsArray[x+i][y+j]);
			}
		}
		
		return island;
	}
	
	@Override
	public String toString() {
		return String.format("%d, %d", offsetWidth, offsetHeight);
	}
}

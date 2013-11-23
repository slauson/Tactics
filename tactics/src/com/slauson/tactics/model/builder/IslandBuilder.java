package com.slauson.tactics.model.builder;

import com.badlogic.gdx.math.Vector2;
import com.slauson.tactics.model.Island;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.utils.Util;

public class IslandBuilder {
	
	public static final int MIN_WIDTH = 2;
	public static final int MIN_HEIGHT = 2;

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
	
	public Island build(int regionOffsetX, int regionOffsetY, int maxIslandWidth, int maxIslandHeight, boolean connectNorth, boolean connectEast, boolean connectSouth, boolean connectWest) {
		
		int islandRegionOffsetStartX = -1, islandRegionOffsetStartY = -1;
		int islandRegionOffsetEndX = -1, islandRegionOffsetEndY = -1;
		
		if (connectNorth) {
			islandRegionOffsetStartY = regionOffsetY;
		} else {
			islandRegionOffsetStartY = regionOffsetY + 1 + Util.random().nextInt(maxIslandHeight - MIN_HEIGHT);
		}
		
		if (connectWest) {
			islandRegionOffsetStartX = regionOffsetX;
		} else {
			islandRegionOffsetStartX = regionOffsetX + 1 + Util.random().nextInt(maxIslandWidth - MIN_WIDTH);
		}
		
		if (connectSouth) {
			islandRegionOffsetEndY = regionOffsetY + maxIslandHeight - 1;
		} else {
			islandRegionOffsetEndY = islandRegionOffsetStartY + MIN_HEIGHT + Util.random().nextInt((regionOffsetY + maxIslandHeight) - islandRegionOffsetStartY - MIN_HEIGHT);
		}
		
		if (connectEast) {
			islandRegionOffsetEndX = regionOffsetX + maxIslandWidth - 1;
		}
		else {
			islandRegionOffsetEndX = islandRegionOffsetStartX + MIN_HEIGHT + Util.random().nextInt((regionOffsetX + maxIslandWidth) - islandRegionOffsetStartX - MIN_WIDTH);
		}
		
		Island island = new Island(islandRegionOffsetEndX - islandRegionOffsetStartX + 1, islandRegionOffsetEndY - islandRegionOffsetStartY + 1);
		
		// construct regions
		for (int column = islandRegionOffsetStartX; column <= islandRegionOffsetEndX; column++) {
			for (int row = islandRegionOffsetStartY; row <= islandRegionOffsetEndY; row++) {
				regionsArray[column][row] = new Region(new Vector2(column, row));
				island.regions.add(regionsArray[column][row]);
			}
		}
		
		return island;
	}
	
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

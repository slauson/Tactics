package com.slauson.tactics.model.builder;

import com.badlogic.gdx.math.Vector2;
import com.slauson.tactics.model.Island;
import com.slauson.tactics.model.Region;

public class IslandBuilder {
	
	private static final int MIN_WIDTH = 2;
	private static final int MIN_HEIGHT = 2;

	private Region[][] regionsArray;
	
	public IslandBuilder(Region[][] regionsArray) {
		this.regionsArray = regionsArray;
	}
	
	public Island build(int x, int y, int width, int height) {
		
		System.out.println("Island build: " + x + ", " + y + "; " + width + ", " + height);
		
		if (width < MIN_WIDTH || height < MIN_HEIGHT) {
			throw new UnsupportedOperationException();
		}
		
		// region positions are with respect to lower left
		Island island = new Island();
		
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
}

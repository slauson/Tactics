package com.slauson.tactics.model;

import com.slauson.tactics.utils.Util;

public class IslandLayout {
	
	public Integer[][] islands;
	
	private int numIslands;

	public IslandLayout(int numIslands) {
		this.numIslands = numIslands;
		
		setup();
	}
	
	private void setup() {
		int layoutWidth, layoutHeight;
		
		switch (numIslands) {
		case 1:
			layoutWidth = 1;
			layoutHeight = 1;
			break;
		case 2:
			layoutWidth = 1;
			layoutHeight = 2;
			break;
		case 3:
		case 4:
			layoutWidth = 2;
			layoutHeight = 2;
			break;
		case 5:
		case 6:
			layoutWidth = 2;
			layoutHeight = 3;
			break;
		case 7:
		case 8:
		case 9:
			layoutWidth = 3;
			layoutHeight = 3;
			break;
		case 10:
		case 11:
		case 12:
			layoutWidth = 3;
			layoutHeight = 4;
			break;
		case 13:
		case 14:
		case 15:
		case 16:
			layoutWidth = 4;
			layoutHeight = 4;
			break;
		default:
			throw new UnsupportedOperationException();
		}
		
		// randomly swap width/height
		if (Util.random().nextBoolean()) {
			int temp = layoutWidth;
			layoutWidth = layoutHeight;
			layoutHeight = temp;
		}
		
		// assign islands to layout
		islands = new Integer[layoutWidth][layoutHeight];
		
		int numMissingIslands = (layoutWidth * layoutHeight) - numIslands;
		int islandNum = 0;
		
		for (int column = 0; column < islands.length; column++) {
			for (int row = 0; row < islands[column].length; row++) {
				if (numMissingIslands > 0
						&& (Util.random().nextFloat() > (100f * numMissingIslands / numIslands)
								|| numIslands - islandNum < numMissingIslands))
				{
					numMissingIslands--;
					islands[row][column] = -1;
				} else {
					islands[row][column] = islandNum;
					islandNum++;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		IslandLayout layout = new IslandLayout(3);
	}
}

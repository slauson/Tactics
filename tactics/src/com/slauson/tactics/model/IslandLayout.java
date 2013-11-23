package com.slauson.tactics.model;

import com.slauson.tactics.model.builder.IslandBuilder;
import com.slauson.tactics.utils.Util;

public class IslandLayout {
	
	public Boolean[][] islands;
	public Integer[] rowEmptyRegions;
	public Integer[] columnEmptyRegions;
	
	public int layoutWidth, layoutHeight;
	
	private int numIslands;

	public IslandLayout(int numIslands) {
		this.numIslands = numIslands;
		
		setup();
	}
	
	private void setup() {
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
		islands = new Boolean[layoutWidth][layoutHeight];
		
		int numMissingIslands = (layoutWidth * layoutHeight) - numIslands;
		int islandNum = 0;
		
		for (int column = 0; column < islands.length; column++) {
			for (int row = 0; row < islands[column].length; row++) {
				if (numMissingIslands > 0
						&& (Util.random().nextFloat() > (100f * numMissingIslands / numIslands)
								|| numIslands - islandNum < numMissingIslands))
				{
					numMissingIslands--;
					islands[column][row] = false;
				} else {
					islands[column][row] = true;
					islandNum++;
				}
			}
		}
		
		// row/column empty regions
		rowEmptyRegions = new Integer[layoutWidth];
		columnEmptyRegions = new Integer[layoutHeight];
		
	}
	
	/*
	public void randomize(int overworldWidth, int overworldHeight) {

		// compute actual width/height taking into account space between islands
		int overworldWidthActual = overworldWidth - islands.length + 1;
		int overworldHeightActual = overworldHeight - islands[0].length + 1;
		

		// randomize height
		for (int column = 0; column < islands.length; column++) {

			int rowIslandHeightOffset = overworldHeightActual - (islands[column].length * IslandBuilder.MIN_HEIGHT);
			
			for (int row = 0; row < islands[column].length; row++) {
				if (islands[column][row] != null) {
					islands[column][row].offsetHeight = Util.random().nextInt(rowIslandHeightOffset + 1);
					rowIslandHeightOffset -= islands[column][row].offsetHeight;
				}
			}
			
			// save # empty regions
			columnEmptyRegions[column] = rowIslandHeightOffset;
		}
		
		// randomize width (this assumes each column has same height)
		for (int row = 0; row < islands[0].length; row++) {

			int rowIslandWidthOffset = overworldWidthActual - (islands[0].length * IslandBuilder.MIN_WIDTH);
			
			for (int column = 0; column < islands.length; column++) {
				if (islands[column][row] != null) {
					islands[column][row].offsetWidth = Util.random().nextInt(rowIslandWidthOffset + 1);
					rowIslandWidthOffset -= islands[column][row].offsetWidth;
				}
			}
			
			// save # empty regions
			rowEmptyRegions[row] = rowIslandWidthOffset;
		}

	}
	*/
	
	public static void main(String[] args) {
		IslandLayout layout = new IslandLayout(3);
		//layout.randomize(10, 10);
	}
}

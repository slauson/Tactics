package com.slauson.tactics.model;

import java.util.ArrayList;
import java.util.List;

import com.slauson.tactics.utils.Util;

public class IslandLayout {
	
	public class BuildIsland {
		public int islandNum;
		public int row, column;
		public List<Direction> forceEdges;
		
		public BuildIsland(int islandNum, int column, int row) {
			this.islandNum = islandNum;
			this.column = column;
			this.row = row;
			forceEdges = new ArrayList<Direction>(4);
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			for (Direction direction : forceEdges) {
				if (builder.length() > 0) {
					builder.append(',');
				}
				builder.append(direction.name());
			}
			return String.format("%d, %d - %s", column, row, builder.toString());
		}
	}
	
	public enum Direction {
		NORTH, EAST, SOUTH, WEST;
		
		public Direction next() {
			switch (this) {
			case NORTH:
				return EAST;
			case EAST:
				return SOUTH;
			case SOUTH:
				return WEST;
			case WEST:
			default:
				return NORTH;
			}
		}
		
		public Direction opposite() {
			switch (this) {
			case NORTH:
				return SOUTH;
			case EAST:
				return WEST;
			case SOUTH:
				return NORTH;
			case WEST:
			default:
				return EAST;
			}
		}
	}
	
	public BuildIsland[][] islands;
	
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
		islands = new BuildIsland[layoutWidth][layoutHeight];
		
		int numMissingIslands = (layoutWidth * layoutHeight) - numIslands;
		int islandNum = 0;
		
		for (int column = 0; column < islands.length; column++) {
			for (int row = 0; row < islands[column].length; row++) {
				if (numMissingIslands > 0
						&& (Util.random().nextFloat() > (100f * numMissingIslands / numIslands)
								|| numIslands - islandNum < numMissingIslands))
				{
					numMissingIslands--;
					islands[column][row] = null;
				} else {
					islands[column][row] = new BuildIsland(islandNum, column, row);
					islandNum++;
				}
			}
		}
		
		forceEdgeTraversal();
	}
	
	/**
	 * Constructs force edges throughout island layout
	 * making each island connected.
	 */
	private void forceEdgeTraversal() {
		// dfs search list
		List<BuildIsland> traversedIslands = new ArrayList<BuildIsland>(numIslands);
		
		// choose random island to start with
		int column = Util.random().nextInt(layoutWidth);
		int row = Util.random().nextInt(layoutHeight);
		
		traversedIslands.add(islands[column][row]);
		
		while (traversedIslands.size() > 0) {
			
			// current island
			BuildIsland island = traversedIslands.get(traversedIslands.size() - 1);
						
			// choose random neighbor
			Direction randomNeighborDirection = Direction.values()[Util.random().nextInt(Direction.values().length)];
			int numNeighborsChecked = 0;
			
			do {
			
				int randomNeighborColumn = island.column;
				int randomNeighborRow = island.row;
				
				switch (randomNeighborDirection) {
				case NORTH:
					randomNeighborRow--;
					break;
				case EAST:
					randomNeighborColumn++;
					break;
				case SOUTH:
					randomNeighborRow++;
					break;
				case WEST:
					randomNeighborColumn--;
					break;
				}
				
				// find neighbor that isn't already connected
				if (randomNeighborColumn >= 0 && randomNeighborColumn < layoutWidth && randomNeighborRow >= 0 && randomNeighborRow < layoutHeight &&
						!island.forceEdges.contains(randomNeighborDirection) &&
						islands[randomNeighborColumn][randomNeighborRow] != null && islands[randomNeighborColumn][randomNeighborRow].forceEdges.size() == 0)
				{
					// force edge
					island.forceEdges.add(randomNeighborDirection);
					islands[randomNeighborColumn][randomNeighborRow].forceEdges.add(randomNeighborDirection.opposite());
					
					// add island to list of traversed islands
					traversedIslands.add(islands[randomNeighborColumn][randomNeighborRow]);
					
					// traverse to new island
					break;
				}
				
				// try next neighbor
				randomNeighborDirection = randomNeighborDirection.next();
				numNeighborsChecked++;
				
			} while (numNeighborsChecked < Direction.values().length);
			
			// finish traversing island if we didn't add something new
			if (numNeighborsChecked == Direction.values().length) {
				traversedIslands.remove(traversedIslands.size() - 1);
			}
		}
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

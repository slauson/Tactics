package com.slauson.tactics.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Overworld of islands/regions.
 * @author josh
 *
 */
public class Overworld {

	public int width, height;
	public Region[][] regions;
	public Island[] islands;
	
	public Overworld() {
		width = 8;
		height = 6;
		
		testOverworld();
		computeRegionNeighbors();
	}
	
	private void testOverworld() {
		islands = new Island[4];
		for (int i = 0; i < 4; i++) {
			islands[i] = new Island();
		}
		
		regions = new Region[width][height];
		
		// null fill
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				regions[i][j] = null;
			}
		}
		
		// top left
		regions[1][3] = new Region(new Vector2(-3, 0));
		regions[1][3].color = Color.RED;
		islands[0].regions.add(regions[1][3]);
		regions[1][4] = new Region(new Vector2(-3, 1));
		regions[1][4].color = Color.RED;
		islands[0].regions.add(regions[1][4]);
		regions[2][3] = new Region(new Vector2(-2, 0));
		regions[2][3].color = Color.RED;
		islands[0].regions.add(regions[2][3]);
		regions[2][4] = new Region(new Vector2(-2, 1));
		regions[2][4].color = Color.RED;
		islands[0].regions.add(regions[2][4]);
		
		// bottom left
		regions[2][0] = new Region(new Vector2(-2, -3));
		regions[2][0].color = Color.GREEN;
		islands[1].regions.add(regions[2][0]);
		regions[2][1] = new Region(new Vector2(-2, -2));
		regions[2][1].color = Color.GREEN;
		islands[1].regions.add(regions[2][1]);
		regions[3][0] = new Region(new Vector2(-1, -3));
		regions[3][0].color = Color.GREEN;
		islands[1].regions.add(regions[3][0]);
		regions[3][1] = new Region(new Vector2(-1, -2));
		regions[3][1].color = Color.GREEN;
		islands[1].regions.add(regions[3][1]);
		
		// top right
		regions[4][4] = new Region(new Vector2(0, 1));
		regions[4][4].color = Color.BLUE;
		islands[2].regions.add(regions[0][1]);
		regions[4][5] = new Region(new Vector2(0, 2));
		regions[4][5].color = Color.BLUE;
		islands[2].regions.add(regions[4][5]);
		regions[5][4] = new Region(new Vector2(1, 1));
		regions[5][4].color = Color.BLUE;
		islands[2].regions.add(regions[5][4]);
		regions[5][5] = new Region(new Vector2(1, 2));
		regions[5][5].color = Color.BLUE;
		islands[2].regions.add(regions[5][5]);
		
		// bottom right
		regions[5][1] = new Region(new Vector2(1, -2));
		regions[5][1].color = Color.ORANGE;
		islands[3].regions.add(regions[5][1]);
		regions[5][2] = new Region(new Vector2(1, -1));
		regions[5][2].color = Color.ORANGE;
		islands[3].regions.add(regions[5][2]);
		regions[6][1] = new Region(new Vector2(2, -2));
		regions[6][1].color = Color.ORANGE;
		islands[3].regions.add(regions[6][1]);
		regions[6][2] = new Region(new Vector2(2, -1));
		regions[6][2].color = Color.ORANGE;
		islands[3].regions.add(regions[6][2]);
		regions[7][1] = new Region(new Vector2(3, -2));
		regions[7][1].color = Color.ORANGE;
		islands[3].regions.add(regions[7][1]);
		regions[7][2] = new Region(new Vector2(3, -1));
		regions[7][2].color = Color.ORANGE;
		islands[3].regions.add(regions[7][2]);
	}
	
	/**
	 * Computes neighboring regions of each region.
	 */
	private void computeRegionNeighbors() {
		for (int column = 0; column < regions.length; column++) {
			for (int row = 0; row < regions[column].length; row++) {
				if (regions[column][row] != null) {
					// surrounding regions
					// left
					if (column - 1 >= 0 && regions[column-1][row] != null) {
						regions[column][row].neighbors.add(regions[column-1][row]);
					}
					// up
					if (row + 1 < regions[column].length && regions[column][row+1] != null) {
						regions[column][row].neighbors.add(regions[column][row+1]);
					}
					// right
					if (column + 1 < regions.length && regions[column+1][row] != null) {
						regions[column][row].neighbors.add(regions[column+1][row]);
					}
					// down
					if (row - 1 >= 0 && regions[column][row-1] != null) {
						regions[column][row].neighbors.add(regions[column][row-1]);
					}
					
					// across regions
					// left
					if (column - 1 >= 0 && regions[column-1][row] == null &&
							column - 2 >= 0 && regions[column-2][row] != null)
					{
						regions[column][row].neighbors.add(regions[column-2][row]);
					}
					// up
					if (row + 1 < regions[column].length && regions[column][row+1] == null &&
							row + 2 < regions[column].length && regions[column][row+2] != null)
					{
						regions[column][row].neighbors.add(regions[column][row+2]);
					}
					// right
					if (column + 1 < regions.length && regions[column+1][row] == null &&
							column + 2 < regions.length && regions[column+2][row] != null)
					{
						regions[column][row].neighbors.add(regions[column+2][row]);
					}
					// down
					if (row - 1 >= 0 && regions[column][row-1] == null &&
							row - 2 >= 0 && regions[column][row-2] != null)
					{
						regions[column][row].neighbors.add(regions[column][row-2]);
					}
				}
			}
		}
	}
}

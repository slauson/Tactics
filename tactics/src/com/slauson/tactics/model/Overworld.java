package com.slauson.tactics.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.slauson.tactics.model.Player.Type;

/**
 * Overworld of islands/regions.
 * @author josh
 *
 */
public class Overworld {
	
	/*
	 * TODO
	 * - customize size
	 * - customize # islands
	 * - customize # regions 
	 */

	public int width, height;
	public Region[][] regions;
	public Island[] islands;
	
	public Player[] players;
	
	public List<Region> regionList;
	
	public int playerTurnIndex;
	
	
	public Overworld(int numPlayers) {
		width = 8;
		height = 6;
		
		players = new Player[numPlayers];
		
		playerTurnIndex = 0;
		
		// player always controls first player
		players[0] = new Player(Type.PLAYER);
		for (int i = 1; i < players.length; i++) {
			players[i] = new Player(Type.AI);
		}
		
		testOverworld();
		computeRegionNeighbors();
		assignRegionPlayers();
	}
	
	private void testOverworld() {
		islands = new Island[4];
		for (int i = 0; i < 4; i++) {
			islands[i] = new Island();
		}
		
		regionList = new ArrayList<Region>();
		regions = new Region[width][height];
		
		// null fill
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				regions[i][j] = null;
			}
		}
		
		// top left
		regions[1][3] = new Region(new Vector2(-3, 0));
		islands[0].regions.add(regions[1][3]);
		regionList.add(regions[1][3]);
		regions[1][4] = new Region(new Vector2(-3, 1));
		islands[0].regions.add(regions[1][4]);
		regionList.add(regions[1][4]);
		regions[2][3] = new Region(new Vector2(-2, 0));
		islands[0].regions.add(regions[2][3]);
		regionList.add(regions[2][3]);
		regions[2][4] = new Region(new Vector2(-2, 1));
		islands[0].regions.add(regions[2][4]);
		regionList.add(regions[2][4]);
		
		// bottom left
		regions[2][0] = new Region(new Vector2(-2, -3));
		islands[1].regions.add(regions[2][0]);
		regionList.add(regions[2][0]);
		regions[2][1] = new Region(new Vector2(-2, -2));
		islands[1].regions.add(regions[2][1]);
		regionList.add(regions[2][1]);
		regions[3][0] = new Region(new Vector2(-1, -3));
		islands[1].regions.add(regions[3][0]);
		regionList.add(regions[3][0]);
		regions[3][1] = new Region(new Vector2(-1, -2));
		islands[1].regions.add(regions[3][1]);
		regionList.add(regions[3][1]);
		
		// top right
		regions[4][4] = new Region(new Vector2(0, 1));
		islands[2].regions.add(regions[0][1]);
		regionList.add(regions[4][4]);
		regions[4][5] = new Region(new Vector2(0, 2));
		islands[2].regions.add(regions[4][5]);
		regionList.add(regions[4][5]);
		regions[5][4] = new Region(new Vector2(1, 1));
		islands[2].regions.add(regions[5][4]);
		regionList.add(regions[5][4]);
		regions[5][5] = new Region(new Vector2(1, 2));
		islands[2].regions.add(regions[5][5]);
		regionList.add(regions[5][5]);
		
		// bottom right
		regions[5][1] = new Region(new Vector2(1, -2));
		islands[3].regions.add(regions[5][1]);
		regionList.add(regions[5][1]);
		regions[5][2] = new Region(new Vector2(1, -1));
		islands[3].regions.add(regions[5][2]);
		regionList.add(regions[5][2]);
		regions[6][1] = new Region(new Vector2(2, -2));
		islands[3].regions.add(regions[6][1]);
		regionList.add(regions[6][1]);
		regions[6][2] = new Region(new Vector2(2, -1));
		islands[3].regions.add(regions[6][2]);
		regionList.add(regions[6][2]);
		regions[7][1] = new Region(new Vector2(3, -2));
		islands[3].regions.add(regions[7][1]);
		regionList.add(regions[7][1]);
		regions[7][2] = new Region(new Vector2(3, -1));
		islands[3].regions.add(regions[7][2]);
		regionList.add(regions[7][2]);
		
	}
	
	/**
	 * Computes neighboring regions of each region.
	 */
	private void computeRegionNeighbors() {
		for (int column = 0; column < regions.length; column++) {
			for (int row = 0; row < regions[column].length; row++) {
				if (regions[column][row] != null) {
					
					// directly neighbors
					
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
//					// left
//					if (column - 1 >= 0 && regions[column-1][row] == null &&
//							column - 2 >= 0 && regions[column-2][row] != null)
//					{
//						regions[column][row].neighbors.add(regions[column-2][row]);
//					}
//					// up
//					if (row + 1 < regions[column].length && regions[column][row+1] == null &&
//							row + 2 < regions[column].length && regions[column][row+2] != null)
//					{
//						regions[column][row].neighbors.add(regions[column][row+2]);
//					}
//					// right
//					if (column + 1 < regions.length && regions[column+1][row] == null &&
//							column + 2 < regions.length && regions[column+2][row] != null)
//					{
//						regions[column][row].neighbors.add(regions[column+2][row]);
//					}
//					// down
//					if (row - 1 >= 0 && regions[column][row-1] == null &&
//							row - 2 >= 0 && regions[column][row-2] != null)
//					{
//						regions[column][row].neighbors.add(regions[column][row-2]);
//					}
					
					// ranged neighbors
					
					// left
					if (column - 2 >= 0 && regions[column-2][row] != null) {
						regions[column][row].rangedNeighbors.add(regions[column-2][row]);
					}
					// up
					if (row + 2 < regions[column].length && regions[column][row+2] != null) {
						regions[column][row].rangedNeighbors.add(regions[column][row+2]);
					}
					// right
					if (column + 2 < regions.length && regions[column+2][row] != null) {
						regions[column][row].rangedNeighbors.add(regions[column+2][row]);
					}
					// down
					if (row - 2 >= 0 && regions[column][row-2] != null) {
						regions[column][row].rangedNeighbors.add(regions[column][row-2]);
					}
					
					// up left
					if (column - 1 >= 0 && row + 1 < regions[column-1].length && regions[column-1][row+1] != null) {
						regions[column][row].rangedNeighbors.add(regions[column-1][row+1]);
					}
					// up right
					if (column + 1 < regions.length && row + 1 < regions[column+1].length && regions[column+1][row+1] != null) {
						regions[column][row].rangedNeighbors.add(regions[column+1][row+1]);
					}
					// down left
					if (column - 1 >= 0 && row - 1 >= 0 && regions[column-1][row-1] != null) {
						regions[column][row].rangedNeighbors.add(regions[column-1][row-1]);
					}
					// down right
					if (column + 1 < regions.length && row - 1 >= 0 && regions[column+1][row-1] != null) {
						regions[column][row].rangedNeighbors.add(regions[column+1][row-1]);
					}
				}
			}
		}
	}
	
	/**
	 * Randomly assigns regions to players.
	 */
	private void assignRegionPlayers() {
		
		Random random = new Random();
		
		// random player index
		int playerIndex = random.nextInt(players.length);
		
		// random region indices
		List<Integer> regionIndices = new ArrayList<Integer>(regionList.size());		
		for (int i = 0; i < regionList.size(); i++) {
			regionIndices.add(random.nextInt(regionIndices.size() + 1), i);
		}
		
		for (int i = 0; i < regionIndices.size(); i++) {
			regionList.get(regionIndices.get(i)).player = players[playerIndex % players.length];
			players[playerIndex % players.length].regions++;
			players[playerIndex % players.length].units++;
			playerIndex++;
		}
	}
}

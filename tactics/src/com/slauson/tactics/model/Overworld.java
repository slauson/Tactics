package com.slauson.tactics.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.slauson.tactics.model.Player.Type;
import com.slauson.tactics.utils.Util;

/**
 * Overworld of islands/regionsArray.
 * @author josh
 *
 */
public class Overworld {
	
	/*
	 * TODO
	 * - customize size
	 * - customize # islands
	 * - customize # regionsArray 
	 */
	
	public enum Phase {
		ATTACK, REINFORCE;
		
		public Phase next() {
			if (this == ATTACK) {
				return REINFORCE;
			} else {
				return ATTACK;
			}
		}
	}
	public Phase phase;

	public int width, height;
	
	public List<Island> islands;
	public List<Region> regions;
	public List<Player> players;
	
	private Region[][] regionsArray;
	
	private int playerTurnIndex;
	
	public Overworld(int numPlayers) {
		width = 8;
		height = 6;
		
		phase = Phase.ATTACK;
		
		players = new ArrayList<Player>(numPlayers);
		
		playerTurnIndex = 0;
		
		// player always controls first player
		players.add(new Player(Type.PLAYER));
		for (int i = 1; i < numPlayers; i++) {
			players.add(new Player(Type.AI));
		}
		
		testOverworld();
		computeRegionNeighbors();
		assignRegionPlayers();
	}
	
	/**
	 * Moves to next phase.
	 */
	public void nextPhase() {
		phase.next();
	}
	
	/**
	 * Moves to next turn.
	 */
	public void nextTurn() {
		playerTurnIndex++;
		if (playerTurnIndex >= players.size()) {
			playerTurnIndex = 0;
		}
	}
	
	/**
	 * Returns currently active player.
	 * @return
	 */
	public Player activePlayer() {
		return players.get(playerTurnIndex);
	}
	
	/**
	 * Returns currently active player index.
	 * @return
	 */
	public int activePlayerIndex() {
		return playerTurnIndex;
	}
	
	private void testOverworld() {
		
		int numIslands = 4;
		
		islands = new ArrayList<Island>(numIslands);
		for (int i = 0; i < numIslands; i++) {
			islands.add(new Island());
		}
		
		regions = new ArrayList<Region>();
		regionsArray = new Region[width][height];
		
		// null fill
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				regionsArray[i][j] = null;
			}
		}
		
		// top left
		regionsArray[1][3] = new Region(new Vector2(-3, 0));
		islands.get(0).regions.add(regionsArray[1][3]);
		regions.add(regionsArray[1][3]);
		regionsArray[1][4] = new Region(new Vector2(-3, 1));
		islands.get(0).regions.add(regionsArray[1][4]);
		regions.add(regionsArray[1][4]);
		regionsArray[2][3] = new Region(new Vector2(-2, 0));
		islands.get(0).regions.add(regionsArray[2][3]);
		regions.add(regionsArray[2][3]);
		regionsArray[2][4] = new Region(new Vector2(-2, 1));
		islands.get(0).regions.add(regionsArray[2][4]);
		regions.add(regionsArray[2][4]);
		
		// bottom left
		regionsArray[2][0] = new Region(new Vector2(-2, -3));
		islands.get(1).regions.add(regionsArray[2][0]);
		regions.add(regionsArray[2][0]);
		regionsArray[2][1] = new Region(new Vector2(-2, -2));
		islands.get(1).regions.add(regionsArray[2][1]);
		regions.add(regionsArray[2][1]);
		regionsArray[3][0] = new Region(new Vector2(-1, -3));
		islands.get(1).regions.add(regionsArray[3][0]);
		regions.add(regionsArray[3][0]);
		regionsArray[3][1] = new Region(new Vector2(-1, -2));
		islands.get(1).regions.add(regionsArray[3][1]);
		regions.add(regionsArray[3][1]);
		
		// top right
		regionsArray[4][4] = new Region(new Vector2(0, 1));
		islands.get(2).regions.add(regionsArray[0][1]);
		regions.add(regionsArray[4][4]);
		regionsArray[4][5] = new Region(new Vector2(0, 2));
		islands.get(2).regions.add(regionsArray[4][5]);
		regions.add(regionsArray[4][5]);
		regionsArray[5][4] = new Region(new Vector2(1, 1));
		islands.get(2).regions.add(regionsArray[5][4]);
		regions.add(regionsArray[5][4]);
		regionsArray[5][5] = new Region(new Vector2(1, 2));
		islands.get(2).regions.add(regionsArray[5][5]);
		regions.add(regionsArray[5][5]);
		
		// bottom right
		regionsArray[5][1] = new Region(new Vector2(1, -2));
		islands.get(3).regions.add(regionsArray[5][1]);
		regions.add(regionsArray[5][1]);
		regionsArray[5][2] = new Region(new Vector2(1, -1));
		islands.get(3).regions.add(regionsArray[5][2]);
		regions.add(regionsArray[5][2]);
		regionsArray[6][1] = new Region(new Vector2(2, -2));
		islands.get(3).regions.add(regionsArray[6][1]);
		regions.add(regionsArray[6][1]);
		regionsArray[6][2] = new Region(new Vector2(2, -1));
		islands.get(3).regions.add(regionsArray[6][2]);
		regions.add(regionsArray[6][2]);
		regionsArray[7][1] = new Region(new Vector2(3, -2));
		islands.get(3).regions.add(regionsArray[7][1]);
		regions.add(regionsArray[7][1]);
		regionsArray[7][2] = new Region(new Vector2(3, -1));
		islands.get(3).regions.add(regionsArray[7][2]);
		regions.add(regionsArray[7][2]);
		
	}
	
	/**
	 * Computes neighboring regionsArray of each region.
	 */
	private void computeRegionNeighbors() {
		for (int column = 0; column < regionsArray.length; column++) {
			for (int row = 0; row < regionsArray[column].length; row++) {
				if (regionsArray[column][row] != null) {
					
					// directly neighbors
					
					// surrounding regionsArray
					// left
					if (column - 1 >= 0 && regionsArray[column-1][row] != null) {
						regionsArray[column][row].neighbors.add(regionsArray[column-1][row]);
					}
					// up
					if (row + 1 < regionsArray[column].length && regionsArray[column][row+1] != null) {
						regionsArray[column][row].neighbors.add(regionsArray[column][row+1]);
					}
					// right
					if (column + 1 < regionsArray.length && regionsArray[column+1][row] != null) {
						regionsArray[column][row].neighbors.add(regionsArray[column+1][row]);
					}
					// down
					if (row - 1 >= 0 && regionsArray[column][row-1] != null) {
						regionsArray[column][row].neighbors.add(regionsArray[column][row-1]);
					}
					
					// across regionsArray
//					// left
//					if (column - 1 >= 0 && regionsArray[column-1][row] == null &&
//							column - 2 >= 0 && regionsArray[column-2][row] != null)
//					{
//						regionsArray[column][row].neighbors.add(regionsArray[column-2][row]);
//					}
//					// up
//					if (row + 1 < regionsArray[column].length && regionsArray[column][row+1] == null &&
//							row + 2 < regionsArray[column].length && regionsArray[column][row+2] != null)
//					{
//						regionsArray[column][row].neighbors.add(regionsArray[column][row+2]);
//					}
//					// right
//					if (column + 1 < regionsArray.length && regionsArray[column+1][row] == null &&
//							column + 2 < regionsArray.length && regionsArray[column+2][row] != null)
//					{
//						regionsArray[column][row].neighbors.add(regionsArray[column+2][row]);
//					}
//					// down
//					if (row - 1 >= 0 && regionsArray[column][row-1] == null &&
//							row - 2 >= 0 && regionsArray[column][row-2] != null)
//					{
//						regionsArray[column][row].neighbors.add(regionsArray[column][row-2]);
//					}
					
					// ranged neighbors
					
					// left
					if (column - 2 >= 0 && regionsArray[column-2][row] != null) {
						regionsArray[column][row].rangedNeighbors.add(regionsArray[column-2][row]);
					}
					// up
					if (row + 2 < regionsArray[column].length && regionsArray[column][row+2] != null) {
						regionsArray[column][row].rangedNeighbors.add(regionsArray[column][row+2]);
					}
					// right
					if (column + 2 < regionsArray.length && regionsArray[column+2][row] != null) {
						regionsArray[column][row].rangedNeighbors.add(regionsArray[column+2][row]);
					}
					// down
					if (row - 2 >= 0 && regionsArray[column][row-2] != null) {
						regionsArray[column][row].rangedNeighbors.add(regionsArray[column][row-2]);
					}
					
					// up left
					if (column - 1 >= 0 && row + 1 < regionsArray[column-1].length && regionsArray[column-1][row+1] != null) {
						regionsArray[column][row].rangedNeighbors.add(regionsArray[column-1][row+1]);
					}
					// up right
					if (column + 1 < regionsArray.length && row + 1 < regionsArray[column+1].length && regionsArray[column+1][row+1] != null) {
						regionsArray[column][row].rangedNeighbors.add(regionsArray[column+1][row+1]);
					}
					// down left
					if (column - 1 >= 0 && row - 1 >= 0 && regionsArray[column-1][row-1] != null) {
						regionsArray[column][row].rangedNeighbors.add(regionsArray[column-1][row-1]);
					}
					// down right
					if (column + 1 < regionsArray.length && row - 1 >= 0 && regionsArray[column+1][row-1] != null) {
						regionsArray[column][row].rangedNeighbors.add(regionsArray[column+1][row-1]);
					}
				}
			}
		}
	}
	
	/**
	 * Randomly assigns regionsArray to players.
	 */
	private void assignRegionPlayers() {
		
		// random player index
		int playerIndex = Util.random().nextInt(players.size());
		
		// random region indices
		List<Integer> regionIndices = new ArrayList<Integer>(regions.size());		
		for (int i = 0; i < regions.size(); i++) {
			regionIndices.add(Util.random().nextInt(regionIndices.size() + 1), i);
		}
		
		for (int i = 0; i < regionIndices.size(); i++) {
			regions.get(regionIndices.get(i)).player = players.get(playerIndex % players.size());
			players.get(playerIndex % players.size()).regions++;
			players.get(playerIndex % players.size()).units++;
			playerIndex++;
		}
	}
}

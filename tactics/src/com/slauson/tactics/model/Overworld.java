package com.slauson.tactics.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.slauson.tactics.model.Neighbor.Type;
import com.slauson.tactics.utils.Utils;

/**
 * Overworld of islands/regionsArray.
 * 
 * @author josh
 * 
 */
public class Overworld {

	public enum Phase {
		ATTACK, REINFORCE, BATTLE;

		public Phase next() {
			if (this == ATTACK || this == BATTLE) {
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

	public Overworld(int width, int height, Region[][] regionsArray,
			List<Island> islands, List<Player> players) {
		this.width = width;
		this.height = height;
		this.regionsArray = regionsArray;
		this.islands = islands;
		this.players = players;

		phase = Phase.ATTACK;

		playerTurnIndex = 0;

		// testOverworld();
		computeRegionList();
		computeRegionNeighbors();
		assignRegionPlayers();
	}

	/**
	 * Moves to next phase.
	 */
	public void nextPhase() {
		phase = phase.next();
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
	 * 
	 * @return
	 */
	public Player activePlayer() {
		return players.get(playerTurnIndex);
	}

	/**
	 * Returns currently active player index.
	 * 
	 * @return
	 */
	public int activePlayerIndex() {
		return playerTurnIndex;
	}

	/**
	 * Returns the region containing the given point, if any. Assumes the
	 * following is true: regions are of size 1, overworld is centered at 0,0,
	 * overworld width/height is even
	 * 
	 * @param worldX
	 * @param worldY
	 * @return
	 */
	public Region getContainingRegion(float worldX, float worldY) {
		worldX += width / 2;
		worldY += height / 2;

		if (worldX >= 0 && worldX < width && worldY >= 0 && worldY < height) {
			return regionsArray[(int) worldX][(int) worldY];
		}

		return null;
	}
	
	public void reset() {
		for (Island island : islands) {
			island.reset();
		}
		assignRegionPlayers();
	}

	private void testOverworld() {

		int numIslands = 4;

		islands = new ArrayList<Island>(numIslands);
		for (int i = 0; i < numIslands; i++) {
			islands.add(new Island(0, 0));
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
		islands.get(2).regions.add(regionsArray[4][4]);
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

	private void computeRegionList() {
		regions = new ArrayList<Region>();
		for (int column = 0; column < regionsArray.length; column++) {
			for (int row = 0; row < regionsArray[column].length; row++) {
				if (regionsArray[column][row] != null) {
					regions.add(regionsArray[column][row]);
				}
			}
		}
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
					if (column - 1 >= 0
							&& regionsArray[column - 1][row] != null) {
						regionsArray[column][row].neighbors.add(
								regionsArray[column - 1][row],
								Type.DIRECT);
					}
					// up
					if (row + 1 < regionsArray[column].length
							&& regionsArray[column][row + 1] != null) {
						regionsArray[column][row].neighbors.add(
								regionsArray[column][row + 1],
								Type.DIRECT);
					}
					// right
					if (column + 1 < regionsArray.length
							&& regionsArray[column + 1][row] != null) {
						regionsArray[column][row].neighbors.add(
								regionsArray[column + 1][row],
								Type.DIRECT);
					}
					// down
					if (row - 1 >= 0 && regionsArray[column][row - 1] != null) {
						regionsArray[column][row].neighbors.add(
								regionsArray[column][row - 1],
								Type.DIRECT);
					}

					// ranged neighbors
					// left
					if (column - 2 >= 0
							&& regionsArray[column - 2][row] != null) {
						if (regionsArray[column - 1][row] == null) {
							regionsArray[column][row].neighbors.add(
									regionsArray[column - 2][row],
									Type.RANGED_INTER_ISLAND);
						} else {
							regionsArray[column][row].neighbors.add(
									regionsArray[column - 2][row],
									Type.RANGED);
						}
					}
					// up
					if (row + 2 < regionsArray[column].length
							&& regionsArray[column][row + 2] != null) {
						if (regionsArray[column][row + 1] == null) {
							regionsArray[column][row].neighbors.add(
									regionsArray[column][row + 2],
									Type.RANGED_INTER_ISLAND);
						} else {
							regionsArray[column][row].neighbors.add(
									regionsArray[column][row + 2],
									Type.RANGED);
						}
					}
					// right
					if (column + 2 < regionsArray.length
							&& regionsArray[column + 2][row] != null) {
						if (regionsArray[column + 1][row] == null) {
							regionsArray[column][row].neighbors.add(
									regionsArray[column + 2][row],
									Type.RANGED_INTER_ISLAND);
						} else {
							regionsArray[column][row].neighbors.add(
									regionsArray[column + 2][row],
									Type.RANGED);
						}
					}
					// down
					if (row - 2 >= 0 && regionsArray[column][row - 2] != null) {
						if (regionsArray[column][row - 1] == null) {
							regionsArray[column][row].neighbors.add(
									regionsArray[column][row - 2],
									Type.RANGED_INTER_ISLAND);
						} else {
							regionsArray[column][row].neighbors.add(
									regionsArray[column][row - 2],
									Type.RANGED);
						}
					}

					// up left
					if (column - 1 >= 0
							&& row + 1 < regionsArray[column - 1].length
							&& regionsArray[column - 1][row + 1] != null) {
						regionsArray[column][row].neighbors.add(
								regionsArray[column - 1][row + 1],
								Type.RANGED);
					}
					// up right
					if (column + 1 < regionsArray.length
							&& row + 1 < regionsArray[column + 1].length
							&& regionsArray[column + 1][row + 1] != null) {
						regionsArray[column][row].neighbors.add(
								regionsArray[column + 1][row + 1],
								Type.RANGED);
					}
					// down left
					if (column - 1 >= 0 && row - 1 >= 0
							&& regionsArray[column - 1][row - 1] != null) {
						regionsArray[column][row].neighbors.add(
								regionsArray[column - 1][row - 1],
								Type.RANGED);
					}
					// down right
					if (column + 1 < regionsArray.length && row - 1 >= 0
							&& regionsArray[column + 1][row - 1] != null) {
						regionsArray[column][row].neighbors.add(
								regionsArray[column + 1][row - 1],
								Type.RANGED);
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
		int playerIndex = Utils.random().nextInt(players.size());

		// random region indices
		List<Integer> regionIndices = new ArrayList<Integer>(regions.size());
		for (int i = 0; i < regions.size(); i++) {
			regionIndices.add(Utils.random().nextInt(regionIndices.size() + 1),
					i);
		}

		for (int i = 0; i < regionIndices.size(); i++) {
			regions.get(regionIndices.get(i)).player = players.get(playerIndex
					% players.size());
			players.get(playerIndex % players.size()).regions++;
			players.get(playerIndex % players.size()).units++;
			playerIndex++;
		}
	}
}

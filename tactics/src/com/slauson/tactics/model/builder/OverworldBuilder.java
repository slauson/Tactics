package com.slauson.tactics.model.builder;

import java.util.ArrayList;
import java.util.List;

import com.slauson.tactics.model.Island;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Player.PlayerType;

public class OverworldBuilder {

	private int width, height;
	
	private int numPlayers;
	
	private int numIslands;
	
	public OverworldBuilder(int width, int height, int numPlayers, int numIslands) {
		this.width = width;
		this.height = height;
		this.numPlayers = numPlayers;
		this.numIslands = numIslands;
	}
	
	public Overworld build() {
		
		// evenly divide overworld among islands
		int numIslandsHorizontal, numIslandsVertical;
		
		switch (numIslands) {
		case 2:
			numIslandsHorizontal = 2;
			numIslandsVertical = 1;
			break;
		case 4:
			numIslandsHorizontal = 2;
			numIslandsVertical = 2;
			break;
		case 6:
			numIslandsHorizontal = 3;
			numIslandsVertical = 2;
			break;
		case 8:
			numIslandsHorizontal = 4;
			numIslandsVertical = 2;
			break;
		default:
			throw new UnsupportedOperationException();
		}
		
		Region[][] regionsArray = new Region[width][height];
		int numRegions = width * height;
		int islandNumRegions = numRegions / numIslands;
		int islandWidth = width / numIslandsHorizontal;
		int islandHeight = height / numIslandsVertical;
		
		int regionOffsetX = 0;
		int regionOffsetY = 0;
		
		// islands
		IslandBuilder islandBuilder = new IslandBuilder(regionsArray);
		List<Island> islands = new ArrayList<Island>(numIslands);
		
		for (int i = 0; i < numIslandsHorizontal; i++) {
			for (int j = 0; j < numIslandsVertical; j++) {
				// construct island
				Island island = islandBuilder.build(regionOffsetX, regionOffsetY, islandWidth, islandHeight);
				islands.add(island);
				
				// center region positions
				for (Region region : island.regions) {
					region.position.x -= width/2;
					region.position.y -= height/2;
				}
				
				regionOffsetY += islandHeight;
			}
			
			regionOffsetX += islandWidth;
			regionOffsetY = 0;
		}
		
		// players
		List<Player> players = new ArrayList<Player>(numPlayers);
		
		// player always controls first player
		players.add(new Player(PlayerType.PLAYER));
		for (int i = 1; i < numPlayers; i++) {
			players.add(new Player(PlayerType.AI));
		}
		
		return new Overworld(width, height, regionsArray, islands, players);
	}
}

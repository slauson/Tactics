package com.slauson.tactics.model.builder;

import java.util.ArrayList;
import java.util.List;

import com.slauson.tactics.model.Island;
import com.slauson.tactics.model.IslandLayout;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.model.Player.PlayerType;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.builder.IslandBuilder.EdgeType;
import com.slauson.tactics.utils.Util;

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
		IslandLayout islandLayout = new IslandLayout(numIslands);
		
		Region[][] regionsArray = new Region[width][height];
		IslandBuilder islandBuilder = new IslandBuilder(regionsArray);

		// islands
		List<Island> islands = new ArrayList<Island>(numIslands);
		
		int islandWidth = (width - (islandLayout.layoutWidth - 1))/ islandLayout.layoutWidth;
		int islandHeight = (height - (islandLayout.layoutHeight - 1))/ islandLayout.layoutHeight;
		int regionOffsetX = 0;
		int regionOffsetY = 0;
		
		for (int column = 0; column < islandLayout.layoutWidth; column++) {
			
			for (int row = 0; row < islandLayout.layoutHeight; row++) {

				if (islandLayout.islands[column][row]) {
					
					// choose random side to not connect
					int nonConnectedSide = Util.random().nextInt(4);
					
					// don't use random side for edge islands
//					if (row == 0 || row == islandLayout.layoutHeight - 1 || column == 0 || column == islandLayout.layoutWidth - 1) {
//						nonConnectedSide = -1;
//					}
					
					// construct island
					Island island;
					
					island = islandBuilder.build(regionOffsetX, regionOffsetY, islandWidth, islandHeight, 
							EdgeType.getEdgeType(nonConnectedSide == 0, row == 0),
							EdgeType.getEdgeType(nonConnectedSide == 2, column == islandLayout.layoutWidth - 1),
							EdgeType.getEdgeType(nonConnectedSide == 3, row == islandLayout.layoutHeight - 1),
							EdgeType.getEdgeType(nonConnectedSide == 3, column == 0));
					islands.add(island);
					
					// center region positions
					for (Region region : island.regions) {
						region.position.x -= width/2;
						region.position.y -= height/2;
					}
				}
				
				regionOffsetY += islandHeight + 1;
			}
			
			regionOffsetX += islandWidth + 1;
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
	
	/*
	public Overworld buildVersion2() {
		IslandLayout islandLayout = new IslandLayout(numIslands);
		islandLayout.randomize(width, height);
		
		Region[][] regionsArray = new Region[width][height];

		// islands
		List<Island> islands = new ArrayList<Island>(numIslands);
		
		int regionOffsetX = 0;
		int regionOffsetY = 0;
		
		for (int column = 0; column < islandLayout.islands.length; column++) {
			
			regionOffsetY = 0;
			
			for (int row = 0; row < islandLayout.islands[column].length; row++) {
				
				// TODO do something better here
				regionOffsetX = 0;
				for (int i = 0; i < row; i++) {
					regionOffsetX += IslandBuilder.MIN_WIDTH + islandLayout.islands[column][i].offsetWidth + 1; 
				}
				
				// construct island
				Island island = islandLayout.islands[column][row].buildVersion2(regionsArray, regionOffsetX, regionOffsetY);
				islands.add(island);
				
				// center region positions
				for (Region region : island.regions) {
					region.position.x -= width/2;
					region.position.y -= height/2;
				}
				
				regionOffsetY += island.height + 1;
				
			}
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
	*/
	
	public Overworld buildVersion1() {
		
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
				Island island = islandBuilder.buildVersion1(regionOffsetX, regionOffsetY, islandWidth, islandHeight);
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
	
	public static void main(String[] args) {
		Overworld overworld = new OverworldBuilder(10, 10, 4, 4).build();
	}
}

package com.slauson.tactics.controller;

import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;
import com.slauson.tactics.utils.Util;

/**
 * Controls the overworld.
 * @author josh
 *
 */
public class OverworldController extends Controller {

	public Region attackingRegion, defendingRegion;
	
	private Overworld overworld;
	private Region selectedRegion;
	
	public OverworldController(Overworld overworld) {
		this.overworld = overworld;
		
		selectedRegion = null;
		attackingRegion = null;
		defendingRegion = null;
	}
	
	@Override
	public void update(float delta) {
		if (delta > MAX_DELTA) {
			delta = MAX_DELTA;
		}
	}
	
	@Override
	public Event touchDown(float worldX, float worldY) {
		// check if region is selected
		for (int i = 0; i < overworld.regions.length; i++) {
			for (int j = 0; j < overworld.regions[i].length; j++) {
				// only check non null regions
				if (overworld.regions[i][j] != null && overworld.regions[i][j].bounds.contains(worldX, worldY)) {
					
					// unselect previously selected region
					if (selectedRegion != null) {
						
						selectedRegion.selected = false;
						
						// unmark previously selected region neighbors
						for (Region neighbor : selectedRegion.neighbors) {
							neighbor.marked = false;
						}
						
						// if selected again, just unselect
						if (selectedRegion == overworld.regions[i][j]) {
							selectedRegion = null;
							return Event.NONE;
						}
						
						// region is in selected region's neighbors 
						if (selectedRegion.neighbors.contains(overworld.regions[i][j])) {
							
							// owned by other player
							if (selectedRegion.player != overworld.regions[i][j].player) {
								//attackingRegion = selectedRegion;
								//defendingRegion = overworld.regions[i][j];
								Region updatedAttackingRegion = handleBattle(selectedRegion, overworld.regions[i][j]);
								
								// keep region selected if attacker won battle
								if (updatedAttackingRegion != null) {
									selectedRegion = updatedAttackingRegion;
									selectedRegion.selected = true;
									
									// mark selected region neighbors
									for (Region neighbor : selectedRegion.neighbors) {
										// only mark regions owned by other players
										// or unoccupied regions owned by same player
										if (selectedRegion.player != neighbor.player || neighbor.unit == null) {
											neighbor.marked = true;
										}
									}
								}
								return Event.BATTLE_START;
							}
							// unoccupied region owned by same player
							else if (overworld.regions[i][j].unit == null) {
								overworld.regions[i][j].unit = selectedRegion.unit;
								selectedRegion.unit = null;
								selectedRegion = overworld.regions[i][j];
								selectedRegion.selected = true;
								
								// mark selected region neighbors
								for (Region neighbor : selectedRegion.neighbors) {
									// only mark regions owned by other players
									// or unoccupied regions owned by same player
									if (selectedRegion.player != neighbor.player || neighbor.unit == null) {
										neighbor.marked = true;
									}
								}
								
								return Event.NONE;
							}
						}
					}
					
					// only allow selecting regions with units for player who has current turn
					if (overworld.regions[i][j].unit != null && overworld.regions[i][j].player == overworld.players[overworld.playerTurnIndex]) {
					
						// select new selected region
						selectedRegion = overworld.regions[i][j];
						selectedRegion.selected = true;
						
						// mark selected region neighbors
						for (Region neighbor : selectedRegion.neighbors) {
							// only mark regions owned by other players
							// or unoccupied regions owned by same player
							if (selectedRegion.player != neighbor.player || neighbor.unit == null) {
								neighbor.marked = true;
							}
						}
					}
					
					return Event.NONE;
				}
			}
		}
		
		return Event.NONE;
	}
	

	@Override
	public void keyTyped(char character) {
		switch (character) {
		case ' ':
			// move to next turn
			overworld.playerTurnIndex++;
			if (overworld.playerTurnIndex >= overworld.players.length) {
				overworld.playerTurnIndex = 0;
			}
			break;
		}
	}

	public void battleResult(Region victorRegion, Region defeatedRegion) {
		// change color of defeated region
		defeatedRegion.player = victorRegion.player;
	}
	
	/**
	 * Handles battle between two regions.
	 * @param attackingRegion
	 * @param defendingRegion
	 * @return attacker's updated region
	 */
	private Region handleBattle(Region attackingRegion, Region defendingRegion) {
		
		// special case of no defending unit
		if (defendingRegion.unit == null) {
			// move attacking unit to defending region
			defendingRegion.player = attackingRegion.player;
			defendingRegion.unit = attackingRegion.unit;
			attackingRegion.unit = null;
			return defendingRegion;
		}
		
		// calculate attack factor for attacking region 
		float attackFactor = 1f;

		switch (attackingRegion.unit.type) {
		case CIRCLE:
			switch (defendingRegion.unit.type) {
			case CIRCLE:
				break;
			case SQUARE:
				attackFactor += Unit.UNIT_WEAKNESS_FACTOR - Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			case TRIANGLE:
				attackFactor -= Unit.UNIT_WEAKNESS_FACTOR - Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			}
			break;
		case SQUARE:
			switch (defendingRegion.unit.type) {
			case CIRCLE:
				attackFactor -= Unit.UNIT_WEAKNESS_FACTOR - Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			case SQUARE:
				break;
			case TRIANGLE:
				attackFactor += Unit.UNIT_WEAKNESS_FACTOR - Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			}
			break;
		case TRIANGLE:
			switch (defendingRegion.unit.type) {
			case CIRCLE:
				attackFactor += Unit.UNIT_WEAKNESS_FACTOR - Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			case SQUARE:
				attackFactor -= Unit.UNIT_WEAKNESS_FACTOR - Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			case TRIANGLE:
				break;
			}
			break;
		}

		// TODO simulate multiple rounds of attacks?
		
		// defender victory
		if (defendingRegion.unit.health/attackingRegion.unit.health > attackFactor) {

			// update defender health
			defendingRegion.unit.health -= attackingRegion.unit.health / (1 / attackFactor);

			// remove unit from attacking region
			attackingRegion.unit = null;
			
			return null;
		}
		// attacker victory
		else {
			
			// update attacker health
			attackingRegion.unit.health -= defendingRegion.unit.health / attackFactor;

			// move attacking unit to defending region
			defendingRegion.player = attackingRegion.player;
			defendingRegion.unit = attackingRegion.unit;
			attackingRegion.unit = null;
			
			return defendingRegion;
		}
	}

}

package com.slauson.tactics.controller;

import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Overworld.Phase;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;
import com.slauson.tactics.utils.Util;

/**
 * Controls the overworld.
 * @author josh
 *
 */
public class OverworldController extends Controller {
	
	private Overworld overworld;
	private Region selectedRegion;
	
	public OverworldController(Overworld overworld) {
		this.overworld = overworld;
		
		selectedRegion = null;
	}
	
	@Override
	public void update(float delta) {
		if (delta > MAX_DELTA) {
			delta = MAX_DELTA;
		}
		
		// check for reinforcements
	}
	
	@Override
	public boolean touchDown(float worldX, float worldY) {
		// check if region is selected
		for (Region region : overworld.regions) {
			if (region.bounds.contains(worldX, worldY)) {
				
				System.out.println("touched: " + region);
				
				switch (overworld.phase) {
				case ATTACK:
					// there's a currently selected region
					if (selectedRegion != null) {
						
						System.out.println("deselected: " + selectedRegion);
						
						// unselect previously selected region
						selectedRegion.selected = false;
						
						// unmark previously selected region neighbors
						unmarkRegionNeighbors(selectedRegion);
						
						// if selected again, just unselect
						if (selectedRegion == region) {
							selectedRegion = null;
							return true;
						}
						
						// region is in selected region's neighbors 
						if (selectedRegion.neighbors.contains(region)) {
						
							// has attack and region is owned by other player
							if (selectedRegion.unit.hasAttack && selectedRegion.player != region.player && region.unit != null) {
								Region updatedAttackingRegion = handleBattleSingleAttack(selectedRegion, region);
								
								// keep region selected if attacker won battle and can still move
								if (updatedAttackingRegion != null && updatedAttackingRegion.unit.hasMove) {
									selectedRegion = updatedAttackingRegion;
									selectedRegion.selected = true;
									
									markRegionNeighbors(selectedRegion);
								} else {
									selectedRegion = null;
								}
								
								return true;
							}
							// has move and unoccupied region
							else if (selectedRegion.unit.hasMove && region.unit == null) {
								
								// update region count if not owned by player
								if (region.player != selectedRegion.player) { 
									selectedRegion.player.regions++;
								}
								
								region.unit = selectedRegion.unit;
								region.player = selectedRegion.player;
								region.unit.hasMove = false;
								selectedRegion.unit = null;
								
								if (region.unit.hasAttack) {
									selectedRegion = region;
									selectedRegion.selected = true;
								
									markRegionNeighbors(selectedRegion);
								} else {
									selectedRegion = null;
								}
								return true;
							}
							// has move and occupied region owned by same player who can move
							else if (selectedRegion.unit.hasMove && selectedRegion.player == region.player && region.unit != null && region.unit.hasMove) {
								// swap units
								Unit temp = region.unit;
								region.unit = selectedRegion.unit;
								selectedRegion.unit = temp;
								
								region.unit.hasMove = false;
								selectedRegion.unit.hasMove = false;
								
								if (region.unit.hasAttack) {
									selectedRegion = region;
									selectedRegion.selected = true;
								
									markRegionNeighbors(selectedRegion);
								} else {
									selectedRegion = null;
								}
							} else {
								selectedRegion = null;
							}
						}
						// region is in ranged unit's ranged neighbors
						else if (selectedRegion.unit.type.isRanged() && selectedRegion.rangedNeighbors.contains(region)) {
							// has attack and region is owned by other player
							if (selectedRegion.player != region.player && selectedRegion.unit.hasAttack) {
								Region updatedAttackingRegion = handleBattleSingleAttack(selectedRegion, region);
								
								// keep region selected if attacker won battle and can still move
								if (updatedAttackingRegion != null && updatedAttackingRegion.unit.hasMove) {
									selectedRegion = updatedAttackingRegion;
									selectedRegion.selected = true;
									
									markRegionNeighbors(selectedRegion);
								} else {
									selectedRegion = null;
								}
								
								return true;
							} else {
								selectedRegion = null;
							}
						} else {
							selectedRegion = null;
						}
					}
					
					// only allow selecting regions with units for player who has current turn and are active
					if (region.unit != null &&
							region.player == overworld.activePlayer() &&
							(region.unit.hasAttack || region.unit.hasMove))
					{
					
						// select new selected region
						selectedRegion = region;
						selectedRegion.selected = true;
						
						System.out.println("selected: " + selectedRegion);
						
						markRegionNeighbors(selectedRegion);
					}
					break;
				case REINFORCE:
					if (region.player == overworld.activePlayer() &&
							region.unit != null && region.unit.health < Unit.MAX_HEALTH &&
							overworld.activePlayer().reinforcements > 0)
					{
						region.unit.health = Unit.MAX_HEALTH;
						region.marked = true;
						overworld.activePlayer().reinforcements--;
						
						System.out.println("reinforced: " + region);
					}
					break;
				}
				
				return true;
			}
		}
		
		return false;
	}
	

	@Override
	public boolean keyTyped(char character) {
		switch (character) {
		case ' ':
			// unmark previously selected region
			if (selectedRegion != null) {
				unmarkRegionNeighbors(selectedRegion);
				selectedRegion.selected = false;
				selectedRegion = null;
			}
			
			// move to next phase
			overworld.nextPhase();
			
			if (overworld.phase == Phase.ATTACK) {
				
				unmarkAllRegions();
				
				// move to next turn
				overworld.nextTurn();
				
				setPlayerUnitsActive(overworld.activePlayer());
			} else {
				markPlayerReinforcements(overworld.activePlayer());
			}
			
			return true;
		}
		
		return false;
	}

	/**
	 * Sets all player units as active.
	 * @param player
	 */
	private void setPlayerUnitsActive(Player player) {
		for (Region region : overworld.regions) {
			if (region.unit != null && region.player == player) {
				region.unit.hasAttack = true;
				region.unit.hasMove = true;
			}
		}
	}
	
	/**
	 * Marks neighboring regions depending on available actions for unit at given region.
	 * @param region
	 * @return true if unit has action
	 */
	private boolean markRegionNeighbors(Region region) {
		
		boolean hasAction = false;
		
		switch (region.unit.type) {
		case CIRCLE:
		case SQUARE:
		case TRIANGLE:
			// mark selected region neighbors
			for (Region neighbor : region.neighbors) {
				// only mark regions owned by other players for attacks
				if (region.unit.hasAttack && region.player != neighbor.player) {
					neighbor.marked = true;
					hasAction = true;
				}
				// or unoccupied regions for moves
				// or occupied regions owned by same player for moves
				else if (region.unit.hasMove && (neighbor.unit == null || (region.player == neighbor.player && neighbor.unit.hasMove)))
				{
					neighbor.marked = true;
					hasAction = true;
				}
			}
			break;
		case RANGED_CIRCLE:
		case RANGED_SQUARE:
		case RANGED_TRIANGLE:
			// mark selected region neighbors
			for (Region neighbor : region.rangedNeighbors) {
				// only mark regions owned by other players for attacks
				if (region.unit.hasAttack && region.player != neighbor.player && neighbor.unit != null) {
					neighbor.marked = true;
					hasAction = true;
				}
			}
			// mark selected region neighbors
			for (Region neighbor : region.neighbors) {
				// only mark unoccupied regions for moves
				// or occupied regions owned by same player for moves
				if (region.unit.hasMove && (neighbor.unit == null || (region.player == neighbor.player && neighbor.unit.hasMove))) {
					neighbor.marked = true;
					hasAction = true;
				}
			}
			break;
		}
		
		return hasAction;
	}
	
	/**
	 * Unmarks all neighboring regions for given region.
	 * @param region
	 */
	private void unmarkRegionNeighbors(Region region) {
		switch (region.unit.type) {
		case CIRCLE:
		case SQUARE:
		case TRIANGLE:
			for (Region neighbor : region.neighbors) {
				neighbor.marked = false;
			}
			break;
		case RANGED_CIRCLE:
		case RANGED_SQUARE:
		case RANGED_TRIANGLE:
			for (Region neighbor : region.neighbors) {
				neighbor.marked = false;
			}
			for (Region neighbor : region.rangedNeighbors) {
				neighbor.marked = false;
			}
			break;
		}
	}
	
	/**
	 * Unmarks all player regions.
	 * @param player
	 */
	private void unmarkAllRegions() {
		for (Region region : overworld.regions) {
			region.marked = false;
		}
	}
	
	/**
	 * Marks regions for player reinformcents.
	 * @param player
	 */
	private void markPlayerReinforcements(Player player) {
		for (Region region : overworld.regions) {
			if (region.player != player || (region.unit != null && region.unit.health == Unit.MAX_HEALTH)) {
				region.marked = true;
			}
		}
	}
	
	/**
	 * Handles battle between two regions.
	 * @param attackingRegion
	 * @param defendingRegion
	 * @return attacker's updated region
	 */
	private Region handleBattleSingleAttack(Region attackingRegion, Region defendingRegion) {
		
		System.out.println("battle (" + attackingRegion + ") vs (" + defendingRegion + ")");
		
		// only allow a single attack
		attackingRegion.unit.hasAttack = false;
		
		// calculate attack factor for attacking region 
		float attackFactor = 1f;

		switch (attackingRegion.unit.type) {
		case CIRCLE:
		case RANGED_CIRCLE:
			switch (defendingRegion.unit.type) {
			case CIRCLE:
			case RANGED_CIRCLE:
				attackFactor += -Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			case SQUARE:
			case RANGED_SQUARE:
				attackFactor += Unit.UNIT_WEAKNESS_FACTOR - Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			case TRIANGLE:
			case RANGED_TRIANGLE:
				attackFactor -= Unit.UNIT_WEAKNESS_FACTOR - Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			}
			break;
		case SQUARE:
		case RANGED_SQUARE:
			switch (defendingRegion.unit.type) {
			case CIRCLE:
			case RANGED_CIRCLE:
				attackFactor -= Unit.UNIT_WEAKNESS_FACTOR - Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			case SQUARE:
			case RANGED_SQUARE:
				attackFactor += -Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			case TRIANGLE:
			case RANGED_TRIANGLE:
				attackFactor += Unit.UNIT_WEAKNESS_FACTOR - Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			}
			break;
		case TRIANGLE:
		case RANGED_TRIANGLE:
			switch (defendingRegion.unit.type) {
			case CIRCLE:
			case RANGED_CIRCLE:
				attackFactor += Unit.UNIT_WEAKNESS_FACTOR - Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			case SQUARE:
			case RANGED_SQUARE:
				attackFactor -= Unit.UNIT_WEAKNESS_FACTOR - Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			case TRIANGLE:
			case RANGED_TRIANGLE:
				attackFactor += -Unit.UNIT_WEAKNESS_RANDOM_FACTOR + 2*Unit.UNIT_WEAKNESS_RANDOM_FACTOR*Util.random().nextFloat();
				break;
			}
			break;
		}
		
		float defendFactor = 2 - attackFactor;

		// calculate how much damage each region can do
		float attackerAttackDamage = attackFactor * attackingRegion.unit.health;
		float defenderAttackDamage = defendFactor * defendingRegion.unit.health;
		
		// special case for ranged units
		if ((attackingRegion.unit.type.isRanged() && !defendingRegion.unit.type.isRanged()) ||
				defendingRegion.unit.type.isRanged() && !attackingRegion.unit.type.isRanged())
		{
			defenderAttackDamage = Float.MIN_VALUE; // use min value instead of 0 to allow division below
		}
		
		// update health
		attackingRegion.unit.health -= defenderAttackDamage;
		defendingRegion.unit.health -= attackerAttackDamage;
		
		System.out.println("battle result (" + attackingRegion.unit + ") (" + defendingRegion.unit + ")");
		
		// special case of both units defeated
		if (defendingRegion.unit.health <= 0 && attackingRegion.unit.health <= 0) {

			// this is unlikely
			
			// determine percentage of attack that went wasted
			float attackerOverkillFactor = -attackingRegion.unit.health / defenderAttackDamage;
			float defenderOverkillFactor = -defendingRegion.unit.health / attackerAttackDamage;
			
			// defender defeated first
			if (defenderOverkillFactor >= attackerOverkillFactor) {
				// update attacker health
				attackingRegion.unit.health += (defenderOverkillFactor * defenderAttackDamage);
			}
			// attacker defeated first
			else {
				// update defender health
				defendingRegion.unit.health += (attackerOverkillFactor * attackerAttackDamage);
			}
		}
		
		// attacker victory
		if (defendingRegion.unit.health <= 0) {

			attackingRegion.player.regions++;
			defendingRegion.player.regions--;
			defendingRegion.player.units--;
			
			// normal attacking unit
			if (!attackingRegion.unit.type.isRanged()) {
				defendingRegion.player = attackingRegion.player;
				defendingRegion.unit = attackingRegion.unit;
				attackingRegion.unit = null;
				
				return defendingRegion;
			}
			// ranged attacking unit
			else {
				defendingRegion.unit = null;
				return null;
			}
		}
		// defender victory
		else if (attackingRegion.unit.health <= 0) {
			// remove unit from attacking region
			attackingRegion.unit = null;
			
			attackingRegion.player.units--;
			
			return null;
		}
		// no victory
		else {
			// do nothing
			return null;
		}
	}		
}

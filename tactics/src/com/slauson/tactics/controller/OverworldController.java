package com.slauson.tactics.controller;

import com.slauson.tactics.model.Island;
import com.slauson.tactics.model.Neighbor;
import com.slauson.tactics.model.Neighbor.NeighborType;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Overworld.Phase;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;
import com.slauson.tactics.model.Unit.UnitType;
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
	}
	
	@Override
	public boolean touchDown(float worldX, float worldY) {
		
		Region region = overworld.getContainingRegion(worldX, worldY);
		if (region != null) {
				
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
					NeighborType neighborType = selectedRegion.neighbors.getNeighborType(region);
					if (neighborType != null) {
						
						// attack
						if (selectedRegion.unit.hasAttack && selectedRegion.player != region.player && region.unit != null &&
								((selectedRegion.unit.type.isRanged() && neighborType.isRanged()) || (!selectedRegion.unit.type.isRanged() && !neighborType.isRanged())))
						{
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
						// move (unoccupied region)
						else if (selectedRegion.unit.hasMove && region.unit == null && neighborType != NeighborType.RANGED) {
							// update region count if not owned by player
							if (region.player != selectedRegion.player) { 
								selectedRegion.player.regions++;
							}
							
							region.unit = selectedRegion.unit;
							region.player = selectedRegion.player;
							region.unit.hasMove = false;
							selectedRegion.unit = null;
							
							// keep region selected if can still attack
							if (region.unit.hasAttack) {
								selectedRegion = region;
								selectedRegion.selected = true;
							
								markRegionNeighbors(selectedRegion);
							} else {
								selectedRegion = null;
							}
							return true;
						}
						// move (player owned region)
						else if (selectedRegion.unit.hasMove && selectedRegion.player == region.player && region.unit != null&& region.unit.hasMove && neighborType != NeighborType.RANGED) {
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
				if (region.player == overworld.activePlayer()) {
					
					// existing unit
					if (overworld.activePlayer().reinforcements > 0 && region.unit != null && region.unit.health < Unit.MAX_HEALTH) {
						
						// mark previously selected region
						if (selectedRegion != null) {
							selectedRegion.marked = true;
							selectedRegion.selected = false;
						}
						
						region.unit.health = Unit.MAX_HEALTH;
						region.marked = true;
						selectedRegion = null;
						overworld.activePlayer().reinforcements--;
					}
					// new unit
					else if (overworld.activePlayer().reinforcements > 0 && region.unit == null) {
						
						// mark previously selected region
						if (selectedRegion != null) {
							selectedRegion.marked = true;
							selectedRegion.selected = false;
						}
						
						region.unit = new Unit(UnitType.values()[0], Unit.MAX_HEALTH);
						selectedRegion = region;
						selectedRegion.selected = true;
						overworld.activePlayer().reinforcements--;
					}
					// switch unit type of new unit
					else if (region == selectedRegion) {
						region.unit.type = region.unit.type.next();
					}
					
					System.out.println("reinforced: " + region);
				}
				break;
			}
			
			return true;
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
				// handle reinforcements
				updatePlayerReinforcements(overworld.activePlayer());
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
			// mark region neighbors
			for (Neighbor neighbor : region.neighbors) {
				// mark unoccupied direct or ranged inter island regions
				// or occupied regions owned by same player for moves
				if (neighbor.type.isMovable() && region.unit.hasMove && 
						(neighbor.region.unit == null || (region.player == neighbor.region.player && neighbor.region.unit.hasMove)))
				{
					neighbor.region.marked = true;
					hasAction = true;
				}
				// only mark direct regions owned by other players with units for attacks
				else if (neighbor.type == NeighborType.DIRECT && region.unit.hasAttack &&
						region.player != neighbor.region.player && neighbor.region.unit != null)
				{
					neighbor.region.marked = true;
					hasAction = true;
				}
				
			}
			break;
		case RANGED_CIRCLE:
		case RANGED_SQUARE:
		case RANGED_TRIANGLE:
			// mark region neighbors
			for (Neighbor neighbor : region.neighbors) {
				// mark unoccupied direct or ranged inter island regions
				// or occupied regions owned by same player for moves
				if (neighbor.type.isMovable() && region.unit.hasMove && 
						(neighbor.region.unit == null || (region.player == neighbor.region.player && neighbor.region.unit.hasMove)))
				{
					neighbor.region.marked = true;
					hasAction = true;
				}
				// only mark ranged regions owned by other players with units for attacks
				else if (neighbor.type.isRanged() && region.unit.hasAttack &&
						region.player != neighbor.region.player && neighbor.region.unit != null)
				{
					neighbor.region.marked = true;
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
		for (Neighbor neighbor : region.neighbors) {
			neighbor.region.marked = false;
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
	 * Marks regions for player reinforcements.
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
	 * Updates reinforcements for given player.
	 * @param player
	 */
	private void updatePlayerReinforcements(Player player) {
		
		int newReinforcements = 0;
		
		for (Island island : overworld.islands) {
			
			boolean playerOwnsIsland = true;
			
			for (int i = 0; i < island.regions.size() && playerOwnsIsland; i++) {
				playerOwnsIsland &= (island.regions.get(i).player == player);
			}
			
			if (playerOwnsIsland) {
				newReinforcements++;
			}
		}
		
		player.reinforcements += newReinforcements;
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

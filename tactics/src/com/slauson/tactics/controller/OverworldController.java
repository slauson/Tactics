package com.slauson.tactics.controller;

import com.slauson.tactics.model.Neighbor.NeighborType;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Overworld.Phase;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;
import com.slauson.tactics.model.Unit.UnitType;
import com.slauson.tactics.utils.BattleUtils;
import com.slauson.tactics.utils.PlayerUtils;
import com.slauson.tactics.utils.RegionUtils;

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
					RegionUtils.unmarkRegionNeighbors(selectedRegion);
					
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
							Region updatedAttackingRegion = BattleUtils.handleBattle(selectedRegion, region);
							
							// keep region selected if attacker won battle and can still move
							if (updatedAttackingRegion != null && updatedAttackingRegion.unit.hasMove) {
								selectedRegion = updatedAttackingRegion;
								selectedRegion.selected = true;
								
								RegionUtils.markRegionNeighbors(selectedRegion);
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
							
								RegionUtils.markRegionNeighbors(selectedRegion);
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
							
								RegionUtils.markRegionNeighbors(selectedRegion);
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
					
					RegionUtils.markRegionNeighbors(selectedRegion);
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
				RegionUtils.unmarkRegionNeighbors(selectedRegion);
				selectedRegion.selected = false;
				selectedRegion = null;
			}
			
			// move to next phase
			overworld.nextPhase();
			
			if (overworld.phase == Phase.ATTACK) {
				
				RegionUtils.unmarkAllRegions(overworld);
				
				// move to next turn
				overworld.nextTurn();
				
				PlayerUtils.setPlayerUnitsActive(overworld, overworld.activePlayer());
			} else {
				// handle reinforcements
				PlayerUtils.updatePlayerReinforcements(overworld, overworld.activePlayer());
				PlayerUtils.markPlayerReinforcements(overworld, overworld.activePlayer());
			}
			
			return true;
		}
		
		return false;
	}
}

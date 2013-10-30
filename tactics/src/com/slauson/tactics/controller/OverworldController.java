package com.slauson.tactics.controller;

import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Region;

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
						
						// region is in selected region's neighbors and owned by other player
						if (selectedRegion.player != overworld.regions[i][j].player && selectedRegion.neighbors.contains(overworld.regions[i][j])) {
							// change color of new region
							overworld.regions[i][j].player = selectedRegion.player;
							attackingRegion = selectedRegion;
							defendingRegion = overworld.regions[i][j];
							selectedRegion = null;
							return Event.BATTLE_START;
						}
					}
					
					// select new selected region
					selectedRegion = overworld.regions[i][j];
					selectedRegion.selected = true;
					
					// mark selected region neighbors
					for (Region neighbor : selectedRegion.neighbors) {
						// only mark regions owned by other players
						if (selectedRegion.player != neighbor.player) {
							neighbor.marked = true;
						}
					}
					
					return Event.NONE;
				}
			}
		}
		
		return Event.NONE;
	}
}

package com.slauson.tactics.controller;

import com.badlogic.gdx.graphics.Color;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Region;

/**
 * Controls the overworld.
 * @author josh
 *
 */
public class OverworldController {

	private static final float MAX_DELTA = 0.1f;
	
	private Overworld overworld;
	private Region selectedRegion;
	
	public OverworldController(Overworld overworld) {
		this.overworld = overworld;
		
		selectedRegion = null;
	}
	
	public void update(float delta) {
		if (delta > MAX_DELTA) {
			delta = MAX_DELTA;
		}
		
	}
	
	public void touchDown(float worldX, float worldY) {
		// check if region is selected
		for (int i = 0; i < overworld.regions.length; i++) {
			for (int j = 0; j < overworld.regions[i].length; j++) {
				if (overworld.regions[i][j].bounds.contains(worldX, worldY)) {
					
					if (selectedRegion != null) {
						selectedRegion.resetColor();
					}
					
					selectedRegion = overworld.regions[i][j];
					selectedRegion.color = Color.BLUE;
				}
			}
		}
	}
}

package com.slauson.tactics.controller;

/**
 * Controls the overworld.
 * @author josh
 *
 */
public class OverworldController {

	private static final float MAX_DELTA = 0.1f;
	
	public OverworldController() {
		
	}
	
	public void update(float delta) {
		if (delta > MAX_DELTA) {
			delta = MAX_DELTA;
		}
		
	}
}

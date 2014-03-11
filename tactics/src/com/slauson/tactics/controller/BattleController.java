package com.slauson.tactics.controller;

import com.slauson.tactics.model.Battle;

public class BattleController extends Controller {

	private Battle battle;
	
	public BattleController(Battle battle) {
		this.battle = battle;
	}
	
	@Override
	public void update(float delta) {
		if (delta > MAX_DELTA) {
			delta = MAX_DELTA;
		}
		
		if (!paused) {
			
			// update battle time
			
			// adjust health
		}
	}

	@Override
	public boolean touchDown(float worldX, float worldY) {
		// ignore
		return false;
	}

}

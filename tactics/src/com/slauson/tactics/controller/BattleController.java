package com.slauson.tactics.controller;

import com.slauson.tactics.model.Battle;

public class BattleController extends Controller {

	private Battle battle;
	
	public BattleController(Battle battle) {
		this.battle = battle;
	}

	@Override
	public void update(float delta) {
		return;
	}

	@Override
	public Event touchDown(float worldX, float worldY) {
		return Event.BATTLE_END;
	}

	@Override
	public void keyTyped(char character) {
		// TODO Auto-generated method stub
	}
}

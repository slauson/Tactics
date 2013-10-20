package com.slauson.tactics.screen;

import com.slauson.tactics.TacticsGame;
import com.slauson.tactics.controller.BattleController;
import com.slauson.tactics.model.Battle;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.view.BattleRenderer;

public class BattleScreen extends Screen {
	
	private Battle battle;
	
	public BattleScreen(TacticsGame game, Region attackingRegion, Region defendingRegion) {
		super(game);
		
		this.game = game;
		
		battle = new Battle(attackingRegion, defendingRegion);
	}
	
	@Override
	public void show() {
		super.show();
		renderer = new BattleRenderer(battle);
		controller = new BattleController(battle);
	}
}

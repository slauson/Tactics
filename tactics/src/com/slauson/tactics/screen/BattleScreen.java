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
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		super.touchDown(screenX, screenY, pointer, button);
		
		switch(controller.touchDown(lastMousePressWorldPosition.x, lastMousePressWorldPosition.y)) {
		case BATTLE_END:
			// go to battle screen
			game.showOverworld();
			break;
		default:
			// do nothing
			break;
		}
		
		return true;
	}
}

package com.slauson.tactics.screen;

import com.slauson.tactics.TacticsGame;
import com.slauson.tactics.controller.OverworldController;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.view.OverworldRenderer;

/**
 * Screen for the overworld.
 * @author josh
 *
 */
public class OverworldScreen extends Screen {

	private static final int NUM_PLAYERS = 4;
	
	private Overworld overworld;
	
	public OverworldScreen(TacticsGame game) {
		super(game);
		
		overworld = new Overworld(NUM_PLAYERS);
	}
	
	@Override
	public void show() {
		super.show();
		renderer = new OverworldRenderer(overworld);
		controller = new OverworldController(overworld);
	}
		
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		super.touchDown(screenX, screenY, pointer, button);
		
		switch(controller.touchDown(lastMousePressWorldPosition.x, lastMousePressWorldPosition.y)) {
		case BATTLE:
			// go to battle screen
			game.showBattle(((OverworldController)controller).attackingRegion, ((OverworldController)controller).defendingRegion);
			break;
		case NONE:
			// do nothing
			break;
		}
		
		return true;
	}
}

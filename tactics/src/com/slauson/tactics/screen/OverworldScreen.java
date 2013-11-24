package com.slauson.tactics.screen;

import com.slauson.tactics.TacticsGame;
import com.slauson.tactics.controller.OverworldController;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.builder.OverworldBuilder;
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
		
		overworld = new OverworldBuilder(9, 7, 4, 4).build();
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
		controller.touchDown(lastMousePressWorldPosition.x, lastMousePressWorldPosition.y);
		return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		controller.keyTyped(character);
		super.keyTyped(character);
		return true;
	}
}

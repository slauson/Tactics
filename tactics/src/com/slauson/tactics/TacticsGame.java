package com.slauson.tactics;

import com.badlogic.gdx.Game;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.screen.OverworldScreen;

/**
 * Main class for the game.
 * @author josh
 *
 */
public class TacticsGame extends Game {
	
	private OverworldScreen overworldScreen;
	
	@Override
	public void create() {
		overworldScreen = new OverworldScreen(this);
		showOverworld();
	}
	
	/**
	 * Displays the overworld screen.
	 */
	public void showOverworld() {
		setScreen(overworldScreen);
	}
	
	/**
	 * Displays the overworld screen.
	 */
	public void showOverworld(Region victorRegion, Region defeatedRegion) {
		setScreen(overworldScreen);
	}
	
}

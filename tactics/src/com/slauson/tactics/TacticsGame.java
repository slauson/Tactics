package com.slauson.tactics;

import com.badlogic.gdx.Game;
import com.slauson.tactics.screen.OverworldScreen;

/**
 * Main class for the game.
 * @author josh
 *
 */
public class TacticsGame extends Game {
	
	@Override
	public void create() {
		setScreen(new OverworldScreen());
	}
}

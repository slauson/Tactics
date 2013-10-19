package com.slauson.tactics;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.screen.BattleScreen;
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
	 * Displays the battle screen.
	 * @param attacker attacking region
	 * @param defender defending region
	 */
	public void showBattle(Region attacker, Region defender, TextureRegion texture) {
		setScreen(new BattleScreen(attacker, defender, texture, this));
	}
}

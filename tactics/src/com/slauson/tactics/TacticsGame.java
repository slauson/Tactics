package com.slauson.tactics;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.screen.BattleScreen;
import com.slauson.tactics.screen.OverworldScreen;
import com.slauson.tactics.view.TransitionScreen;

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
	 * @param attackingRegion attacking region
	 * @param defendingRegion defending region
	 */
	public void showBattle(Region attackingRegion, Region defendingRegion) {
		BattleScreen battleScreen = new BattleScreen(this, attackingRegion, defendingRegion);
		setScreen(new TransitionScreen(this, battleScreen));
	}
}

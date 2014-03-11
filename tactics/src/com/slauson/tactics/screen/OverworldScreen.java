package com.slauson.tactics.screen;

import com.slauson.tactics.TacticsGame;
import com.slauson.tactics.controller.BattleController;
import com.slauson.tactics.controller.OverworldController;
import com.slauson.tactics.model.Battle;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.builder.OverworldBuilder;
import com.slauson.tactics.view.BattleRenderer;
import com.slauson.tactics.view.OverworldRenderer;

/**
 * Screen for the overworld.
 * @author josh
 *
 */
public class OverworldScreen extends Screen {

	private static final int NUM_PLAYERS = 2;
	
	private Overworld overworld;
	private Battle battle;
	
	public OverworldScreen(TacticsGame game) {
		super(game);
		
		overworld = new OverworldBuilder(7, 7, NUM_PLAYERS, 4).build();
		battle = new Battle();
	}
	
	@Override
	public void show() {
		super.show();
		renderers.add(new OverworldRenderer(overworld, battle));
		renderers.add(new BattleRenderer(battle));
		controllers.add(new OverworldController(overworld, battle));
		controllers.add(new BattleController(battle));
	}
}

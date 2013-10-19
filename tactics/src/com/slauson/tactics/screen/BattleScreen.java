package com.slauson.tactics.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slauson.tactics.TacticsGame;
import com.slauson.tactics.controller.BattleController;
import com.slauson.tactics.model.Battle;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.view.BattleRenderer;

public class BattleScreen extends Screen {
	
	private Battle battle;
	
	private TextureRegion texture;
	
	public BattleScreen(Region attacker, Region defender, TextureRegion texture, TacticsGame game) {
		super(game);
		
		this.texture = texture;
		this.game = game;
		
		battle = new Battle(attacker, defender);
	}
	
	@Override
	public void show() {
		super.show();
		renderer = new BattleRenderer(battle, texture);
		controller = new BattleController(battle);
	}
}

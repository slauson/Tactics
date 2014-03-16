package com.slauson.tactics.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.slauson.tactics.model.Battle;

public class BattleRenderer extends Renderer {

	//private static final float SPIN_DURATION = 1f * Battle.DURATION / 2;
	//private static final float SHAKE_DURATION = 0.5f * Battle.DURATION / 2;
	
	private ShapeRenderer renderer;
	private Battle battle;
	
	public BattleRenderer(Battle battle) {
		super();
		this.battle = battle;
		
		renderer = new ShapeRenderer();
	}
	
	@Override
	public void render(OrthographicCamera camera, float delta, boolean debug) {
		
		renderer.setProjectionMatrix(camera.combined);
		
		if (battle.active()) {
			
			// draw units
			renderer.setColor(Color.BLACK);
			switch (battle.currentPhase()) {
			case ATTACKER_ATTACK:
			case DEFENDER_ATTACK:
			case ATTACKER_DAMAGE:
			case DEFENDER_DAMAGE:
			case UPDATE_HEALTH:
				drawUnit(renderer, battle.attackingRegion);
				renderer.setColor(battle.defendingRegion.player.color.r*NON_TURN_COLOR_FACTOR,
						battle.defendingRegion.player.color.g*NON_TURN_COLOR_FACTOR,
						battle.defendingRegion.player.color.b*NON_TURN_COLOR_FACTOR,
						1);
				drawUnit(renderer, battle.defendingRegion);
				break;
			case TAKEOVER:
				drawUnit(renderer, battle.attackingRegion);
				break;
			default:
				// do nothing
			}
		}
	}
}

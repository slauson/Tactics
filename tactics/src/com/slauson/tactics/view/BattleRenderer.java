package com.slauson.tactics.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.slauson.tactics.model.Battle;

public class BattleRenderer extends Renderer {

	//private static final float SPIN_DURATION = 1f * Battle.DURATION / 2;
	//private static final float SHAKE_DURATION = 0.5f * Battle.DURATION / 2;
	private static final float SHAKE_FACTOR = 0.05f;
	
	private float counter;
	
	private ShapeRenderer renderer;
	
	private Battle battle;
	
	public BattleRenderer(Battle battle) {
		super();
		this.battle = battle;
		
		renderer = new ShapeRenderer();
	}
	
	@Override
	public void render(OrthographicCamera camera, float delta, boolean debug) {
		
		counter += delta;
		//counter %= (SPIN_DURATION * SHAKE_DURATION);
		
		renderer.setProjectionMatrix(camera.combined);
		
		if (battle.active()) {
			
			float rotation = 0;
			float offset = 0;
			
			// calculate offset/spin
			switch (battle.currentPhase()) {
			case ATTACKER_ATTACK:
			case DEFENDER_ATTACK:
				 rotation = battle.percentPhaseComplete() * 360;
				break;
			case ATTACKER_DAMAGE:
			case DEFENDER_DAMAGE:
				// + to -
				if (battle.percentPhaseComplete() < 0.5) {
					offset = ((0.25f - battle.percentPhaseComplete()) / 0.25f)
							* SHAKE_FACTOR * battle.defendingRegion.bounds.width;
				}
				// - to +
				else {
					offset = -((0.75f - battle.percentPhaseComplete()) / 0.25f)
							* SHAKE_FACTOR * battle.defendingRegion.bounds.width;
				}
				break;
			default:
				// do nothing
			}
			
			// draw units
			renderer.setColor(Color.BLACK);
			
			switch (battle.currentPhase()) {
			case ATTACKER_ATTACK:
				drawUnit(renderer, battle.attackingRegion, 0, 0, rotation);
				drawUnit(renderer, battle.defendingRegion);
				break;
			case DEFENDER_ATTACK:
				drawUnit(renderer, battle.attackingRegion);
				drawUnit(renderer, battle.defendingRegion, 0, 0, rotation);
				break;
			case ATTACKER_DAMAGE:
				drawUnit(renderer, battle.attackingRegion, offset, 0, 0);
				drawUnit(renderer, battle.defendingRegion);
				break;
			case DEFENDER_DAMAGE:
				drawUnit(renderer, battle.attackingRegion);
				drawUnit(renderer, battle.defendingRegion, offset, 0, 0);
				break;
			case UPDATE_HEALTH:
				drawUnit(renderer, battle.attackingRegion);
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

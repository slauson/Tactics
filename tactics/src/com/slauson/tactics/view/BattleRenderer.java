package com.slauson.tactics.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.slauson.tactics.model.Battle;

public class BattleRenderer extends Renderer {

	private static final float SPIN_DURATION = 1f * Battle.DURATION / 2;
	private static final float SHAKE_DURATION = 0.5f * Battle.DURATION / 2;
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
			
			float rotation;
			float offset;
			
			if (battle.percentComplete() < 0.5f) {
				 rotation = ((counter % SPIN_DURATION) / SPIN_DURATION) * 360;
			} else {
				rotation = 0;
			}
			
			float shakeCounter = counter % SHAKE_DURATION;
			
			if (battle.percentComplete() > 0.5f) {
				// + to -
				if (shakeCounter < SHAKE_DURATION / 2) {
					offset = ((SHAKE_DURATION / 4) - (shakeCounter)) / (SHAKE_DURATION / 4)
							* SHAKE_FACTOR * battle.defendingRegion.bounds.width;
				}
				// - to +
				else {
					offset = -((3 * SHAKE_DURATION / 4) - (shakeCounter)) / (SHAKE_DURATION / 4)
							* SHAKE_FACTOR * battle.defendingRegion.bounds.width;
				}
			} else {
				offset = 0;
			}
			
			renderer.setColor(Color.BLACK);
			
			// draw attacking unit
			drawUnit(renderer, battle.attackingRegion, 0, 0, rotation);
			
			// draw defending unit
			drawUnit(renderer, battle.defendingRegion, offset, 0, 0);
		}
	}
}

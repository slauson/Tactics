package com.slauson.tactics.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.slauson.tactics.model.Battle;
import com.slauson.tactics.model.Unit;

public class BattleRenderer extends Renderer {

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
		
//		Gdx.gl.glClearColor(0f, 0f, 0f, 0.5f);
//		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float x, y;
		float yIncrement = -3;
		float size = 1;
		int numSegments = 20;
		
		y = -yIncrement - size/2;
		x = -3;
		
		// draw attacking units on left
		renderer.setColor(battle.attackingRegion.player.color);
		for (Unit unit : battle.attackingUnits) {
			switch (unit.type) {
			case CIRCLE:
				renderer.begin(ShapeType.FilledCircle);
				renderer.filledCircle(x, y, size/2, numSegments);
				renderer.end();
				break;
			case SQUARE:
				renderer.begin(ShapeType.FilledRectangle);
				renderer.filledRect(x, y, size, size);
				renderer.end();
				break;
			case TRIANGLE:
				renderer.begin(ShapeType.FilledTriangle);
				renderer.filledTriangle(x, y, x + size/2, y + size, x + size, y);
				renderer.end();
				break;
			}
			
			y+= yIncrement;
		}
		
		y = -yIncrement - size/2;
		x = 3;
		
		// draw defending units on left
		renderer.setColor(battle.defendingRegion.player.color);
		for (Unit unit : battle.defendingUnits) {
			switch (unit.type) {
			case CIRCLE:
				renderer.begin(ShapeType.FilledCircle);
				renderer.filledCircle(x, y, size/2, numSegments);
				renderer.end();
				break;
			case SQUARE:
				renderer.begin(ShapeType.FilledRectangle);
				renderer.filledRect(x, y, size, size);
				renderer.end();
				break;
			case TRIANGLE:
				renderer.begin(ShapeType.FilledTriangle);
				renderer.filledTriangle(x, y, x + size/2, y + size, x + size, y);
				renderer.end();
				break;
			}
			
			y+= yIncrement;
		}
		
		super.render(camera, delta, debug);
	}
	
	
}

package com.slauson.tactics.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.slauson.tactics.model.Battle;

public class BattleRenderer extends Renderer {

	private Battle battle;
	
	public BattleRenderer(Battle battle) {
		this.battle = battle;
	}
	
	@Override
	public void render(OrthographicCamera camera, float delta, boolean debug) {
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 0.5f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//super.render(camera, delta, debug);
	}
}

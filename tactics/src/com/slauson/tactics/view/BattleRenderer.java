package com.slauson.tactics.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slauson.tactics.model.Battle;

public class BattleRenderer extends Renderer {

	private Battle battle;
	private TextureRegion texture;
	
	public BattleRenderer(Battle battle, TextureRegion texture) {
		this.battle = battle;
		this.texture = texture;
	}
	
	@Override
	public void render(OrthographicCamera camera, float delta, boolean debug) {
		
		spriteBatch.begin();
		spriteBatch.draw(texture, 0, 0);
		spriteBatch.end();
		
		super.render(camera, delta, debug);
	}
}

package com.slauson.tactics.view;

import com.badlogic.gdx.Screen;
import com.slauson.tactics.TacticsGame;
import com.slauson.tactics.screen.BattleScreen;

public class TransitionScreen implements Screen {

	private static final float DURATION = 2f; 
	
	private TacticsGame game;
	private BattleScreen battleScreen;
	
	private int width, height;
	
	private float elapsedTime;
	
	public TransitionScreen(TacticsGame game, BattleScreen battleScreen) {
		this.game = game;
		this.battleScreen = battleScreen;
		
		elapsedTime = 0;
	}

	@Override
	public void render(float delta) {
		elapsedTime += delta;
		
		// go back to overworld
		if (elapsedTime > DURATION) {
			game.showOverworld();
		}
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}

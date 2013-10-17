package com.slauson.tactics.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.slauson.tactics.controller.OverworldController;
import com.slauson.tactics.view.OverworldRenderer;

/**
 * Screen for the overworld.
 * @author josh
 *
 */
public class OverworldScreen implements Screen, InputProcessor {

	private OverworldRenderer renderer;
	private OverworldController controller;
	
	public OverworldScreen() {
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		controller.update(delta);
		renderer.render();
	}
	
	@Override
	public void resize(int width, int height) {
		renderer.resize(width, height);
	}

	@Override
	public void show() {
		renderer = new OverworldRenderer();
		controller = new OverworldController();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
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
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		renderer.keyTyped(character);
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		renderer.touchDown(screenX, screenY, pointer, button);		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		renderer.touchUp(screenX, screenY, pointer, button);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		renderer.touchDragged(screenX, screenY, pointer);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// 1: mouse scroll down
		// -1: mouse scroll up
		return true;
	}
	
}

package com.slauson.tactics.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.slauson.tactics.TacticsGame;
import com.slauson.tactics.controller.Controller;
import com.slauson.tactics.view.Renderer;

public abstract class Screen implements com.badlogic.gdx.Screen, InputProcessor {

	protected static final float PIXELS_PER_METER = 32;
	
	private static final float ZOOM_INCREMENT = 0.1f;
	private static final float MIN_ZOOM = 0.25f;
	private static final float MAX_ZOOM = 2f;
	
	protected TacticsGame game;
	
	private static final boolean DEBUG = true;
	
	protected int width, height;
	
	protected List<Renderer> renderers;
	protected List<Controller> controllers;
	
	protected OrthographicCamera camera;
	
	protected int mouseButtonDown;
	protected float zoomAmount;
	
	protected final Vector3 lastMousePressWorldPosition;
	protected final Vector3 lastMouseScreenPosition;
	
	public Screen(TacticsGame game) {
		this.game = game;
		
		camera = new OrthographicCamera();
		
		lastMousePressWorldPosition = new Vector3();
		lastMouseScreenPosition = new Vector3();
		
		zoomAmount = 1f;
	}
	
	@Override
	public void render(float delta) {
		
		for (Controller controller : controllers) {
			controller.update(delta);
		}
		
		for (Renderer renderer : renderers) {
			renderer.render(camera, delta, DEBUG);
		}
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		
		System.out.println("resize: " + width + ", " + height);
		
		// save old position as setToOrtho resets position
		Vector3 oldCameraPosition = new Vector3(camera.position);

		// scale visible world coordinates based on screen coordinates
		camera.setToOrtho(false, width/PIXELS_PER_METER, height/PIXELS_PER_METER);
		
		// move camera back to its original position
		camera.translate(oldCameraPosition.x - camera.position.x, oldCameraPosition.y - camera.position.y);
		camera.update();
		
		System.out.println("camera at: " + camera.position.x + ", " + camera.position.y);
		
		for (Renderer renderer : renderers) {
			renderer.resize(width, height);
		}
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
		renderers = new ArrayList<Renderer>();
		controllers = new ArrayList<Controller>();
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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//		System.out.println("touchDown: " + screenX + ", " + screenY + ", " + pointer + ", " + button);
		
		lastMousePressWorldPosition.set(screenX, screenY, 0);
		camera.unproject(lastMousePressWorldPosition);
		mouseButtonDown = button;
		
		// set this here so we use the updated position in touchDragged
		lastMouseScreenPosition.set(screenX, screenY, 0);
		
		// update controllers
		for (Controller controller : controllers) {
			controller.touchDown(lastMousePressWorldPosition.x, lastMousePressWorldPosition.y);
		}
		
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mouseButtonDown = 0;
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		System.out.println("touchDragged: " + screenX + ", " + screenY + ", " + pointer);
		
		if (mouseButtonDown == Input.Buttons.RIGHT) {
			camera.translate((lastMouseScreenPosition.x - screenX)/PIXELS_PER_METER, (screenY - lastMouseScreenPosition.y)/PIXELS_PER_METER);
			camera.update();
		}
		
		lastMouseScreenPosition.set(screenX, screenY, 0);
		return true;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		switch (amount) {
		case -1:
			if (zoomAmount - ZOOM_INCREMENT >= MIN_ZOOM) {
				zoomAmount -= ZOOM_INCREMENT;
				camera.zoom = zoomAmount;
				camera.update();
			}
			break;
		case 1:
			if (zoomAmount + ZOOM_INCREMENT <= MAX_ZOOM) {
				zoomAmount += ZOOM_INCREMENT;
				camera.zoom = zoomAmount;
				camera.update();
			}
			break;
		}
		return true;
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
		switch (character) {
		case 'z':
			zoomAmount += ZOOM_INCREMENT;
			camera.zoom = zoomAmount;
			camera.update();
			break;
		case 'x':
			zoomAmount -= ZOOM_INCREMENT;
			camera.zoom = zoomAmount;
			camera.update();
			break;
		}
		
		// controller updates
		for (Controller controller : controllers) {
			controller.keyTyped(character);
		}
		
		return true;
	}
}

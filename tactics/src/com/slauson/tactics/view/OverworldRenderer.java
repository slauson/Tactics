package com.slauson.tactics.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

/**
 * Renders the overworld.
 * @author josh
 *
 */
public class OverworldRenderer {
	private static final int GRID_SIZE = 10;
	
	private static final float PIXELS_PER_METER = 32;
	
	private static final float ZOOM_INCREMENT = 0.02f;

	private OrthographicCamera camera;
	
	private ShapeRenderer renderer = new ShapeRenderer();
	
	private int width, height;
	
	private final Vector3 lastMousePressWorldPosition;
	private final Vector3 lastMouseScreenPosition;
	
	private int mouseButtonDown;
	private float zoomAmount = 1f;
	
	
	public OverworldRenderer() {
		camera = new OrthographicCamera();
		lastMousePressWorldPosition = new Vector3();
		lastMouseScreenPosition = new Vector3();
	}
	
	/**
	 * Handles window resize.
	 * @param width
	 * @param height
	 */
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
		
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Renders the overworld.
	 */
	public void render() {
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeType.Rectangle);
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				renderer.setColor(new Color((i*1.0f/GRID_SIZE), 0, (GRID_SIZE-i*1.0f)/GRID_SIZE, 1));
				renderer.rect(i, j, 1, 1);
			}
		}
		renderer.end();
	}
	
	/**
	 * Handles key type events.
	 * @param character
	 */
	public void keyTyped(char character) {
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
	}
	
	/**
	 * Handles touch down events.
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 * @param button
	 */
	public void touchDown(int screenX, int screenY, int pointer, int button) {
		
		System.out.println("touchDown: " + screenX + ", " + screenY + ", " + pointer + ", " + button);
		
		lastMousePressWorldPosition.set(screenX, screenY, 0);
		camera.unproject(lastMousePressWorldPosition);
		mouseButtonDown = button;
		
		// set this here so we use the updated position in touchDragged
		lastMouseScreenPosition.set(screenX, screenY, 0);
	}
	
	/**
	 * Handles touch up events.
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 * @param button
	 */
	public void touchUp(int screenX, int screenY, int pointer, int button) {
		mouseButtonDown = 0;
	}
	
	/**
	 * Handles touch drag events.
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 */
	public void touchDragged(int screenX, int screenY, int pointer) {
		
		System.out.println("touchDragged: " + screenX + ", " + screenY + ", " + pointer);
		
		if (mouseButtonDown == Input.Buttons.RIGHT) {
			camera.translate((lastMouseScreenPosition.x - screenX)/OverworldRenderer.PIXELS_PER_METER, (screenY - lastMouseScreenPosition.y)/OverworldRenderer.PIXELS_PER_METER);
			camera.update();
		}
		
		lastMouseScreenPosition.set(screenX, screenY, 0);
	}

}

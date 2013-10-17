package com.slauson.tactics.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * Renders the overworld.
 * @author josh
 *
 */
public class OverworldRenderer {
	private static final int GRID_SIZE = 10;
	
	private ShapeRenderer renderer;
	
	private int width, height;
	
	public OverworldRenderer() {
		renderer = new ShapeRenderer();
	}
	
	/**
	 * Handles window resize.
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Renders the overworld.
	 */
	public void render(OrthographicCamera camera) {
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
}

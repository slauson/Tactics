package com.slauson.tactics.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.slauson.tactics.model.Overworld;

/**
 * Renders the overworld.
 * @author josh
 *
 */
public class OverworldRenderer {
	
	private ShapeRenderer renderer;
	
	private Overworld overworld;
	
	private int width, height;
	
	public OverworldRenderer(Overworld overworld) {
		this.overworld = overworld;
		
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
		renderer.begin(ShapeType.FilledRectangle);
		
		for (int i = 0; i < overworld.regions.length; i++) {
			for (int j = 0; j < overworld.regions[i].length; j++) {
				renderer.setColor(overworld.regions[i][j].color);
				renderer.filledRect(overworld.regions[i][j].position.x, overworld.regions[i][j].position.y, overworld.regions[i][j].bounds.width, overworld.regions[i][j].bounds.height);
			}
		}
		renderer.end();
	}
}

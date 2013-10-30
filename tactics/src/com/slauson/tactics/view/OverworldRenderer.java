package com.slauson.tactics.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Region;

/**
 * Renders the overworld.
 * @author josh
 *
 */
public class OverworldRenderer extends Renderer {
	
	private static final Color SELECTED_REGION_COLOR = Color.WHITE;
	private static final Color SELECTED_REGION_OUTLINE_COLOR = Color.WHITE;
	private static final float MARKED_REGION_COLOR_FACTOR = 0.5f;
	
	private ShapeRenderer renderer;
	
	private Overworld overworld;
	
	public OverworldRenderer(Overworld overworld) {
		super();
		this.overworld = overworld;
		
		renderer = new ShapeRenderer();
	}
	
	/**
	 * Renders the overworld.
	 */
	@Override
	public void render(OrthographicCamera camera, float delta, boolean debug) {
		renderer.setProjectionMatrix(camera.combined);
		
		// draw colors
		renderer.begin(ShapeType.FilledRectangle);
		for (Region region : overworld.regionList) {
			// selected region
			if (region.selected) {
				renderer.setColor(SELECTED_REGION_COLOR);
			}
			// marked region
			else if (region.marked) {
				renderer.setColor(region.player.color.r*MARKED_REGION_COLOR_FACTOR, region.player.color.g*MARKED_REGION_COLOR_FACTOR, region.player.color.b*MARKED_REGION_COLOR_FACTOR, 0);
			}
			// normal region
			else {
				renderer.setColor(region.player.color);
			}
			
			renderer.filledRect(region.position.x, region.position.y, region.bounds.width, region.bounds.height);
		}
		renderer.end();

		// draw outline
		renderer.begin(ShapeType.Rectangle);
		renderer.setColor(Color.BLACK);
		for (Region region : overworld.regionList) {
			renderer.rect(region.position.x, region.position.y, region.bounds.width, region.bounds.height);
		}
		renderer.end();
		
		super.render(camera, delta, debug);
	}
}

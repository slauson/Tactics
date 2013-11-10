package com.slauson.tactics.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

public abstract class Renderer {

	private static final int FPS_MIN_CHANGE = 5;
	
	private int fps;
	
	protected SpriteBatch screenSpriteBatch;
	protected ShapeRenderer screenRenderer;
	protected BitmapFont screenFont;
	
	protected int screenWidth, screenHeight;
	
	public Renderer() {
		screenSpriteBatch = new SpriteBatch();
		screenRenderer = new ShapeRenderer();
		screenFont = new BitmapFont();
		
		fps = 0;
	}
	
	/**
	 * Handles window resize.
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		
		// reset the sprite batch
		screenSpriteBatch = new SpriteBatch();
		screenRenderer = new ShapeRenderer();
	}
	
	public void render(OrthographicCamera camera, float delta, boolean debug) {
		if (debug) {
			// draw gridlines
			
			// draw fps
			int currentFps = (int)(1.f/delta);
			
			if (Math.abs(fps - currentFps) >= FPS_MIN_CHANGE) {
				fps = currentFps;
			}
			
			String fpsText = "FPS: " + fps;
			screenSpriteBatch.begin();
			screenFont.setColor(Color.WHITE);
			//TextBounds bounds = font.getBounds(fpsText);
			// draws from top left of text
			screenFont.draw(screenSpriteBatch, fpsText, 0, screenHeight);
			screenSpriteBatch.end();
		}
	}
}

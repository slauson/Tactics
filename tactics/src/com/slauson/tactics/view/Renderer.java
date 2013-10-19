package com.slauson.tactics.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;

public abstract class Renderer {

	private static final int FPS_MIN_CHANGE = 5;
	
	protected SpriteBatch spriteBatch;
	protected BitmapFont font;
	
	private int fps;
	protected int width, height;
	
	public Renderer() {
		this.spriteBatch = new SpriteBatch();
		this.font = new BitmapFont();
		
		this.fps = 0;
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
	
	public void render(OrthographicCamera camera, float delta, boolean debug) {
		if (debug) {
			// draw gridlines
			
			// draw fps
			int currentFps = (int)(1.f/delta);
			
			if (Math.abs(fps - currentFps) >= FPS_MIN_CHANGE) {
				fps = currentFps;
			}
			
			String fpsText = "FPS: " + fps;
			spriteBatch.begin();
			font.setColor(Color.WHITE);
			TextBounds bounds = font.getBounds(fpsText);
			// draws from top left of text
			font.draw(spriteBatch, fpsText, width - bounds.width, height);
			spriteBatch.end();
		}
	}
}

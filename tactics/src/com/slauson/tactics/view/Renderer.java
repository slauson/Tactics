package com.slauson.tactics.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;

public abstract class Renderer {

	private static final int FPS_MIN_CHANGE = 5;
	protected static final float NON_TURN_COLOR_FACTOR = 0.5f;
	
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
	
	public void drawUnit(ShapeRenderer renderer, Region region) {
		
		// size of unit is based on health
		float sizeFactor = 0.25f + (region.unit.health / Unit.MAX_HEALTH * 3 / 4);
		
		switch(region.unit.type) {
		case CIRCLE:
			renderer.begin(ShapeType.FilledCircle);
			if (region.unit.rotation != 0) {
				rotateAroundPoint(renderer, region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2, region.unit.rotation);
			}
			if (region.unit.offset.len() != 0) {
				renderer.translate(region.unit.offset.x, region.unit.offset.y, 0);
			}
			renderer.filledCircle(region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2, 3*region.bounds.width/8*sizeFactor, 20);
			renderer.end();
			break;
		case SQUARE:
			renderer.begin(ShapeType.FilledRectangle);
			if (region.unit.rotation != 0) {
				rotateAroundPoint(renderer, region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2, region.unit.rotation);
			}
			if (region.unit.offset.len() != 0) {
				renderer.translate(region.unit.offset.x, region.unit.offset.y, 0);
			}
			renderer.filledRect(region.position.x + region.bounds.width/2 - 3*region.bounds.width/8*sizeFactor, region.position.y + region.bounds.height/2 - 3*region.bounds.height/8*sizeFactor, 3*region.bounds.width/4*sizeFactor, 3*region.bounds.height/4*sizeFactor);
			renderer.end();
			break;
		case TRIANGLE:
			renderer.begin(ShapeType.FilledTriangle);
			if (region.unit.rotation != 0) {
				rotateAroundPoint(renderer, region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2, region.unit.rotation);
			}
			if (region.unit.offset.len() != 0) {
				renderer.translate(region.unit.offset.x, region.unit.offset.y, 0);
			}
			renderer.filledTriangle(region.position.x + region.bounds.width/2 - 3*region.bounds.width/8*sizeFactor, region.position.y + region.bounds.height/2 - 3*region.bounds.height/8*sizeFactor,
					region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2 + 3*region.bounds.height/8*sizeFactor,
					region.position.x + region.bounds.width/2 + 3*region.bounds.width/8*sizeFactor, region.position.y + region.bounds.width/2 - 3*region.bounds.height/8*sizeFactor);
//			renderer.filledTriangle(region.position.x + region.bounds.width/8, region.position.y + region.bounds.height/8,
//					region.position.x + region.bounds.width/2, region.position.y + 7*region.bounds.height/8,
//					region.position.x + 7*region.bounds.width/8, region.position.y + region.bounds.height/8);
			renderer.end();
			break;
		case RANGED_CIRCLE:
			renderer.begin(ShapeType.FilledCircle);
			if (region.unit.rotation != 0) {
				rotateAroundPoint(renderer, region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2, region.unit.rotation);
			}
			if (region.unit.offset.len() != 0) {
				renderer.translate(region.unit.offset.x, region.unit.offset.y, 0);
			}
			//renderer.filledCircle(region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2, 3*region.bounds.width/8*sizeFactor, 20);
			renderer.filledCircle(region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2 - 3*region.bounds.height/16*sizeFactor, 3*region.bounds.width/16*sizeFactor, 20);
			renderer.filledCircle(region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2 + 3*region.bounds.height/16*sizeFactor, 3*region.bounds.width/16*sizeFactor, 20);
			renderer.end();
			renderer.identity();
			break;
		case RANGED_SQUARE:
			renderer.begin(ShapeType.FilledRectangle);
			if (region.unit.rotation != 0) {
				rotateAroundPoint(renderer, region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2, region.unit.rotation);
			}
			if (region.unit.offset.len() != 0) {
				renderer.translate(region.unit.offset.x, region.unit.offset.y, 0);
			}
			renderer.filledRect(region.position.x + region.bounds.width/2 - 3*region.bounds.width/16*sizeFactor, region.position.y + region.bounds.height/2 - 3*region.bounds.height/8*sizeFactor, 3*region.bounds.width/8*sizeFactor, 3*region.bounds.height/4*sizeFactor);
			renderer.end();
			break;
		case RANGED_TRIANGLE:
			renderer.begin(ShapeType.FilledTriangle);
			if (region.unit.rotation != 0) {
				rotateAroundPoint(renderer, region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2, region.unit.rotation);
			}
			if (region.unit.offset.len() != 0) {
				renderer.translate(region.unit.offset.x, region.unit.offset.y, 0);
			}
			renderer.filledTriangle(region.position.x + region.bounds.width/2 - 3*region.bounds.width/16*sizeFactor, region.position.y + region.bounds.height/2 - 3*region.bounds.height/8*sizeFactor,
					region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2 + 3*region.bounds.height/8*sizeFactor,
					region.position.x + region.bounds.width/2 + 3*region.bounds.width/16*sizeFactor, region.position.y + region.bounds.width/2 - 3*region.bounds.height/8*sizeFactor);
//			renderer.filledTriangle(region.position.x + region.bounds.width/2 - 3*region.bounds.width/16*sizeFactor, region.position.y + region.bounds.height/2 - 3*region.bounds.height/8*sizeFactor,
//					region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2,
//					region.position.x + region.bounds.width/2 + 3*region.bounds.width/16*sizeFactor, region.position.y + region.bounds.width/2 - 3*region.bounds.height/8*sizeFactor);
//			renderer.filledTriangle(region.position.x + region.bounds.width/2 - 3*region.bounds.width/16*sizeFactor, region.position.y + region.bounds.height/2,
//					region.position.x + region.bounds.width/2, region.position.y + region.bounds.height/2 + 3*region.bounds.height/8*sizeFactor,
//					region.position.x + region.bounds.width/2 + 3*region.bounds.width/16*sizeFactor, region.position.y + region.bounds.width/2);
			renderer.end();
			break;
		}
		
		// reset renderer
		renderer.identity();
	}
	
	/**
	 * Performs rotate around given point.
	 * @param renderer renderer
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 * @param angle angle to rotate
	 */
	protected void rotateAroundPoint(ShapeRenderer renderer, float x, float y, float angle) {
		
		// translate to point
		renderer.translate(x, y, 0);
		
		// rotate
		renderer.rotate(0, 0, 1, angle);
		
		// translate back
		renderer.translate(-x, -y, 0);
	}
}

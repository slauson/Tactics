package com.slauson.tactics.controller;

/**
 * Base controller.
 * @author josh
 *
 */
public abstract class Controller {

	protected static final float MAX_DELTA = 0.1f;
	
	protected boolean paused;
	
	public Controller() {
		paused = false;
	}
	
	/**
	 * Updates controller.
	 * @param delta elapsed time since last update.
	 */
	public abstract void update(float delta);
	
	/**
	 * Handles touch event.
	 * @param worldX
	 * @param worldY
	 * @return true if touch event is handled
	 */
	public abstract boolean touchDown(float worldX, float worldY);

	/**
	 * Handles key events.
	 * @param character
	 * @return true if key event is handled
	 */
	public boolean keyTyped(char character) {
		switch (character) {
		case 'p':
			paused = !paused;
			break;
		}
		return false;
	}
}

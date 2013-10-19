package com.slauson.tactics.controller;

/**
 * Base controller.
 * @author josh
 *
 */
public abstract class Controller {

	protected static final float MAX_DELTA = 0.1f;
	
	public enum Event {
		NONE, BATTLE
	};
	
	/**
	 * Updates controller.
	 * @param delta elapsed time since last update.
	 */
	public abstract void update(float delta);
	
	/**
	 * Handles touch event.
	 * @param worldX
	 * @param worldY
	 * @return event type of event that needs to be handled.
	 */
	public abstract Event touchDown(float worldX, float worldY);
}

package com.slauson.tactics.controller;

import com.slauson.tactics.TacticsGame;
import com.slauson.tactics.event.Event;
import com.slauson.tactics.event.EventHandler;

/**
 * Base controller.
 * @author josh
 *
 */
public abstract class Controller implements EventHandler {

	protected static final float MAX_DELTA = 0.1f;
	
	protected boolean paused;
	
	private TacticsGame game;
	
	public Controller(TacticsGame game) {
		this.game = game;
		game.registerEventHandler(this);
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
	
	public void fireEvent(Event event) {
		game.fireEvent(event);
	}
}

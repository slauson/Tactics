package com.slauson.tactics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.slauson.tactics.event.Event;
import com.slauson.tactics.event.EventHandler;
import com.slauson.tactics.screen.OverworldScreen;

/**
 * Main class for the game.
 * @author josh
 *
 */
public class TacticsGame extends Game {
	
	private OverworldScreen overworldScreen;
	private List<EventHandler> eventHandlers;
	
	@Override
	public void create() {
		overworldScreen = new OverworldScreen(this);
		eventHandlers = new ArrayList<EventHandler>();
		showOverworld();
	}
	
	/**
	 * Displays the overworld screen.
	 */
	public void showOverworld() {
		setScreen(overworldScreen);
	}
	
	public void registerEventHandler(EventHandler eventHandler) {
		eventHandlers.add(eventHandler);
	}
	
	public boolean unregisterEventHandler(EventHandler eventHandler) {
		return eventHandlers.remove(eventHandler);
	}
	
	public void fireEvent(Event event) {
		for (EventHandler eventHandler : eventHandlers) {
			eventHandler.handleEvent(event);
		}
	}
	
}

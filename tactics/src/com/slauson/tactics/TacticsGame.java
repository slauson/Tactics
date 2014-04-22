package com.slauson.tactics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.slauson.tactics.event.Event;
import com.slauson.tactics.event.EventHandler;
import com.slauson.tactics.menu.MainMenu;
import com.slauson.tactics.screen.OverworldScreen;

/**
 * Main class for the game.
 * @author josh
 *
 */
public class TacticsGame extends Game implements EventHandler{
	
	private MainMenu mainMenu;
	private OverworldScreen overworldScreen;
	private List<EventHandler> eventHandlers;
	
	@Override
	public void create() {
		eventHandlers = new ArrayList<EventHandler>();
		eventHandlers.add(this);
		showMainMenu();
	}
	
	/**
	 * Displays the main menu.
	 */
	public void showMainMenu() {
		
		if (mainMenu == null) {
			mainMenu = new MainMenu(this);
		}
		
		setScreen(mainMenu);
	}
	
	/**
	 * Displays the overworld screen.
	 */
	public void showOverworld() {
		
		if (overworldScreen == null) {
			overworldScreen = new OverworldScreen(this);
		}
		
		setScreen(overworldScreen);
	}
	
	/**
	 * Registers event handler.
	 * @param eventHandler
	 */
	public void registerEventHandler(EventHandler eventHandler) {
		eventHandlers.add(eventHandler);
	}
	
	/**
	 * Unregisters event handler.
	 * @param eventHandler
	 * @return true if event handler was removed.
	 */
	public boolean unregisterEventHandler(EventHandler eventHandler) {
		return eventHandlers.remove(eventHandler);
	}
	
	/**
	 * Fires event to be handled by event handler.
	 * @param event event
	 */
	public void fireEvent(Event event) {
		for (EventHandler eventHandler : eventHandlers) {
			eventHandler.handleEvent(event);
		}
	}

	@Override
	public void handleEvent(Event event) {
		switch (event.type) {
		default:
			// ignore
			break;
		}
	}
	
}

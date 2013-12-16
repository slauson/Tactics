package com.slauson.tactics.model;

import com.badlogic.gdx.graphics.Color;
import com.slauson.tactics.ai.AI;
import com.slauson.tactics.ai.RandomAI;

public class Player {
	
	public static int NUM_FREE_REINFORCEMENTS = 3;
	public static int NUM_REINFORCEMENTS_PER_ISLAND = 1;
	
	public enum PlayerType {
		PLAYER, AI_RANDOM
	}
	
	public int id;
	public PlayerType type;
	public AI ai;
	
	public Color color;
	
	public int regions;
	public int units;
	public int reinforcements;
	
	private static final Color[] colors = new Color[] {
		Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.MAGENTA, Color.YELLOW
	};
	private static int colorIndex = 0;
	
	public Player(int id, PlayerType type) {
		this.type = type;

		switch (type) {
		case AI_RANDOM:
			ai = new RandomAI();
			break;
		default:
			ai = null;
			break;
		}
		
		color = colors[colorIndex];
		colorIndex++;
		
		regions = 0;
		units = 0;
		reinforcements = NUM_FREE_REINFORCEMENTS;
	}
	
	@Override
	public String toString() {
		return color.toString();
	}
	
}

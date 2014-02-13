package com.slauson.tactics.model;

import com.badlogic.gdx.graphics.Color;
import com.slauson.tactics.ai.AI;
import com.slauson.tactics.ai.AdvancedAI;
import com.slauson.tactics.ai.RandomAI;
import com.slauson.tactics.ai.ScoreFactorBuilder;
import com.slauson.tactics.ai.SimpleAI;

public class Player {
	
	public static int NUM_FREE_REINFORCEMENTS = 3;
	public static int NUM_REINFORCEMENTS_PER_ISLAND = 1;
	
	public enum Type {
		PLAYER, RANDOM, SIMPLE, ADVANCED
	}
	
	public int id;
	public Type type;
	public AI ai;
	
	public Color color;
	
	public int regions;
	public int units;
	public int reinforcements;
	
	private static final Color[] colors = new Color[] {
		Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.MAGENTA, Color.YELLOW
	};
	private static int colorIndex = 0;
	
	public Player(int id, Type type) {
		this.type = type;

		switch (type) {
		case RANDOM:
			ai = new RandomAI();
			break;
		case SIMPLE:
			ai = new SimpleAI();
			break;
		case ADVANCED:
			ai = new AdvancedAI(ScoreFactorBuilder.Level.NORMAL);
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

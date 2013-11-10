package com.slauson.tactics.model;

import com.badlogic.gdx.graphics.Color;

public class Player {
	
	public enum Type {
		PLAYER, AI
	}
	
	public Type type;
	
	public Color color;
	
	public int regions;
	public int units;
	public int reinforcements;
	
	private static final Color[] colors = new Color[] {
		Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.MAGENTA, Color.YELLOW
	};
	private static int colorIndex = 0;
	
	public Player(Type type) {
		this.type = type;
		
		color = colors[colorIndex];
		colorIndex++;
		
		regions = 0;
		units = 0;
		reinforcements = 10;
	}
	
	@Override
	public String toString() {
		return color.toString();
	}

}

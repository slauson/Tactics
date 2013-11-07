package com.slauson.tactics.model;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;

public class Player {
	
	public enum Type {
		PLAYER, AI
	}
	
	public Type type;
	
	public Color color;
	
	public Set<Region> regions;
	
	private static final Color[] colors = new Color[] {
		Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.MAGENTA, Color.YELLOW
	};
	private static int colorIndex = 0;
	
	public Player(Type type) {
		this.type = type;
		
		color = colors[colorIndex];
		colorIndex++;
		
		regions = new HashSet<Region>();
	}
	
	@Override
	public String toString() {
		return color.toString();
	}

}

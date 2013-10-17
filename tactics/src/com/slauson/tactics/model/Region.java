package com.slauson.tactics.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Region {

	private static final float SIZE = 1f;
	private static final Color DEFAULT_COLOR = Color.RED;
	
	public Vector2 position;
	public Rectangle bounds;
	
	public Color color;
	
	public Region(Vector2 position) {
		this.position = position;
		
		bounds = new Rectangle();
		bounds.x = position.x;
		bounds.y = position.y;
		bounds.width = SIZE;
		bounds.height = SIZE;
		
		color = DEFAULT_COLOR;
	}
	
	public void resetColor() {
		color = DEFAULT_COLOR;
	}
}

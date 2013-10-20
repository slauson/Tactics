package com.slauson.tactics.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Region {

	private static final float SIZE = 1f;
	private static final Color DEFAULT_COLOR = Color.WHITE;
	
	public List<Region> neighbors;
	
	public Player player;
	
	public Vector2 position;
	public Rectangle bounds;
	
	public boolean selected;
	public boolean marked;
	
	public Region(Vector2 position) {
		this.position = position;
		
		bounds = new Rectangle();
		bounds.x = position.x;
		bounds.y = position.y;
		bounds.width = SIZE;
		bounds.height = SIZE;
		
		selected = false;
		marked = false;
		
		neighbors = new ArrayList<Region>();
	}
}

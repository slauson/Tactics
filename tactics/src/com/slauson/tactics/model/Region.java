package com.slauson.tactics.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.slauson.tactics.utils.Utils;

public class Region {
	
	private static final float SIZE = 1f;
	
	public Neighbors neighbors;
	
	public Player player;
	
	public Island island;
	
	public Unit unit;
	
	public Vector2 position;
	public Rectangle bounds;
	
	public boolean selected;
	public boolean marked;
	
	public Region(Vector2 position) {
		this(position, null);
	}
	
	public Region(Vector2 position, Island island) {
		this.position = position;
		this.island = island;
		
		bounds = new Rectangle();
		bounds.x = position.x;
		bounds.y = position.y;
		bounds.width = SIZE;
		bounds.height = SIZE;
		
		selected = false;
		marked = false;
		
		neighbors = new Neighbors();
		
		testRegion();
	}
	
	public float simpleDistanceTo(Region other) {
		return Math.abs(position.x - other.position.x) + Math.abs(position.y - other.position.y);
	}
	
	@Override
	public String toString() {
		return position.x + ", " + position.y + ": " + player + " - " + unit;
	}
	
	private void testRegion() {
		int typeIndex = Utils.random().nextInt(Unit.Type.values().length);
		unit = new Unit(Unit.Type.values()[typeIndex], Utils.random().nextInt((int)Unit.MAX_HEALTH));
	}
}

package com.slauson.tactics.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.slauson.tactics.utils.Util;

public class Region {
	
	private static final float SIZE = 1f;
	
	public Neighbors neighbors;
	
	public Player player;
	
	public Unit unit;
	
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
		
		neighbors = new Neighbors();
		
		testRegion();
	}
	
	private void testRegion() {
		int typeIndex = Util.random().nextInt(Unit.UnitType.values().length);
		unit = new Unit(Unit.UnitType.values()[typeIndex], Util.random().nextInt((int)Unit.MAX_HEALTH));
	}
	
	@Override
	public String toString() {
		return position.x + ", " + position.y + ": " + player + " - " + unit;
	}
}

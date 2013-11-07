package com.slauson.tactics.model;

public class Unit {
	
	public static float MAX_HEALTH = 10f;
	public static float UNIT_WEAKNESS_FACTOR = 0.25f;
	public static float UNIT_WEAKNESS_RANDOM_FACTOR = 0.125f;
	
	/*
	 * Square > triangle
	 * Triangle > circle
	 * Circle > square
	 */
	public enum Type {
		/* Strong against square, weak against triangle */
		CIRCLE, RANGED_CIRCLE,
		/* Strong against triangle, weak against circle */
		SQUARE, RANGED_SQUARE,
		/* Strong against circle, weak against square */
		TRIANGLE, RANGED_TRIANGLE;
		
		public boolean isRanged() {
			return this == Type.RANGED_CIRCLE || this == Type.RANGED_SQUARE || this == Type.RANGED_TRIANGLE;
		}
	}
	
	public enum State {
		ACTIVE, IDLE
	}
	
	public final Type type;
	public State state;

	public float health;
	
	public Unit(Type type, float health) {
		this.type = type;
		this.health = health;
		
		state = State.ACTIVE;
	}
	
	@Override
	public String toString() {
		return type.name() + "(" + state.name() + ", " + health + ")";
	}
}
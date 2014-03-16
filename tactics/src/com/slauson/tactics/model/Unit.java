package com.slauson.tactics.model;

import com.badlogic.gdx.math.Vector2;

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
		
		/**
		 * Returns true if unit is ranged.
		 * @return
		 */
		public boolean isRanged() {
			return this == Type.RANGED_CIRCLE || this == Type.RANGED_SQUARE || this == Type.RANGED_TRIANGLE;
		}
		
		/**
		 * Returns next unit type.
		 * @return
		 */
		public Type next() {
			switch (this) {
			default:
			case CIRCLE:
				return SQUARE;
			case SQUARE:
				return TRIANGLE;
			case TRIANGLE:
				return RANGED_CIRCLE;
			case RANGED_CIRCLE:
				return RANGED_SQUARE;
			case RANGED_SQUARE:
				return RANGED_TRIANGLE;
			case RANGED_TRIANGLE:
				return CIRCLE;
			}
		}
	}
	
	/**
	 * Unit state.
	 */
	public enum State {
		IDLE,
		BATTLE;
		
		/**
		 * Returns true if unit requires animation.
		 * @return
		 */
		public boolean animate() {
			return this != IDLE;
		}
	}
	
	public Type type;
	public State state;
	
	public Vector2 offset;
	public float rotation;
	
	public float health;
	public boolean hasMove;
	public boolean hasAttack;
	
	public Unit(Type type, float health) {
		this.type = type;
		this.health = health;
		
		state = State.IDLE;
		rotation = 0f;
		hasMove = true;
		hasAttack = true;
		offset = new Vector2();
	}
	
	@Override
	public String toString() {
		return type.name() + "(" + hasMove + ", " + hasAttack + ", " + health + ")";
	}
}
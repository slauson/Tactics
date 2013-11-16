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
	public enum UnitType {
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
			return this == UnitType.RANGED_CIRCLE || this == UnitType.RANGED_SQUARE || this == UnitType.RANGED_TRIANGLE;
		}
		
		/**
		 * Returns next unit type.
		 * @return
		 */
		public UnitType next() {
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
	
	public UnitType type;
	
	public float health;
	public boolean hasMove;
	public boolean hasAttack;
	
	public Unit(UnitType type, float health) {
		this.type = type;
		this.health = health;
		
		hasMove = true;
		hasAttack = true;
	}
	
	@Override
	public String toString() {
		return type.name() + "(" + hasMove + ", " + hasAttack + ", " + health + ")";
	}
}
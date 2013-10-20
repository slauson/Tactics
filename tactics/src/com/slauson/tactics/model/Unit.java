package com.slauson.tactics.model;

public class Unit {
	
	public enum Type {
		CIRCLE, SQUARE, TRIANGLE
	}
	
	public enum State {
		ATTACKING, DEFENDING, IDLE, DEFENDED
	}
	
	public final Type type;
	public State state;

	public int health;
	
	public Unit(Type type, int health) {
		this.type = type;
		this.health = health;
		
		state = State.IDLE;
	}
}
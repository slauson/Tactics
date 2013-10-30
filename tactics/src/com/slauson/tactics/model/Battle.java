package com.slauson.tactics.model;

import com.slauson.tactics.model.Unit.Type;

public class Battle {
	
	private static final int NUM_UNITS = 3;

	public Region attackingRegion, defendingRegion;
	
	public Unit[] attackingUnits, defendingUnits;
	
	public Battle(Region attackingRegion, Region defendingRegion) {
		this.attackingRegion = attackingRegion;
		this.defendingRegion = defendingRegion;
		
		attackingUnits = new Unit[NUM_UNITS];
		defendingUnits = new Unit[NUM_UNITS];
		
		testUnits();
	}
	
	private void testUnits() {
		
		// random units
		attackingUnits[0] = new Unit(Type.CIRCLE, 100);
		attackingUnits[1] = new Unit(Type.SQUARE, 100);
		attackingUnits[2] = new Unit(Type.TRIANGLE, 100);
		
		defendingUnits[0] = new Unit(Type.TRIANGLE, 100);
		defendingUnits[1] = new Unit(Type.SQUARE, 100);
		defendingUnits[2] = new Unit(Type.CIRCLE, 100);
	}
}

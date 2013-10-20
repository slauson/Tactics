package com.slauson.tactics.model;

public class Battle {

	private Region attackingRegion, defendingRegion;
	
	private Unit[] attackingUnits, defendingUnits;
	
	public Battle(Region attackingRegion, Region defendingRegion) {
		this.attackingRegion = attackingRegion;
		this.defendingRegion = defendingRegion;
		
		attackingUnits = new Unit[3];
		defendingUnits = new Unit[3];
	}
}

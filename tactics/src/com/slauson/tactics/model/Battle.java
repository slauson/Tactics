package com.slauson.tactics.model;

import com.slauson.tactics.utils.BattleUtils;

public class Battle {
	
	public static final float DURATION = 1f; 
	
	public Region attackingRegion, defendingRegion;
	
	public float battleTime;
	public float[] battleDamage;
	
	public Battle() {
		reset();
	}
	
	public void init(Region attackingRegion, Region defendingRegion) {
		this.attackingRegion = attackingRegion;
		this.defendingRegion = defendingRegion;
		
		attackingRegion.unit.state = Unit.State.BATTLE;
		defendingRegion.unit.state = Unit.State.BATTLE;
		
		battleTime = DURATION;
		battleDamage = BattleUtils.calculateBattleDamage(attackingRegion, defendingRegion, Unit.UNIT_WEAKNESS_RANDOM_FACTOR);
	}
	
	public void reset() {
		if (attackingRegion != null && attackingRegion.unit != null) {
			attackingRegion.unit.state = Unit.State.IDLE;
		}
		if (defendingRegion != null && defendingRegion.unit != null) {
			defendingRegion.unit.state = Unit.State.IDLE;
		}
		attackingRegion = null;
		defendingRegion = null;
		battleTime = 0;
	}
	
	/**
	 * Updates battle
	 * @param delta delta passed
	 * @return true if battle is still active
	 */
	public boolean update(float delta) {
		battleTime -= delta;
		
		if (battleTime < 0) {
			return false;
		}
		
		return true;
	}
	
	public boolean active () {
		return battleTime > 0;
	}
	
	public float percentComplete() {
		return (DURATION - battleTime) / DURATION;
	}
}

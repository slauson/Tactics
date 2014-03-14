package com.slauson.tactics.model;

import com.slauson.tactics.utils.BattleUtils;

public class Battle {
	
	public static final float DURATION = 1f;
	public static final float PHASE_DURATION = 0.5f;
	
	/**
	 * Phase of battle
	 */
	public static enum Phase {
		ATTACKER_ATTACK,
		DEFENDER_DAMAGE,
		DEFENDER_ATTACK,
		ATTACKER_DAMAGE,
		DONE;
		
		public Phase next(int numPhases) {
			switch (this) {
			case ATTACKER_ATTACK:
				return DEFENDER_DAMAGE;
			case DEFENDER_DAMAGE:
				return numPhases > 2 ? DEFENDER_ATTACK : DONE;
			case DEFENDER_ATTACK:
				return ATTACKER_DAMAGE;
			case ATTACKER_DAMAGE:
				return DONE;
			case DONE:
			default:
				return DONE;
			}
		}
	}
	
	public Region attackingRegion, defendingRegion;
	
	public float[] battleDamage;
	public float phaseTime;
	public Phase phase;
	public int numPhases;
	
	public Battle() {
		reset();
	}
	
	public void init(Region attackingRegion, Region defendingRegion) {
		this.attackingRegion = attackingRegion;
		this.defendingRegion = defendingRegion;
		
		attackingRegion.unit.state = Unit.State.BATTLE;
		defendingRegion.unit.state = Unit.State.BATTLE;
		
		battleDamage = BattleUtils.calculateBattleDamage(attackingRegion, defendingRegion, Unit.UNIT_WEAKNESS_RANDOM_FACTOR);
		phaseTime = DURATION;
		
		// calculate number of phases
		// check if defending region can attack back
		// assuming attacking region can attack...
		for (Neighbor neighbor : defendingRegion.neighbors) {
			if (neighbor.region.equals(attackingRegion)) {
				if (neighbor.type == Neighbor.Type.DIRECT) {
					numPhases = defendingRegion.unit.type.isRanged() ? 2 : 4;
				} else {
					numPhases = defendingRegion.unit.type.isRanged() ? 4 : 2;
				}
			}
		}
		
		phase = Phase.ATTACKER_ATTACK;
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
		phase = Phase.DONE;
		numPhases = 0;
	}
	
	/**
	 * Updates battle
	 * @param delta delta passed
	 * @return true if battle is still active
	 */
	public boolean update(float delta) {
		phaseTime -= delta;
		
		if (phaseTime < 0) {
			// move to next phase
			phase = phase.next(numPhases);
			
			System.out.println(phase);
			
			phaseTime = PHASE_DURATION + phaseTime;
		}
		
		return phase != Phase.DONE;
	}
	
	public boolean active () {
		return phase != Phase.DONE;
	}
	
	public float percentPhaseComplete() {
		return phaseTime / PHASE_DURATION;
	}
}

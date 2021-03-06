package com.slauson.tactics.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.slauson.tactics.utils.BattleUtils;

// TODO BattleController
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
		UPDATE_HEALTH,
		TAKEOVER;
	}
	
	/**
	 * Type of battle
	 */
	public static enum Type {
		DIRECT,
		RANGED;
	}
	
	public Region attackingRegion, defendingRegion;
	
	public float[] originalHealth;
	public float[] battleHealth;
	public float[] battleDamage;
	public float phaseTime;
	
	// battle is complete
	public boolean complete;
	public List<Phase> phases;
	public Type type;
	
	public Battle() {
		phases = new ArrayList<Phase>(Phase.values().length);
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
		// assuming attacking region can attack...
		phases.add(Phase.ATTACKER_ATTACK);
		phases.add(Phase.DEFENDER_DAMAGE);
		
		// check if defending region can attack back
		for (Neighbor neighbor : defendingRegion.neighbors) {
			if (neighbor.region.equals(attackingRegion)) {
				if ((neighbor.type == Neighbor.Type.DIRECT && !defendingRegion.unit.type.isRanged())
						|| (neighbor.type != Neighbor.Type.DIRECT && defendingRegion.unit.type.isRanged()))
				{
					phases.add(Phase.DEFENDER_ATTACK);
					phases.add(Phase.ATTACKER_DAMAGE);
				}
				
				type = (neighbor.type == Neighbor.Type.DIRECT) ? Type.DIRECT : Type.RANGED;
			}
		}
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
		phases.clear();
		complete = false;
		
		battleDamage = null;
		battleHealth = null;
		originalHealth = null;
	}
	
	/**
	 * Updates battle
	 * @param delta delta passed
	 */
	public void update(float delta) {
		phaseTime -= delta;
	}
	
	public boolean active () {
		return !phases.isEmpty();
	}
	
	public float percentPhaseComplete() {
		return (PHASE_DURATION - phaseTime) / PHASE_DURATION;
	}
	
	public Phase currentPhase() {
		return phases.get(0);
	}
}

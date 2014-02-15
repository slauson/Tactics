package com.slauson.tactics.utils;

import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;

/**
 * Helper methods relating to battles.
 * @author josh
 *
 */
public class BattleUtils {

	/**
	 * Calculates battle damage between two regions.
	 * Returns attackingRegionAttackDamage, defendingRegionAttackDamage
	 * @param attackingRegion
	 * @param defendingRegion
	 * @param randomFactor
	 * @return
	 */
	public static float[] calculateBattleDamage(Region attackingRegion, Region defendingRegion, float randomFactor) {
		
		float[] result = new float[2];
		
		// calculate attack factor for attacking region 
		float attackFactor = 1f;

		switch (attackingRegion.unit.type) {
		case CIRCLE:
		case RANGED_CIRCLE:
			switch (defendingRegion.unit.type) {
			case CIRCLE:
			case RANGED_CIRCLE:
				attackFactor += -randomFactor + 2*randomFactor*Utils.random().nextFloat();
				break;
			case SQUARE:
			case RANGED_SQUARE:
				attackFactor += Unit.UNIT_WEAKNESS_FACTOR - randomFactor + 2*randomFactor*Utils.random().nextFloat();
				break;
			case TRIANGLE:
			case RANGED_TRIANGLE:
				attackFactor -= Unit.UNIT_WEAKNESS_FACTOR - randomFactor + 2*randomFactor*Utils.random().nextFloat();
				break;
			}
			break;
		case SQUARE:
		case RANGED_SQUARE:
			switch (defendingRegion.unit.type) {
			case CIRCLE:
			case RANGED_CIRCLE:
				attackFactor -= Unit.UNIT_WEAKNESS_FACTOR - randomFactor + 2*randomFactor*Utils.random().nextFloat();
				break;
			case SQUARE:
			case RANGED_SQUARE:
				attackFactor += -randomFactor + 2*randomFactor*Utils.random().nextFloat();
				break;
			case TRIANGLE:
			case RANGED_TRIANGLE:
				attackFactor += Unit.UNIT_WEAKNESS_FACTOR - randomFactor + 2*randomFactor*Utils.random().nextFloat();
				break;
			}
			break;
		case TRIANGLE:
		case RANGED_TRIANGLE:
			switch (defendingRegion.unit.type) {
			case CIRCLE:
			case RANGED_CIRCLE:
				attackFactor += Unit.UNIT_WEAKNESS_FACTOR - randomFactor + 2*randomFactor*Utils.random().nextFloat();
				break;
			case SQUARE:
			case RANGED_SQUARE:
				attackFactor -= Unit.UNIT_WEAKNESS_FACTOR - randomFactor + 2*randomFactor*Utils.random().nextFloat();
				break;
			case TRIANGLE:
			case RANGED_TRIANGLE:
				attackFactor += -randomFactor + 2*randomFactor*Utils.random().nextFloat();
				break;
			}
			break;
		}
		
		float defendFactor = 2 - attackFactor;

		// calculate how much damage each region can do
		result[0] = attackFactor * attackingRegion.unit.health;
		result[1] = defendFactor * defendingRegion.unit.health;
		
		// special case for ranged units
		if ((attackingRegion.unit.type.isRanged() && !defendingRegion.unit.type.isRanged()) ||
				defendingRegion.unit.type.isRanged() && !attackingRegion.unit.type.isRanged())
		{
			result[1] = 0;
		}
		
		return result;
	}
	
	/**
	 * Returns likelihood that attacking region could defeat defending region.
	 * (1: attacker more likely to win, 0: defender more likely to win)
	 * @param attackingRegion
	 * @param defendingRegion
	 * @return
	 */
	public static float calculateBattleLikelihood(Region attackingRegion, Region defendingRegion) {
		if (attackingRegion.unit == null) {
			return 0;
		}
		if (defendingRegion.unit == null) {
			return 1;
		}
		
		float[] battleDamage = BattleUtils.calculateBattleDamage(attackingRegion, defendingRegion, 0);
		
		// ((attacker damage - defender damage) / total damage + 1) / 2 
		return ((battleDamage[0] / defendingRegion.unit.health - battleDamage[1] / attackingRegion.unit.health)
				/ (battleDamage[0] / defendingRegion.unit.health + battleDamage[1] / attackingRegion.unit.health)
				+ 1) / 2;
	}
	
	/**
	 * Handles battle between two regions.
	 * @param attackingRegion
	 * @param defendingRegion
	 * @return attacker's updated region
	 */
	public static Region handleBattle(Region attackingRegion, Region defendingRegion) {
		
		//System.out.println("handleBattle (" + attackingRegion + ") vs (" + defendingRegion + ")");
		
		// only allow a single attack
		attackingRegion.unit.hasAttack = false;
		
		float[] battleDamage = calculateBattleDamage(attackingRegion, defendingRegion, Unit.UNIT_WEAKNESS_RANDOM_FACTOR);
		
		float attackerAttackDamage = battleDamage[0];
		float defenderAttackDamage = battleDamage[1];
		
		// update health
		attackingRegion.unit.health -= defenderAttackDamage;
		defendingRegion.unit.health -= attackerAttackDamage;
		
		//System.out.println("battle result (" + attackingRegion.unit + ") (" + defendingRegion.unit + ")");
		
		// special case of both units defeated
		if (defendingRegion.unit.health <= 0 && attackingRegion.unit.health <= 0) {

			// this is unlikely
			
			// determine percentage of attack that went wasted
			float attackerOverkillFactor = -attackingRegion.unit.health / defenderAttackDamage;
			float defenderOverkillFactor = -defendingRegion.unit.health / attackerAttackDamage;
			
			// defender defeated first
			if (defenderOverkillFactor >= attackerOverkillFactor) {
				// update attacker health
				attackingRegion.unit.health += (defenderOverkillFactor * defenderAttackDamage);
			}
			// attacker defeated first
			else {
				// update defender health
				defendingRegion.unit.health += (attackerOverkillFactor * attackerAttackDamage);
			}
		}
		
		// attacker victory
		if (defendingRegion.unit.health <= 0) {

			defendingRegion.player.units--;
			
			// normal attacking unit
			if (!attackingRegion.unit.type.isRanged()) {
				
				attackingRegion.player.regions++;
				defendingRegion.player.regions--;
				
				defendingRegion.player = attackingRegion.player;
				defendingRegion.unit = attackingRegion.unit;
				attackingRegion.unit = null;
				
				return defendingRegion;
			}
			// ranged attacking unit
			else {
				defendingRegion.unit = null;
				return null;
			}
		}
		// defender victory
		else if (attackingRegion.unit.health <= 0) {
			// remove unit from attacking region
			attackingRegion.unit = null;
			
			attackingRegion.player.units--;
			
			return null;
		}
		// no victory
		else {
			// do nothing
			return null;
		}
	}
	
}

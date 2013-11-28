package com.slauson.tactics.ai;

import java.util.Map;

import com.slauson.tactics.model.Island;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.model.Region;

public abstract class AbstractAI {

	/**
	 * Returns likelihood that attacking region could defeat defending region.
	 * @param attacker
	 * @param defender
	 * @return
	 */
	public float calculateBattleLikelihood(Region attacker, Region defender) {
		return -1;
	}
	
	/**
	 * Returns strength of region against neighbors.
	 * @param region
	 * @return
	 */
	public float getRegionStrength(Region region) {
		return -1;
	}
	
	/**
	 * Returns island strength for given player.
	 * @param overworld
	 * @param player
	 * @return
	 */
	public Map<Island, Float> getIslandStrengths(Overworld overworld, Player player) {
		return null;
	}
}

package com.slauson.tactics.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.slauson.tactics.model.Island;
import com.slauson.tactics.model.Neighbor;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.utils.BattleUtils;
import com.slauson.tactics.utils.RegionUtils;

public abstract class AbstractAI {
	
	/**
	 * Returns next move for player to make.
	 * @param overworld
	 * @param player
	 * @return
	 */
	public abstract Move getNextMove(Overworld overworld, Player player);

	/**
	 * Returns changes in region strengths for each possible move.
	 * @param region
	 * @return
	 */
	public Map<Region, Float> checkMoves(Region region) {
		Map<Region, Float> result = new HashMap<Region, Float>();
		
		// for each potential region to move to
		// check change in region strength(s) if unit moved to new region
		// need to factor in unit on region to move to
		
		return result;
	}
	
	/**
	 * Returns likelihood that region could defeat neighboring regions.
	 * @param region
	 * @return
	 */
	public Map<Region, Float> checkAttacks(Region region) {
		Map<Region, Float> result = new HashMap<Region, Float>();
		
		for (Neighbor neighbor : region.neighbors) {
			// check if region can be attacked
			if (!region.player.equals(neighbor.region.player) && ((!region.unit.type.isRanged() && !neighbor.type.isRanged()) || (region.unit.type.isRanged() && neighbor.type.isRanged()))) {
				result.put(neighbor.region, BattleUtils.calculateBattleDamage(region, neighbor.region, 0)[1]);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns likelihood that attacking region could defeat defending region.
	 * (+: attacker more likely to win, -: defender more likely to win)
	 * @param attackingRegion
	 * @param defendingRegion
	 * @return
	 */
	public float calculateBattleLikelihood(Region attackingRegion, Region defendingRegion) {
		if (attackingRegion.unit == null) {
			return -1;
		}
		if (defendingRegion.unit == null) {
			return 1;
		}
		
		float[] battleDamage = BattleUtils.calculateBattleDamage(attackingRegion, defendingRegion, 0);
		
		return battleDamage[0] / defendingRegion.unit.health - battleDamage[1] / attackingRegion.unit.health;
	}
	
	/**
	 * Returns strength of region against neighbors.
	 * (sum of neighboring regions attack strength over region unit health). 
	 * @param region
	 * @return
	 */
	public float getRegionStrength(Region region) {

		if (region.unit == null) {
			return -1;
		}
		
		float result = 0;
		
		for (Neighbor neighbor : region.neighbors) {
			// check if region can be attacked
			if (RegionUtils.canAttack(region, neighbor)) {
				// sum the defending region damage
				result += BattleUtils.calculateBattleDamage(region, neighbor.region, 0)[1];
			}
		}
		
		return result / region.unit.health;
	}
	
	/**
	 * Returns region strengths on island for given player.
	 * @param island
	 * @param player
	 * @return
	 */
	public Map<Region, Float> getRegionStrengths(Island island, Player player) {
		Map<Region, Float> result = new HashMap<Region, Float>(island.regions.size());
		
		for (Region region : island.regions) {
			if (region.player.equals(player)) {
				result.put(region, getRegionStrength(region));
			}
		}
		
		return result;
	}
	
	/**
	 * Returns region strengths on overworld for given player.
	 * @param island
	 * @param player
	 * @return
	 */
	public Map<Region, Float> getRegionStrengths(Overworld overworld, Player player) {
		Map<Region, Float> result = new HashMap<Region, Float>();
		
		for (Island island : overworld.islands) {
			for (Region region : island.regions) {
				if (region.player.equals(player)) {
					result.put(region, getRegionStrength(region));
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Returns strength of player on given island.
	 * (sum of player's region's strengths on island).
	 * @param island
	 * @param player
	 * @return
	 */
	public float getIslandStrength(Island island, Player player) {
		
		float result = 0;
		
		for (Region region : island.regions) {
			if (region.player.equals(player)) {
				result += getRegionStrength(region);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns island strengths for given player.
	 * @param overworld
	 * @param player
	 * @return
	 */
	public Map<Island, Float> getIslandStrengths(Overworld overworld, Player player) {
		
		Map<Island, Float> result = new HashMap<Island, Float>(overworld.islands.size());
		
		// go over each island
		for (Island island : overworld.islands) {
			result.put(island, getIslandStrength(island, player));
		}
		
		return result;
	}
	
	/**
	 * Returns attackable regions from given region.
	 * @param overworld
	 * @param player
	 * @return
	 */
	public List<Region> getAttacks(Region region) {
		
		List<Region> result = new ArrayList<Region>();
		
		for (Neighbor neighbor : region.neighbors) {
			// check if region can be attacked
			if (RegionUtils.canAttack(region, neighbor)) {
				result.add(neighbor.region);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns list of regions given region can move to.
	 * @param region
	 * @return
	 */
	public List<Region> getMoves(Region region) {
		
		List<Region> result = new ArrayList<Region>();
		
		for (Neighbor neighbor : region.neighbors) {
			if (neighbor.type.isMovable() && region.player.equals(neighbor.region.player) && (neighbor.region.unit == null || neighbor.region.unit.hasMove)) {
				result.add(neighbor.region);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns regions owned by player.
	 * @param overworld
	 * @param player
	 * @param hasUnits
	 * @return
	 */
	public List<Region> getPlayerRegions(Overworld overworld, Player player, boolean hasUnits) {
		List<Region> result = new ArrayList<Region>();
		
		for (Island island : overworld.islands) {
			for (Region region : island.regions) {
				if (region.player.equals(player) && (!hasUnits || region.unit != null)) {
					result.add(region);
				}
			}
		}
		
		return result;
	}
	
}

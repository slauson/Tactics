package com.slauson.tactics.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.slauson.tactics.model.Island;
import com.slauson.tactics.model.Neighbor;
import com.slauson.tactics.model.Neighbor.Type;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;

/**
 * Helper methods relating to regions.
 * @author josh
 *
 */
public class RegionUtils {

	/**
	 * Marks neighboring regions depending on available actions for unit at given region.
	 * @param region
	 * @return true if unit has action
	 */
	public static boolean markRegionNeighbors(Region region) {
		
		boolean hasAction = false;
		
		switch (region.unit.type) {
		case CIRCLE:
		case SQUARE:
		case TRIANGLE:
			// mark region neighbors
			for (Neighbor neighbor : region.neighbors) {
				// mark unoccupied direct or ranged inter island regions
				// or occupied regions owned by same player for moves
				if (neighbor.type.isMovable() && region.unit.hasMove && 
						(neighbor.region.unit == null || (region.player == neighbor.region.player && neighbor.region.unit.hasMove)))
				{
					neighbor.region.marked = true;
					hasAction = true;
				}
				// only mark direct regions owned by other players with units for attacks
				else if (neighbor.type == Type.DIRECT && region.unit.hasAttack &&
						region.player != neighbor.region.player && neighbor.region.unit != null)
				{
					neighbor.region.marked = true;
					hasAction = true;
				}
				
			}
			break;
		case RANGED_CIRCLE:
		case RANGED_SQUARE:
		case RANGED_TRIANGLE:
			// mark region neighbors
			for (Neighbor neighbor : region.neighbors) {
				// mark unoccupied direct or ranged inter island regions
				// or occupied regions owned by same player for moves
				if (neighbor.type.isMovable() && region.unit.hasMove && 
						(neighbor.region.unit == null || (region.player == neighbor.region.player && neighbor.region.unit.hasMove)))
				{
					neighbor.region.marked = true;
					hasAction = true;
				}
				// only mark ranged regions owned by other players with units for attacks
				else if (neighbor.type.isRanged() && region.unit.hasAttack &&
						region.player != neighbor.region.player && neighbor.region.unit != null)
				{
					neighbor.region.marked = true;
					hasAction = true;
				}
			}
			break;
		}
		
		return hasAction;
	}
	
	/**
	 * Unmarks all neighboring regions for given region.
	 * @param region
	 */
	public static void unmarkRegionNeighbors(Region region) {
		for (Neighbor neighbor : region.neighbors) {
			neighbor.region.marked = false;
		}
	}
	
	/**
	 * Unmarks all player regions.
	 * @param player
	 */
	public static void unmarkAllRegions(Overworld overworld) {
		for (Region region : overworld.regions) {
			region.marked = false;
		}
	}
	
	/**
	 * Returns true if region can attack given neighbor.
	 * @param region
	 * @param neighbor
	 * @return
	 */
	public static boolean canAttack(Region region, Neighbor neighbor) {
		return region.unit != null && region.unit.hasAttack
				&& neighbor.region.unit != null
				&& !region.player.equals(neighbor.region.player)
				&& ((!region.unit.type.isRanged() && !neighbor.type.isRanged()) || (region.unit.type.isRanged() && neighbor.type.isRanged()));
	}
	
	/**
	 * Returns true if region can move to given neighbor.
	 * @param region
	 * @param neighbor
	 * @return
	 */
	public static boolean canMove(Region region, Neighbor neighbor) {
		return region.unit != null && region.unit.hasMove
				&& neighbor.type.isMovable()
				&& (region.player.equals(neighbor.region.player) || neighbor.region.unit == null)
				&& (neighbor.region.unit == null || neighbor.region.unit.hasMove);
	}
	
	/**
	 * Returns strength of region against neighbors.
	 * (sum of neighboring regions attack strength over region unit health). 
	 * @param region
	 * @return
	 */
	public static float getRegionBattleStrength(Region region) {

		if (region.unit == null) {
			return -1;
		}
		
		float result = 0;
		int numAttacks = 0;
		
		for (Neighbor neighbor : region.neighbors) {
			// check if region can be attacked
			if (RegionUtils.canAttack(region, neighbor)) {
				// sum the difference in attacking region damage and defending region damage
				result += BattleUtils.calculateBattleLikelihood(region, neighbor.region);
				numAttacks++;
			}
		}
		
		return result / numAttacks;
	}
	
	/**
	 * Returns region battle strengths on island for given player.
	 * @param island
	 * @param player
	 * @return
	 */
	public static Map<Region, Float> getRegionBattleStrengths(Island island, Player player) {
		Map<Region, Float> result = new HashMap<Region, Float>();
		
		for (Region region : island.regions) {
			if (region.player.equals(player)) {
				result.put(region, getRegionBattleStrength(region));
			}
		}
		
		return result;
	}
	
	/**
	 * Returns region battle strengths on overworld for given player.
	 * @param island
	 * @param player
	 * @return
	 */
	public static Map<Region, Float> getRegionBattleStrengths(Overworld overworld, Player player) {
		Map<Region, Float> result = new HashMap<Region, Float>();
		
		for (Island island : overworld.islands) {
			for (Region region : island.regions) {
				if (region.player.equals(player)) {
					result.put(region, getRegionBattleStrength(region));
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Returns battle strength of player on given island.
	 * (sum of player's region's strengths on island).
	 * @param island
	 * @param player
	 * @return
	 */
	public static float getIslandBattleStrength(Island island, Player player) {
		
		float result = 0;
		
		for (Region region : island.regions) {
			if (region.player.equals(player)) {
				result += getRegionBattleStrength(region);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns island battle strengths for given player.
	 * @param overworld
	 * @param player
	 * @return
	 */
	public static Map<Island, Float> getIslandBattleStrengths(Overworld overworld, Player player) {
		
		Map<Island, Float> result = new HashMap<Island, Float>(overworld.islands.size());
		
		// go over each island
		for (Island island : overworld.islands) {
			result.put(island, getIslandBattleStrength(island, player));
		}
		
		return result;
	}
	
	/**
	 * Returns strength of region (1 + region health).
	 * @param region
	 * @return
	 */
	public static float getRegionStrength(Region region) {
		return region.unit != null ? 1 + region.unit.health : 1; 
	}
	
	/**
	 * Returns strength of player on given island.
	 * (sum of player's region's strengths over total region strength on island).
	 * @param island
	 * @param player
	 * @return
	 */
	public static float getIslandStrength(Island island, Player player) {
		
		float playerStrength = 0;
		float islandStrength = 0;
		
		for (Region region : island.regions) {
			if (region.player.equals(player)) {
				playerStrength += getRegionStrength(region);
			}
			islandStrength += getRegionStrength(region);
		}
		
		return playerStrength / islandStrength;
	}
	
	/**
	 * Returns island strengths for given player
	 * @param overworld
	 * @param player
	 * @return
	 */
	public static Map<Island, Float> getIslandStrengths(Overworld overworld, Player player) {
		
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
	public static List<Region> getAttacks(Region region) {
		
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
	public static List<Region> getMoves(Region region) {
		
		List<Region> result = new ArrayList<Region>();
		
		for (Neighbor neighbor : region.neighbors) {
			if (RegionUtils.canMove(region, neighbor)) {
				result.add(neighbor.region);
			}
		}
		
		return result;
	}
	
}
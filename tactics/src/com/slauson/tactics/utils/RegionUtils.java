package com.slauson.tactics.utils;

import com.slauson.tactics.model.Neighbor;
import com.slauson.tactics.model.Neighbor.NeighborType;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Region;

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
				else if (neighbor.type == NeighborType.DIRECT && region.unit.hasAttack &&
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
		return region.unit != null && region.unit.hasAttack && !region.player.equals(neighbor.region.player) && ((!region.unit.type.isRanged() && !neighbor.type.isRanged()) || (region.unit.type.isRanged() && neighbor.type.isRanged()));
	}
	
}
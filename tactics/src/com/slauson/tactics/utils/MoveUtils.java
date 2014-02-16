package com.slauson.tactics.utils;

import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;

public class MoveUtils {

	/**
	 * Handles move from one region to another.
	 * @param from
	 * @param to
	 * @returns from region, if it can still move, null otherwise
	 */
	public static Region handleMove(Region from, Region to) {
		// takeover empty region
		if (to.unit == null) {
			
			// update region counts if owned by other player
			if (from.player != to.player) {
				from.player.regions++;
				to.player.regions--;
			}
			
			to.player = from.player;
		}
		
		// swap units
		Unit temp = from.unit;
		from.unit = to.unit;
		to.unit = temp;
		
		// deselect region and unmark neighboring regions
		from.selected = false;
		RegionUtils.unmarkRegionNeighbors(from);
		
		// unit can't move again
		if (from.unit != null) {
			from.unit.hasMove = false;
		}
		if (to.unit != null) {
			to.unit.hasMove = false;
			
			// keep selected if unit can still attack
			if (to.unit.hasAttack) {
				to.selected = true;
				RegionUtils.markRegionNeighbors(to);
				return to;
			}
		}
		
		return null;
	}
	
	public static Region handleReinforcement(Region region) {
		return handleReinforcement(region, null);
	}
	
	/**
	 * Handles reinforcement to given region, using given unit type for regions without units.
	 * @param region
	 * @param unitType
	 * @return region if new unit was added, null otherwise
	 */
	public static Region handleReinforcement(Region region, Unit.Type unitType) {
		// existing unit
		if (region.unit != null) {
			region.unit.health = Unit.MAX_HEALTH;
			region.marked = true;
		}
		// new unit
		else {
			region.unit = new Unit(unitType != null ? unitType : Unit.Type.values()[0], Unit.MAX_HEALTH);
			return region;
		}
		
		return null;
	}
}

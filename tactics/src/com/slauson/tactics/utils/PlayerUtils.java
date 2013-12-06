package com.slauson.tactics.utils;

import com.slauson.tactics.model.Island;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;

public class PlayerUtils {

	/**
	 * Sets all player units as active.
	 * @param player
	 */
	public static void setPlayerUnitsActive(Overworld overworld, Player player) {
		for (Region region : overworld.regions) {
			if (region.unit != null && region.player == player) {
				region.unit.hasAttack = true;
				region.unit.hasMove = true;
			}
		}
	}
	
	/**
	 * Marks regions for player reinforcements.
	 * @param player
	 */
	public static void markPlayerReinforcements(Overworld overworld, Player player) {
		for (Region region : overworld.regions) {
			if (region.player != player || (region.unit != null && region.unit.health == Unit.MAX_HEALTH)) {
				region.marked = true;
			}
		}
	}
	
	/**
	 * Updates reinforcements for given player.
	 * @param player
	 */
	public static void updatePlayerReinforcements(Overworld overworld, Player player) {
		
		int newReinforcements = 0;
		
		for (Island island : overworld.islands) {
			
			boolean playerOwnsIsland = true;
			
			for (int i = 0; i < island.regions.size() && playerOwnsIsland; i++) {
				playerOwnsIsland &= (island.regions.get(i).player == player);
			}
			
			if (playerOwnsIsland) {
				newReinforcements++;
			}
		}
		
		player.reinforcements += newReinforcements;
	}
}

package com.slauson.tactics.ai;

import java.util.List;

import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;
import com.slauson.tactics.utils.PlayerUtils;
import com.slauson.tactics.utils.RegionUtils;
import com.slauson.tactics.utils.Utils;

/**
 * AI class that chooses a random possible move.
 * @author josh
 *
 */
public class RandomAI extends AI {

	@Override
	public Move getNextMove(Overworld overworld, Player player) {
		
		List<Region> regions = PlayerUtils.getPlayerRegions(overworld, player, false);
		
		// choose random region to take action with
		Region region = regions.get(Utils.random().nextInt(regions.size()));
		
		// attack phase
		if (overworld.phase == Overworld.Phase.ATTACK) {
			
			// no unit on region
			if (region.unit == null) {
				return new Move(Move.Type.END_PHASE);
			}
			// attack
			else if (region.unit.hasAttack) {
				
				// get regions to attack
				List<Region> attackRegions = RegionUtils.getAttacks(region);
				
				// no region to attack
				if (attackRegions.size() == 0) {
					return new Move(Move.Type.END_PHASE);
				} else {
					// choose random region to attack
					Region attackRegion = attackRegions.get(Utils.random().nextInt(attackRegions.size()));
	
					return new Move(Move.Type.ATTACK, region, attackRegion);
				}
			}
			// move
			else if (region.unit.hasMove) {
				
				// get regions to move to
				List<Region> moveRegions = RegionUtils.getMoves(region);
				
				// no regions to move to
				if (moveRegions.size() == 0) {
					return new Move(Move.Type.END_PHASE);
				} else {
					// choose random region to move to
					Region moveRegion = moveRegions.get(Utils.random().nextInt(moveRegions.size()));
					
					return new Move(Move.Type.MOVE, region, moveRegion);
				}
			}
			// no possible action for region
			else {
				return new Move(Move.Type.END_PHASE);
			}
		}
		
		// reinforce phase
		if (overworld.phase == Overworld.Phase.REINFORCE) {
			
			// no reinforcements or region cannot be reinforced
			if (player.reinforcements == 0 || (region.unit != null && region.unit.health == Unit.MAX_HEALTH)) {
				return new Move(Move.Type.END_PHASE);
			}
			// reinforce region
			else {
				return new Move(Move.Type.REINFORCE, region);
			}
		}
		return null;
	}

}

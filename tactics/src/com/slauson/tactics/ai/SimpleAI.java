package com.slauson.tactics.ai;

import java.util.Map;

import com.slauson.tactics.model.Neighbor;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;

public class SimpleAI extends AI {
	
	private static final float MIN_MOVE_THRESHOLD = Unit.MAX_HEALTH / 2;

	@Override
	public Move getNextMove(Overworld overworld, Player player) {

		// attack phase
		if (overworld.phase == Overworld.Phase.ATTACK) {
			
			float best = 0;
			Region bestRegion = null;
			Region bestMoveRegion = null;
			Neighbor bestMoveNeighbor = null;
			
			for (Region region : overworld.regions) {
				if (region.player.equals(player)) {
					
					// check attacks
					Map<Region, Float> regionAttacks = checkAttacks(region);
					
					for (Region attackRegion : regionAttacks.keySet()) {
						if (regionAttacks.get(attackRegion) > best) {
							best = regionAttacks.get(attackRegion);
							bestRegion = region;
							bestMoveRegion = attackRegion;
						}
					}
					
					// check moves
					Map<Neighbor, Float> regionMoves = checkMoves(region);
					
					for (Neighbor neighbor : regionMoves.keySet()) {
						if (regionMoves.get(neighbor) > best) {
							best = regionMoves.get(neighbor);
							bestRegion = region;
							bestMoveNeighbor = neighbor;
						}
					}
				}
			}
			
			if (best > MIN_MOVE_THRESHOLD && bestMoveNeighbor != null) {
				System.out.println("best move: " + best + " - " + new Move(Move.Type.MOVE, bestRegion, bestMoveNeighbor.region));
				return new Move(Move.Type.MOVE, bestRegion, bestMoveNeighbor.region);
			} else if (best > MIN_MOVE_THRESHOLD && bestMoveRegion != null) {
				System.out.println("best move: " + best + " - " + new Move(Move.Type.ATTACK, bestRegion, bestMoveRegion));
				return new Move(Move.Type.ATTACK, bestRegion, bestMoveRegion);
			}
			
			return new Move(Move.Type.END_PHASE);
		}
		// reinforce phase
		else if (player.reinforcements > 0) {
			
			// get reinforcements
			Map<Region, ReinforcementType> reinforcements = checkReinforcements(overworld, player);
			
			ReinforcementType bestReinforcement = null;
			Region bestReinforcementRegion = null;
			
			// go through each region based on strength, checking if should reinforce
			for (Region region : reinforcements.keySet()) {
				
				if (bestReinforcement == null || reinforcements.get(region).strengthChange > bestReinforcement.strengthChange) {
					bestReinforcement = reinforcements.get(region);
					bestReinforcementRegion = region;
				}
			}
			
			return new Move(Move.Type.REINFORCE, bestReinforcementRegion, bestReinforcement.unitType);
		}
		
		return new Move(Move.Type.END_PHASE);
	}

}
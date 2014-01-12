package com.slauson.tactics.ai;

import java.util.List;
import java.util.Map;

import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.utils.Utils;

public class AdvancedAI extends AI {
	
	private static final float MIN_SCORE = 0.25f;
	private static final ScoreFactor attackScoreFactor =
			new ScoreFactor()
				.addFactor(ScoreFactor.Type.BATTLE_LIKELIHOOD, 0.5f)
				.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.25f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.125f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH_CHANGE, 0.125f);
	private static final ScoreFactor moveScoreFactor =
			new ScoreFactor()
				.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.25f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.5f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH_CHANGE, 0.25f);
	private static final ScoreFactor reinforcementScoreFactor =
			new ScoreFactor()
				.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.25f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.5f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH_CHANGE, 0.25f);

	@Override
	public Move getNextMove(Overworld overworld, Player player) {
		// attack phase
		if (overworld.phase == Overworld.Phase.ATTACK) {

			List<Move> attacks = getAttacks(overworld, player, MIN_SCORE, attackScoreFactor);
			List<Move> moves = getMoves(overworld, player, MIN_SCORE, moveScoreFactor);
			
			if (attacks.size() == 0 && moves.size() == 0) {
				return new Move(Move.Type.END_PHASE);
			} else {
				int moveIndex = Utils.random().nextInt(attacks.size() + moves.size());
				
				if (moveIndex < attacks.size()) {
					return attacks.get(moveIndex);
				} else {
					return moves.get(moveIndex - attacks.size());
				}
			}
		}
		// reinforce phase
		else if (player.reinforcements > 0) {
			List<Move> reinforcements = getReinforcements(overworld, player, MIN_SCORE, reinforcementScoreFactor);
			
			if (reinforcements.size() > 0) {
				return reinforcements.get(Utils.random().nextInt(reinforcements.size()));
			}
		}
		
		return new Move(Move.Type.END_PHASE);
	}

}

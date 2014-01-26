package com.slauson.tactics.ai;

import java.util.Collection;

import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;

public class AdvancedAI extends AI {
	
	private static final ScoreFactor attackScore =
			new ScoreFactor()
				.addFactor(ScoreFactor.Type.BATTLE_LIKELIHOOD, 0.5f)
				.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.2f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.2f)
				.addFactor(ScoreFactor.Type.RANDOM, 0.1f);
	private static final ScoreFactor attackScoreThreshold =
			new ScoreFactor()
				.addFactor(ScoreFactor.Type.BATTLE_LIKELIHOOD, 0.501f);
	
	private static final ScoreFactor moveScore =
			new ScoreFactor()
				.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.5f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.4f)
				.addFactor(ScoreFactor.Type.RANDOM, 0.1f);
	private static final ScoreFactor moveScoreThreshold =
			new ScoreFactor()
				.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.501f);
	private static final ScoreFactor captureScoreThreshold =
			new ScoreFactor()
				.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.25f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.5f);
	
	private static final ScoreFactor reinforcementScore =
			new ScoreFactor()
				.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.4f)
				.addFactor(ScoreFactor.Type.ISLAND_PERCENTAGE, 0.2f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.2f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH_CHANGE, 0.1f)
				.addFactor(ScoreFactor.Type.RANDOM, 0.1f);
	private static final ScoreFactor reinforcementScoreThreshold=
			new ScoreFactor()
				.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.499f)
				.addFactor(ScoreFactor.Type.ISLAND_PERCENTAGE, 0.25f);

	@Override
	public Move getNextMove(Overworld overworld, Player player) {
		// attack phase
		if (overworld.phase == Overworld.Phase.ATTACK) {

			Collection<Move> attacks = getAttacks(overworld, player, attackScoreThreshold, attackScore);
			Collection<Move> moves = getMoves(overworld, player, moveScoreThreshold, captureScoreThreshold, moveScore);

			if (moves.size() > 0) {
				return moves.iterator().next();
			}
			else if (attacks.size() > 0) {
				return attacks.iterator().next();
			}
			else {
				return new Move(Move.Type.END_PHASE);
			}
		}
		// reinforce phase
		else if (player.reinforcements > 0) {
			Collection<Move> reinforcements = getReinforcements(overworld, player, reinforcementScoreThreshold, reinforcementScore);
			
			if (reinforcements.size() > 0) {
				return reinforcements.iterator().next();
			}
		}
		
		return new Move(Move.Type.END_PHASE);
	}

}

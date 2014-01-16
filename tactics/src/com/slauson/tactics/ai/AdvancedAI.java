package com.slauson.tactics.ai;

import java.util.Collection;
import java.util.Iterator;

import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.utils.Utils;

public class AdvancedAI extends AI {
	
	private static final float MIN_SCORE = 0f;
	private static final ScoreFactor attackScoreFactor =
			new ScoreFactor()
				.addFactor(ScoreFactor.Type.BATTLE_LIKELIHOOD, 0.5f)
				.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.1f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.25f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH_CHANGE, 0.1f)
				.addFactor(ScoreFactor.Type.RANDOM, 0.05f);
	private static final ScoreFactor moveScoreFactor =
			new ScoreFactor()
				.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.5f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.35f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH_CHANGE, 0.1f)
				.addFactor(ScoreFactor.Type.RANDOM, 0.05f);
	private static final ScoreFactor reinforcementScoreFactor =
			new ScoreFactor()
				.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.4f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.45f)
				.addFactor(ScoreFactor.Type.ISLAND_STRENGTH_CHANGE, 0.1f)
				.addFactor(ScoreFactor.Type.RANDOM, 0.05f);

	@Override
	public Move getNextMove(Overworld overworld, Player player) {
		// attack phase
		if (overworld.phase == Overworld.Phase.ATTACK) {

			Collection<Move> attacks = getAttacks(overworld, player, MIN_SCORE, attackScoreFactor);
			Collection<Move> moves = getMoves(overworld, player, MIN_SCORE, moveScoreFactor);

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
			Move[] reinforcements = (Move[]) getReinforcements(overworld, player, MIN_SCORE, reinforcementScoreFactor).toArray();
			
			if (reinforcements.length > 0) {
				return reinforcements[Utils.random().nextInt(reinforcements.length)];
			}
		}
		
		return new Move(Move.Type.END_PHASE);
	}

}

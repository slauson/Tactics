package com.slauson.tactics.ai;

import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;

/**
 * AI class that chooses a random possible move.
 * @author josh
 *
 */
public class RandomAI extends AI {

	@Override
	public Move getNextMove(Overworld overworld, Player player) {
		
		if (overworld.phase == Overworld.Phase.ATTACK) {
			return getRandomMove(overworld, player, false, false, Move.Type.ATTACK, Move.Type.MOVE);
		}
		else {
			return getRandomReinforcement(overworld, player, false, false);
		}
	}
}
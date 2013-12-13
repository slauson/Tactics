package com.slauson.tactics.ai;

import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;

public class SimpleAI extends AbstractAI {

	@Override
	public Move getNextMove(Overworld overworld, Player player) {

		// get islands ordered by player strength
		
		// attack phase
		if (overworld.phase == Overworld.Phase.ATTACK) {
			
			// go through each island
			// go through each region based on strength, checking potential moves
			
		}
		// reinforce phase
		else {
			
			// go through each island
			// go through each region based on strength, checking if should reinforce
			
		}
		
		return null;
	}

}
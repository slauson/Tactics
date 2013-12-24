package com.slauson.tactics.ai;

import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;

public abstract class AI {
	
	/**
	 * Returns next move for player to make.
	 * @param overworld
	 * @param player
	 * @return
	 */
	public abstract Move getNextMove(Overworld overworld, Player player);
	
}

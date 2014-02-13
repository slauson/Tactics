package com.slauson.tactics.ai;

import java.util.Collection;

import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.utils.RegionUtils;

/**
 * Standard advanced AI.
 */
public class AdvancedAI extends AI {

	private ScoreFactorBuilder scoreFactorBuilder;
	
	private ScoreFactor attackScore, moveScore, reinforcementScore;
	private ScoreFactor attackScoreThreshold, moveScoreThreshold, captureScoreThreshold, reinforcementScoreThreshold;
	
	private static final float PLAYER_PERCENTAGE_LIGHT_THRESHOLD = 0.25f;
	private static final float PLAYER_PERCENTAGE_HEAVY_THRESHOLD = 0.75f;
	
	private static final int PLAYER_REINFORCEMENTS_LIGHT_THRESHOLD = 1;
	private static final int PLAYER_REINFORCEMENTS_HEAVY_THRESHOLD = 5;
	
	public AdvancedAI(ScoreFactorBuilder.Level level) {
		scoreFactorBuilder = new ScoreFactorBuilder(level);
		
		attackScore = scoreFactorBuilder.build(ScoreFactorBuilder.Type.ATTACK);
		moveScore = scoreFactorBuilder.build(ScoreFactorBuilder.Type.MOVE);
		reinforcementScore = scoreFactorBuilder.build(ScoreFactorBuilder.Type.REINFORCE);
	}
	
	@Override
	public Move getNextMove(Overworld overworld, Player player) {
		
		// recompute score factors
		recomputeScoreThresholds(overworld, player);
		
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
	
	/**
	 * Recomputes score thresholds based on overworld and player data.
	 * @param overworld
	 * @param player
	 */
	private void recomputeScoreThresholds(Overworld overworld, Player player) {
		
		// recompute attack/move/capture score factor thresholds
		if (overworld.phase == Overworld.Phase.ATTACK) {
			
			// get updated player percentage
			float playerPercentage = RegionUtils.getOverworldPercentage(overworld, player);
			ScoreFactorBuilder.Focus focus = ScoreFactorBuilder.Focus.NORMAL;
			
			if (playerPercentage <= PLAYER_PERCENTAGE_LIGHT_THRESHOLD) {
				focus = ScoreFactorBuilder.Focus.LIGHT;
			} else if (playerPercentage >= PLAYER_PERCENTAGE_HEAVY_THRESHOLD) {
				focus = ScoreFactorBuilder.Focus.LIGHT;
			} else {
				// TODO check other things here?
			}
			
			// update score thresholds
			attackScoreThreshold = scoreFactorBuilder.buildThreshold(ScoreFactorBuilder.Type.ATTACK, focus);
			moveScoreThreshold = scoreFactorBuilder.buildThreshold(ScoreFactorBuilder.Type.MOVE, focus);
			captureScoreThreshold = scoreFactorBuilder.buildThreshold(ScoreFactorBuilder.Type.CAPTURE, focus);
		}
		// recompute reinforcement score factor threshold
		else if (player.reinforcements > 0) {
		
			ScoreFactorBuilder.Focus focus = ScoreFactorBuilder.Focus.NORMAL;
			
			if (player.reinforcements <= PLAYER_REINFORCEMENTS_LIGHT_THRESHOLD) {
				focus = ScoreFactorBuilder.Focus.LIGHT;
			} else if (player.reinforcements >= PLAYER_REINFORCEMENTS_HEAVY_THRESHOLD) {
				focus = ScoreFactorBuilder.Focus.HEAVY;
			}
			
			// update score threshold
			reinforcementScoreThreshold = scoreFactorBuilder.buildThreshold(ScoreFactorBuilder.Type.REINFORCE, focus);
		}
	}

}

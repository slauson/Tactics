package com.slauson.tactics.ai;

import java.util.List;

import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;
import com.slauson.tactics.utils.PlayerUtils;
import com.slauson.tactics.utils.RegionUtils;
import com.slauson.tactics.utils.Utils;

public abstract class AI {
	
	/**
	 * Returns next move for player to make.
	 * @param overworld
	 * @param player
	 * @return
	 */
	public abstract Move getNextMove(Overworld overworld, Player player);
	
	/**
	 * Returns random move (attack or move).
	 * @param overworld overworld
	 * @param player player
	 * @param forceAttack true if we want to force an attack if possible
	 * @param forceMove true if we want to force a move if possible
	 * @param types types of moves allowed (attack or move)
	 * @return random move
	 */
	protected static  Move getRandomMove(Overworld overworld, Player player, boolean forceAttack, boolean forceMove, Move.Type...types) {
		
		List<Region> regions = PlayerUtils.getPlayerRegions(overworld, player, false);
		
		// check possible moves
		boolean allowAttack = false;
		boolean allowMove = false;
		for (Move.Type type : types) {
			switch (type) {
			case ATTACK:
				allowAttack = true;
				break;
			case MOVE:
				allowMove = true;
				break;
			}
		}
		
		// choose random region to take action with
		int regionIndex = Utils.random().nextInt(regions.size());
		int regionStartIndex = regionIndex;
		
		// check if region can take action
		
		// get regions to attack
		List<Region> attackRegions = RegionUtils.getAttacks(regions.get(regionIndex));
		List<Region> moveRegions = RegionUtils.getMoves(regions.get(regionIndex));

		// if region can't attack or move, check other regions if we force an attack or move
		while ((allowAttack && forceAttack && attackRegions.size() == 0) ||
				(allowMove && forceMove && moveRegions.size() == 0))
		{
			
			regionIndex++;
			
			if (regionIndex >= regions.size()) {
				regionIndex = 0;
			}
			
			// exit loop if we've checked all regions
			if (regionIndex == regionStartIndex) {
				break;
			}
			
			attackRegions = RegionUtils.getAttacks(regions.get(regionIndex));
			moveRegions = RegionUtils.getMoves(regions.get(regionIndex));
		}
		
		// region to attack
		if (allowAttack && attackRegions.size() > 0) {
			// choose random region to attack
			Region attackRegion = attackRegions.get(Utils.random().nextInt(attackRegions.size()));

			return new Move(Move.Type.ATTACK, regions.get(regionIndex), attackRegion);
		}
		else if (allowMove && moveRegions.size() > 0) {
			// choose random region to move to
			Region moveRegion = moveRegions.get(Utils.random().nextInt(moveRegions.size()));

			return new Move(Move.Type.MOVE, regions.get(regionIndex), moveRegion);
		}
		
		return new Move(Move.Type.END_PHASE);
	}
	
	/**
	 * Returns random reinforcement.
	 * @param overworld overworld
	 * @param player player
	 * @param forceReinforcement true if we want to force reinforcements to happen, if possible
	 * @param onlyNew true if we only want new reinforcements
	 * @return random reinforcement
	 */
	protected static Move getRandomReinforcement(Overworld overworld, Player player, boolean forceReinforcement, boolean onlyNew) {
		
		// player can't make any reinforcements
		if (player.reinforcements <= 0) {
			return new Move(Move.Type.END_PHASE);
		}
		
		List<Region> regions = PlayerUtils.getPlayerRegions(overworld, player, false);
		
		// choose random region to take action with
		int regionIndex = Utils.random().nextInt(regions.size());
		int regionStartIndex = regionIndex;
		
		// check if region can take action
		
		// if region can't be reinforced, check other regions if we force reinforcements
		while (forceReinforcement && ((onlyNew && regions.get(regionIndex).unit != null) || (regions.get(regionIndex).unit != null && regions.get(regionIndex).unit.health == Unit.MAX_HEALTH))) {
			regionIndex++;
			
			if (regionIndex >= regions.size()) {
				regionIndex = 0;
			}
			
			// exit loop if we've checked all regions
			if (regionIndex == regionStartIndex) {
				break;
			}
		}
		
		// reinforce new region
		if (regions.get(regionIndex).unit == null) {
			return new Move(Move.Type.REINFORCE, regions.get(regionIndex), Unit.UnitType.values()[Utils.random().nextInt(Unit.UnitType.values().length)]);
		}
		// reinforce existing region
		else if (!onlyNew && regions.get(regionIndex).unit.health < Unit.MAX_HEALTH) {
			return new Move(Move.Type.REINFORCE, regions.get(regionIndex));
		}
		
		return new Move(Move.Type.END_PHASE);
	}
	
}

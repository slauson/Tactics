package com.slauson.tactics.ai;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.slauson.tactics.model.Neighbor;
import com.slauson.tactics.model.Overworld;
import com.slauson.tactics.model.Player;
import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;
import com.slauson.tactics.utils.BattleUtils;
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
	 * Returns collection of moves for given score factor,
	 * ordered by descending score.
	 * @param overworld
	 * @param player
	 * @param scoreThreshold
	 * @param scoreFactor
	 * @return
	 */
	protected static Collection<Move> getMoves(Overworld overworld, Player player, ScoreFactor scoreThreshold, ScoreFactor captureScoreThreshold, ScoreFactor scoreFactor) {
		
		Map<Float, Move> moves = new TreeMap<Float, Move>();
		
		// go through each region
		for (Region region : overworld.regions) {
			if (region.player.equals(player) && region.unit != null && region.unit.hasMove) {
				
				// go through each neighbor
				for (Neighbor neighbor : region.neighbors) {
					if (RegionUtils.canMove(region, neighbor)) {

						ScoreFactor score = new ScoreFactor();
						float scoreValue = 0;
						boolean aboveThreshold = true;
						
						// get each score type
						for (ScoreFactor.Type type : ScoreFactor.Type.values()) {
							if (scoreThreshold.usesType(type) || scoreFactor.usesType(type)) {
								switch (type) {
								case BATTLE_STRENGTH_CHANGE:
									score.addFactor(type, getBattleStrengthChange(region, neighbor, true));
									break;
								case ISLAND_PERCENTAGE:
									score.addFactor(type, RegionUtils.getIslandPercentage(region.island, player));
									break;
								case ISLAND_STRENGTH:
									score.addFactor(type, RegionUtils.getIslandStrength(region.island, player));
									break;
								case RANDOM:
									score.addFactor(type, Utils.random().nextFloat());
									break;
								case BATTLE_LIKELIHOOD:
								case ISLAND_STRENGTH_CHANGE:
									// ignore
									break;
								}
								
								// check if score meets threshold
								// capture threshold
								if (!neighbor.region.player.equals(region.player) && neighbor.region.unit == null) {
									if (captureScoreThreshold.usesType(type)) {
										aboveThreshold &= score.getScore(type) >= captureScoreThreshold.getScore(type);
									}
								}
								// regular move threshold
								else {
									if (scoreThreshold.usesType(type)) {
										aboveThreshold &= score.getScore(type) >= scoreThreshold.getScore(type);
									}
								}
								
								// update score value
								if (scoreFactor.usesType(type)) {
									scoreValue += score.getScore(type) * scoreFactor.getScore(type);
								}
							}
						}
						
						// score above threshold
						if (aboveThreshold) {
							moves.put(scoreValue, new Move(Move.Type.MOVE, region, neighbor.region));
							System.out.println(String.format("\tpossible move %f: %s", scoreValue, moves.get(scoreValue)));
							System.out.println("\t\t" + score);
						}
					}
				}
			}
		}
		
		return moves.values();
	}
	
	/**
	 * Returns collection of attacks for given score factor,
	 * ordered by descending score.
	 * @param overworld
	 * @param player
	 * @param scoreThreshold
	 * @param scoreFactor
	 * @return
	 */
	protected static Collection<Move> getAttacks(Overworld overworld, Player player, ScoreFactor scoreThreshold, ScoreFactor scoreFactor) {
		
		Map<Float, Move> attacks = new TreeMap<Float, Move>();
		
		// go through each region
		for (Region region : overworld.regions) {
			if (region.player.equals(player) && region.unit != null && region.unit.hasAttack) {
				
				// check each neighbor
				for (Neighbor neighbor : region.neighbors) {
					if (RegionUtils.canAttack(region, neighbor)) {
						
						ScoreFactor score = new ScoreFactor();
						float scoreValue = 0;
						boolean aboveThreshold = true;
						
						// get each score type
						for (ScoreFactor.Type type : ScoreFactor.Type.values()) {
							if (scoreThreshold.usesType(type) || scoreFactor.usesType(type)) {
								switch (type) {
								case BATTLE_STRENGTH_CHANGE:
									if (neighbor.type != Neighbor.Type.RANGED) {
										score.addFactor(type, getBattleStrengthChange(region, neighbor, false));
									} else {
										score.addFactor(type, 0.5f);
									}
									break;
								case BATTLE_LIKELIHOOD:
									score.addFactor(type, BattleUtils.calculateBattleLikelihood(region, neighbor.region));
									break;
								case ISLAND_PERCENTAGE:
									score.addFactor(type, RegionUtils.getIslandPercentage(region.island, player));
									break;
								case ISLAND_STRENGTH:
									score.addFactor(type, RegionUtils.getIslandStrength(region.island, player));
									break;
								case RANDOM:
									score.addFactor(type, Utils.random().nextFloat());
									break;
								case ISLAND_STRENGTH_CHANGE:
									// ignore
									break;
								}
								
								// check if score meets threshold
								if (scoreThreshold.usesType(type)) {
									aboveThreshold &= score.getScore(type) >= scoreThreshold.getScore(type);
								}
								
								// update score value
								if (scoreFactor.usesType(type)) {
									scoreValue += score.getScore(type) * scoreFactor.getScore(type);
								}
							}
						}
						
						// score above threshold
						if (aboveThreshold) {
							attacks.put(scoreValue, new Move(Move.Type.ATTACK, region, neighbor.region));
							System.out.println(String.format("\tpossible attack %f: %s", scoreValue, attacks.get(scoreValue)));
							System.out.println("\t\t" + score);
						}
					}
				}
			}
		}
		
		return attacks.values();
	}
	
	/**
	 * Returns collection of reinforcements for given score factor,
	 * ordered by descending score.
	 * @param overworld
	 * @param player
	 * @param minScore
	 * @param scoreFactor
	 * @return
	 */
	protected static Collection<Move> getReinforcements(Overworld overworld, Player player, ScoreFactor scoreThreshold, ScoreFactor scoreFactor) {
		
		Map<Float, Move> reinforcements = new TreeMap<Float, Move>();
		
		// go through each region
		for (Region region : overworld.regions) {
			if (region.player.equals(player) && (region.unit == null || region.unit.health < Unit.MAX_HEALTH)) {
				
				// get optimal reinforcement type for region
				ReinforcementType reinforcementType = getBestReinforcement(region);
				
				ScoreFactor score = new ScoreFactor();
				float scoreValue = 0;
				boolean aboveThreshold = true;
				
				// get each score type
				for (ScoreFactor.Type type : ScoreFactor.Type.values()) {
					if (scoreThreshold.usesType(type) || scoreFactor.usesType(type)) {
						switch (type) {
						case BATTLE_STRENGTH_CHANGE:
							score.addFactor(type, reinforcementType.strengthChange);
							break;
						case ISLAND_PERCENTAGE:
							score.addFactor(type, RegionUtils.getIslandPercentage(region.island, player));
							break;
						case ISLAND_STRENGTH:
							score.addFactor(type, RegionUtils.getIslandStrength(region.island, player));
							break;
						case ISLAND_STRENGTH_CHANGE:
							score.addFactor(type, getIslandStrengthChange(region, reinforcementType, player));
							break;
						case RANDOM:
							score.addFactor(type, Utils.random().nextFloat());
							break;
						case BATTLE_LIKELIHOOD:
							// ignore
							break;
						}
						
						// check if score meets threshold
						if (scoreThreshold.usesType(type)) {
							aboveThreshold &= score.getScore(type) >= scoreThreshold.getScore(type);
						}
						
						// update score value
						if (scoreFactor.usesType(type)) {
							scoreValue += score.getScore(type) * scoreFactor.getScore(type);
						}
					}
				}
				
				// score above threshold
				if (aboveThreshold) {
					reinforcements.put(scoreValue, new Move(Move.Type.REINFORCE, region, null, reinforcementType.unitType));
					System.out.println(String.format("\tpossible reinforcement %f: %s", scoreValue, reinforcements.get(scoreValue)));
					System.out.println("\t\t" + score);
				}
			}
		}
		
		return reinforcements.values();
	}

	
	/**
	 * Returns changes in region strengths for each possible move.
	 * @param region
	 * @return
	 */
	protected static Map<Neighbor, Float> checkMoves(Region region) {
		Map<Neighbor, Float> result = new HashMap<Neighbor, Float>();
		
		for (Neighbor neighbor : region.neighbors) {
			// check if neighbor can be moved to
			if (RegionUtils.canMove(region, neighbor)) {
				// get current strengths of region, neighbor
				float regionStrength = RegionUtils.getRegionBattleStrength(region);
				float neighborStrength = RegionUtils.getRegionBattleStrength(neighbor.region);
				
				// swap units, see change in strengths
				Unit tempUnit = region.unit;
				region.unit = neighbor.region.unit;
				neighbor.region.unit = tempUnit;
				
				Player tempPlayer = region.player;
				region.player = neighbor.region.player;
				neighbor.region.player = tempPlayer;
				
				float regionMoveStrength = RegionUtils.getRegionBattleStrength(region);
				float neighborMoveStrength = RegionUtils.getRegionBattleStrength(neighbor.region);
				
				// put units back
				tempUnit = region.unit;
				region.unit = neighbor.region.unit;
				neighbor.region.unit = tempUnit;
				
				tempPlayer = region.player;
				region.player = neighbor.region.player;
				neighbor.region.player = tempPlayer;
				
				result.put(neighbor, regionMoveStrength + neighborMoveStrength - regionStrength - neighborStrength);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns likelihood that region could defeat neighboring regions.
	 * @param region
	 * @return
	 */
	protected static Map<Region, Float> checkAttacks(Region region) {
		Map<Region, Float> result = new HashMap<Region, Float>();
		
		for (Neighbor neighbor : region.neighbors) {
			// check if region can be attacked
			if (RegionUtils.canAttack(region, neighbor)) {
				float[] battleDamage = BattleUtils.calculateBattleDamage(region, neighbor.region, 0);
				result.put(neighbor.region, battleDamage[0] - battleDamage[1]);
			}
		}
		
		return result;
	}
	
	protected static class ReinforcementType {
		public float strengthChange = -1;
		public Unit.Type unitType = null;
	}
	
	/**
	 * Returns change in battle strength for player regions
	 * @return
	 */
	protected static Map<Region, ReinforcementType> checkReinforcements(Overworld overworld, Player player) {
		
		Map<Region, ReinforcementType> result = new HashMap<Region, ReinforcementType>(); 
		
		for (Region region : overworld.regions) {
			if (region.player.equals(player)) {
				result.put(region, getBestReinforcement(region));
			}
		}
		
		return result;
	}
	
	/**
	 * Returns best reinforcement for given region.
	 * @param region
	 * @return
	 */
	protected static ReinforcementType getBestReinforcement(Region region) {
		
		ReinforcementType result = new ReinforcementType();
		
		// existing unit
		if (region.unit != null) {
			float originalRegionHealth = region.unit.health;
			float originalBattleStrength = RegionUtils.getRegionBattleStrength(region);
			
			// simulate max health
			region.unit.health = Unit.MAX_HEALTH;
			result.strengthChange = RegionUtils.getRegionBattleStrength(region) - originalBattleStrength;
			region.unit.health = originalRegionHealth;
		}
		// new unit
		else {
			
			// get optimal unit type
			// check each possible unit type
			for (Unit.Type unitType : Unit.Type.values()) {
				region.unit = new Unit(unitType, Unit.MAX_HEALTH);
				
				float regionBattleStrength = RegionUtils.getRegionBattleStrength(region);
				
				if (result.unitType == null || regionBattleStrength > result.strengthChange) {
					result.strengthChange = regionBattleStrength;
					result.unitType = unitType;
				}
			}
			
			// reset region to have no unit
			region.unit = null;
		}
		
		return result;
	}
	
	
	
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
			return new Move(Move.Type.REINFORCE, regions.get(regionIndex), Unit.Type.values()[Utils.random().nextInt(Unit.Type.values().length)]);
		}
		// reinforce existing region
		else if (!onlyNew && regions.get(regionIndex).unit.health < Unit.MAX_HEALTH) {
			return new Move(Move.Type.REINFORCE, regions.get(regionIndex));
		}
		
		return new Move(Move.Type.END_PHASE);
	}
	
	/**
	 * Returns change in battle strength if unit at region switches places with neighbor. 
	 * @param region
	 * @param neighbor
	 * @param includeNeighborStrengthChange true if change in neighbor strength should be included
	 * @return
	 */
	private static float getBattleStrengthChange(Region region, Neighbor neighbor, boolean includeNeighborStrengthChange) {
		// get current strengths of region, neighbor
		float regionStrength = RegionUtils.getRegionBattleStrength(region);
		float neighborStrength = RegionUtils.getRegionBattleStrength(neighbor.region);
		
		// swap units, see change in strengths
		Unit tempUnit = region.unit;
		region.unit = neighbor.region.unit;
		neighbor.region.unit = tempUnit;
		
		Player tempPlayer = region.player;
		region.player = neighbor.region.player;
		neighbor.region.player = tempPlayer;
		
		float regionMoveStrength = RegionUtils.getRegionBattleStrength(region);
		float neighborMoveStrength = RegionUtils.getRegionBattleStrength(neighbor.region);
		
		// put units back
		tempUnit = region.unit;
		region.unit = neighbor.region.unit;
		neighbor.region.unit = tempUnit;
		
		tempPlayer = region.player;
		region.player = neighbor.region.player;
		neighbor.region.player = tempPlayer;
		
		if (includeNeighborStrengthChange) {
			return (regionMoveStrength + neighborMoveStrength - regionStrength - neighborStrength + 2) / 4;
		} else {
			return (regionMoveStrength - regionStrength + 1) / 2;
		}
	}
	
	/**
	 * Returns change in island strengths if unit at region switches place with neighbor.
	 * @param region
	 * @param neighbor
	 * @param player
	 * @return
	 */
	private static float getIslandStrengthChange(Region region, Neighbor neighbor, Player player) {
		
		// same island
		if (region.island.equals(neighbor.region.island)) {

			// island strength is always same
			return 0;
		}
		// different island
		else {
			
			// island strength is always same
			return 0;
		}
	}
	
	/**
	 * Returns change in island strength for given reinforcement
	 * @param region
	 * @param reinforcementType
	 * @param player
	 * @return
	 */
	private static float getIslandStrengthChange(Region region, ReinforcementType reinforcementType, Player player) {
		
		float regionUnitHealth = -1f;
		float regionIslandStrength = RegionUtils.getIslandStrength(region.island, player);
		
		// add reinforcement, see change in island strength
		if (reinforcementType.unitType != null) {
			region.unit = new Unit(reinforcementType.unitType, Unit.MAX_HEALTH);
		} else {
			regionUnitHealth = region.unit.health;
			region.unit.health = Unit.MAX_HEALTH;
		}
		
		float regionIslandReinforcementStrength = RegionUtils.getIslandStrength(region.island, player);
		
		// remove reinforcement
		if (reinforcementType.unitType != null) {
			region.unit = null;
		} else {
			region.unit.health = regionUnitHealth;
		}

		return (regionIslandReinforcementStrength - regionIslandStrength) / regionIslandReinforcementStrength;
	}
	
}

package com.slauson.tactics.ai;

public class ScoreFactorBuilder {

	public static enum Type {
		ATTACK,
		MOVE,
		CAPTURE,
		REINFORCE
	}
	
	public static enum Level {
		EASY,
		NORMAL,
		HARD
	}
	
	public static enum Focus {
		LIGHT,
		NORMAL,
		HEAVY
	}

	private Level level;
	
	public ScoreFactorBuilder(Level level) {
		this.level = level;
	}
	
	public ScoreFactor build(Type type) {
		switch (level) {
		case EASY:
			switch (type) {
			case ATTACK:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_LIKELIHOOD, 0.3f)
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.1f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.1f)
					.addFactor(ScoreFactor.Type.RANDOM, 0.5f);
			case MOVE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.3f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.2f)
					.addFactor(ScoreFactor.Type.RANDOM, 0.5f);
			case CAPTURE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.3f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.2f)
					.addFactor(ScoreFactor.Type.RANDOM, 0.5f);
			case REINFORCE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.2f)
					.addFactor(ScoreFactor.Type.ISLAND_PERCENTAGE, 0.1f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.1f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH_CHANGE, 0.1f)
					.addFactor(ScoreFactor.Type.RANDOM, 0.5f);
			default:
				return new ScoreFactor();
			}
		case NORMAL:
		default:
			switch (type) {
			case ATTACK:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_LIKELIHOOD, 0.4f)
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.2f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.2f)
					.addFactor(ScoreFactor.Type.RANDOM, 0.2f);
			case MOVE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.4f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.4f)
					.addFactor(ScoreFactor.Type.RANDOM, 0.2f);
			case CAPTURE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.4f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.4f)
					.addFactor(ScoreFactor.Type.RANDOM, 0.2f);
			case REINFORCE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.3f)
					.addFactor(ScoreFactor.Type.ISLAND_PERCENTAGE, 0.2f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.2f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH_CHANGE, 0.1f)
					.addFactor(ScoreFactor.Type.RANDOM, 0.2f);
			default:
				return new ScoreFactor();
			}
		case HARD:
			switch (type) {
			case ATTACK:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_LIKELIHOOD, 0.5f)
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.2f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.2f)
					.addFactor(ScoreFactor.Type.RANDOM, 0.1f);
			case MOVE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.5f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.4f)
					.addFactor(ScoreFactor.Type.RANDOM, 0.1f);
			case CAPTURE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.5f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.4f)
					.addFactor(ScoreFactor.Type.RANDOM, 0.1f);
			case REINFORCE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.4f)
					.addFactor(ScoreFactor.Type.ISLAND_PERCENTAGE, 0.2f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.2f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH_CHANGE, 0.1f)
					.addFactor(ScoreFactor.Type.RANDOM, 0.1f);
			default:
				return new ScoreFactor();
			}
		}
	}
	
	public ScoreFactor buildThreshold(Type type, Focus focus) {
		switch (focus) {
		case LIGHT:
			switch (type) {
			case ATTACK:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_LIKELIHOOD, 0.601f);
			case MOVE:
				return new ScoreFactor()
						.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.601f);
			case CAPTURE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.35f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.6f);
			case REINFORCE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.599f)
					.addFactor(ScoreFactor.Type.ISLAND_PERCENTAGE, 0.35f);
			default:
				return new ScoreFactor();
			}
		case NORMAL:
		default:
			switch (type) {
			case ATTACK:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_LIKELIHOOD, 0.501f);
			case MOVE:
				return new ScoreFactor()
						.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.501f);
			case CAPTURE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.25f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.5f);
			case REINFORCE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.499f)
					.addFactor(ScoreFactor.Type.ISLAND_PERCENTAGE, 0.25f);
			default:
				return new ScoreFactor();
			}
		case HEAVY:
			switch (type) {
			case ATTACK:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_LIKELIHOOD, 0.401f);
			case MOVE:
				return new ScoreFactor()
						.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.401f);
			case CAPTURE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.15f)
					.addFactor(ScoreFactor.Type.ISLAND_STRENGTH, 0.4f);
			case REINFORCE:
				return new ScoreFactor()
					.addFactor(ScoreFactor.Type.BATTLE_STRENGTH_CHANGE, 0.399f)
					.addFactor(ScoreFactor.Type.ISLAND_PERCENTAGE, 0.15f);
			default:
				return new ScoreFactor();
			}
		}
	}
}

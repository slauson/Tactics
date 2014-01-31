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

	private Type type;
	private Level level;
	
	public ScoreFactorBuilder(Type type, Level level) {
		this.type = type;
		this.level = level;
	}
	
	public ScoreFactor build() {
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
	
	public ScoreFactor buildThreshold() {
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
	}
}

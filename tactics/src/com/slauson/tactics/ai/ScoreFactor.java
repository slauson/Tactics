package com.slauson.tactics.ai;

/**
 * Factors that go into choosing a move. 
 */
public class ScoreFactor {
	
	public static enum Type {
		BATTLE_STRENGTH_CHANGE,
		BATTLE_LIKELIHOOD,
		ISLAND_STRENGTH_CHANGE,
		ISLAND_STRENGTH,
		ISLAND_PERCENTAGE,
		RANDOM
	}
	
	public float[] scores;
	
	public ScoreFactor() {
		this.scores = new float[Type.values().length];
		
		// initialize scores
		for (int i = 0; i < scores.length; i++) {
			scores[i] = 0f;
		}
	}
	
	/**
	 * Adds factor with given score to score factor.
	 * @param type
	 * @param score
	 * @return
	 */
	public ScoreFactor addFactor(Type type, float score) {
		scores[type.ordinal()] = score;
		
		return this;
	}
	
	/**
	 * Returns score for given type
	 * @param type
	 * @return
	 */
	public float getScore(Type type) {
		return scores[type.ordinal()];
	}
	
	/**
	 * Returns true if score factor uses given type
	 * @param type
	 * @return
	 */
	public boolean usesType(Type type) {
		return scores[type.ordinal()] > 0;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (Type type : Type.values()) {
			if (scores[type.ordinal()] > 0) {
				if (builder.length() > 0) {
					builder.append(", ");
				}
				builder.append(String.format("%s: %f", type.name(), scores[type.ordinal()]));
			}
		}
		
		return builder.toString();
	}
}
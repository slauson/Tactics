package com.slauson.tactics.ai;

import com.slauson.tactics.model.Region;

/**
 * A type of move to make.
 * @author josh
 *
 */
public class Move {

	public enum Type {
		ATTACK,
		MOVE,
		REINFORCE,
		END_PHASE
	}
	
	public Type type;
	public Region region;
	public Region otherRegion;
	
	public Move(Type type) {
		this(type, null, null);
	}
	
	public Move(Type type, Region region) {
		this(type, region, null);
	}
	
	public Move(Type type, Region region, Region otherRegion) {
		this.type = type;
		this.region = region;
		this.otherRegion = otherRegion;
	}
}

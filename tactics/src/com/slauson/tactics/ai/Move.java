package com.slauson.tactics.ai;

import com.slauson.tactics.model.Region;
import com.slauson.tactics.model.Unit;
import com.slauson.tactics.model.Unit.Type;

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
		END_PHASE,
		DUMMY
	}
	
	public Type type;
	public Region region;
	public Region otherRegion;
	public Unit.Type unitType;
	public int numPhases;
	
	public Move(Type type) {
		this(type, null, null, null);
	}
	
	public Move(Type type, Region region) {
		this(type, region, null, null);
	}
	
	public Move(Type type, Region region, Unit.Type unitType) {
		this(type, region, null, unitType);
	}
	
	public Move(Type type, Region region, Region otherRegion) {
		this(type, region, otherRegion, null);
	}
	
	public Move(Move.Type type, Region region, Region otherRegion, Unit.Type unitType) {
		this.type = type;
		this.region = region;
		this.otherRegion = otherRegion;
		this.unitType = unitType;
		
		switch (type) {
		case ATTACK:
		case MOVE:
		case REINFORCE:
			numPhases = 2;
			break;
		case END_PHASE:
			numPhases = 1;
			break;
		case DUMMY:
			numPhases = 0;
			break;
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s, %s, %s, %d",
				this.type.name(),
				region != null ? region.toString() : "null",
				otherRegion != null ? otherRegion.toString() : "null",
				unitType != null ? unitType.name() : "null",
				numPhases
			);
	}
}

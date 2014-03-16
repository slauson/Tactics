package com.slauson.tactics.event;

import com.slauson.tactics.model.Region;

public class Event {
	
	public enum Type {
		BATTLE_BEGIN,
		BATTLE_END;
	}
	
	public Type type;
	public Region region1, region2;
	
	public Event(Type type) {
		this(type, null, null);
	}
	
	public Event(Type type, Region region) {
		this(type, region, null);
	}
	
	public Event(Type type, Region region1, Region region2) {
		this.type = type;
		this.region1 = region1;
		this.region2 = region2;
	}
}

package com.slauson.tactics.model;

public class Neighbor {

	public enum NeighborType {
		DIRECT, RANGED, RANGED_INTER_ISLAND;
		
		public boolean isRanged() {
			return (this != DIRECT);
		}
		
		public boolean isMovable() {
			return (this != RANGED);
		}
	}
	
	public final Region region;
	public final NeighborType type;
	
	public Neighbor(Region region, NeighborType type) {
		this.region = region;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type.name() + " - " + region;
	}
	
	@Override
	public boolean equals(Object object) {
		
		if (object instanceof Neighbor || object instanceof Region) {
			return region.equals(object);
		}
		
		return false;
	}
}

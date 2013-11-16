package com.slauson.tactics.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.slauson.tactics.utils.MultiIterator;

/**
 * "List" of neighbors with different types.
 * @author josh
 *
 */
public class Neighbors implements Iterable<Region> {

	public enum NeighborType {
		DIRECT, RANGED, RANGED_INTER_ISLAND;
		
		public boolean isRanged() {
			return (this != DIRECT);
		}
	}
	
	public Set<Region> directNeighbors;
	public Set<Region> rangedNeighbors;
	public Set<Region> rangedInterIslandNeighbors;
	
	private MultiIterator<Region> iterator;
	private boolean[] iteratedNeighborTypes;
	
	@SuppressWarnings("unchecked")
	public Neighbors() {
		directNeighbors = new HashSet<Region>(4);
		rangedNeighbors = new HashSet<Region>(4);
		rangedInterIslandNeighbors = new HashSet<Region>(4);
		
		iterator = new MultiIterator<Region>(directNeighbors, rangedNeighbors, rangedInterIslandNeighbors);
		iteratedNeighborTypes = new boolean[NeighborType.values().length];
		
		for (int i = 0; i < iteratedNeighborTypes.length; i++) {
			iteratedNeighborTypes[i] = true;
		}
	}
	
	/**
	 * Add region to list of neighbors.
	 * @param region
	 * @param type
	 */
	public void add(Region region, NeighborType type) {
		switch (type) {
		case DIRECT:
			directNeighbors.add(region);
			break;
		case RANGED:
			rangedNeighbors.add(region);
			break;
		case RANGED_INTER_ISLAND:
			rangedInterIslandNeighbors.add(region);
			break;
		}
	}
	
	/**
	 * Returns neighbor type if given region is a neighbor, otherwise null.
	 * @param region
	 * @return
	 */
	public NeighborType contains(Region region) {
		if (directNeighbors.contains(region)) {
			return NeighborType.DIRECT;
		} else if (rangedNeighbors.contains(region)) {
			return NeighborType.RANGED;
		} else if (rangedInterIslandNeighbors.contains(region)) {
			return NeighborType.RANGED_INTER_ISLAND;
		}
		
		return null;
	}

	@Override
	public Iterator<Region> iterator() {
		return iterator.reset(iteratedNeighborTypes);
	}
	
	/**
	 * Sets types of neighbors to iterate.
	 * @param iterated
	 */
	public void setIteratedTypes(NeighborType... iterated) {
		
		// reset iterated types
		for (int i = 0; i < iteratedNeighborTypes.length; i++) {
			iteratedNeighborTypes[i] = false;
		}
		
		// set iterated types
		for (NeighborType neighborType : iterated) {
			iteratedNeighborTypes[neighborType.ordinal()] = true;
		}
	}
	
	public static void main(String[] args) {
		Region region1 = new Region(new Vector2(1,1));
		Region region2 = new Region(new Vector2(2,2));
		Region region3 = new Region(new Vector2(3,3));
		Region region4 = new Region(new Vector2(4,4));
		Region region5 = new Region(new Vector2(5,5));
		Region region6 = new Region(new Vector2(6,6));
		Region region7 = new Region(new Vector2(7,7));
		Region region8 = new Region(new Vector2(8,8));
		Region region9 = new Region(new Vector2(9,9));
		Region region10 = new Region(new Vector2(10,10));
		Region region11 = new Region(new Vector2(11,11));
		Region region12 = new Region(new Vector2(12,12));
		
		Neighbors neighbors = new Neighbors();
		
		neighbors.add(region1, NeighborType.DIRECT);
		neighbors.add(region2, NeighborType.DIRECT);
		neighbors.add(region3, NeighborType.DIRECT);
		neighbors.add(region4, NeighborType.DIRECT);
		
		neighbors.add(region5, NeighborType.RANGED);
		neighbors.add(region6, NeighborType.RANGED);
		neighbors.add(region7, NeighborType.RANGED);
		neighbors.add(region8, NeighborType.RANGED);
		
		neighbors.add(region9, NeighborType.RANGED_INTER_ISLAND);
		neighbors.add(region10, NeighborType.RANGED_INTER_ISLAND);
		neighbors.add(region11, NeighborType.RANGED_INTER_ISLAND);
		neighbors.add(region12, NeighborType.RANGED_INTER_ISLAND);
		
		System.out.println("All neighbors");
		for (Region region : neighbors) {
			System.out.println(region);
		}
		
		System.out.println("Direct neighbors");
		neighbors.setIteratedTypes(NeighborType.DIRECT);
		for (Region region : neighbors) {
			System.out.println(region);
		}
		
		System.out.println("Ranged neighbors");
		neighbors.setIteratedTypes(NeighborType.RANGED, NeighborType.RANGED_INTER_ISLAND);
		for (Region region : neighbors) {
			System.out.println(region);
		}
	}
}

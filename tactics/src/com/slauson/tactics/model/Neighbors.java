package com.slauson.tactics.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;

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
	
	private NeighborsIterator iterator;
	
	public Neighbors() {
		directNeighbors = new HashSet<Region>(4);
		rangedNeighbors = new HashSet<Region>(4);
		rangedInterIslandNeighbors = new HashSet<Region>(4);
		
		iterator = new NeighborsIterator(directNeighbors, rangedNeighbors, rangedInterIslandNeighbors);
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
		return iterator.reset();
	}
	
	public Iterator<Region> iterator(NeighborType... iterated) {
		
		
		
		return iterator.reset();
	}
	
	/**
	 * Iterator to iterate over different types of neighbors.
	 * @author josh
	 *
	 */
	// TODO abstract this...
	public class NeighborsIterator implements Iterator<Region> {
		
		private Set<Region> sets[];
		private boolean[] iterated;
		private int index;
		
		private NeighborsIterator(Set<Region>... sets) {
			this.sets = sets;
			
			index = 0;
			iterated = new boolean[sets.length];
			
			for (int i = 0; i < iterated.length; i++) {
				iterated[i] = true;
			}
		}
		
		/**
		 * Reset the iterator instead of instantiating each time.
		 * @return
		 */
		private NeighborsIterator reset(boolean... iterated) {
			
			for (int i = 0; i < this.iterated.length; i++) {
				this.iterated[i] = iterated[i];
			}
			
			return this;
		}

		@Override
		public boolean hasNext() {
			
			while (index >= 0 && index < sets.length) {
				
				if (iterated[index] && sets[index].iterator().hasNext()) {
					return true;
				} else {
					index++;
				}
			}
			
			return false;
		}

		@Override
		public Region next() {
			
			while (index >= 0 && index < sets.length) {
				Region next = iterated[index] ? sets[index].iterator().next() : null;
				
				if (next != null) {
					return next;
				} else {
					index++;
				}
			}
			
			return null;
		}

		@Override
		public void remove() {
			// possible, but not needed
			throw new UnsupportedOperationException();
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
		
		for (Region region : neighbors) {
			System.out.println(region);
		}
	}
}

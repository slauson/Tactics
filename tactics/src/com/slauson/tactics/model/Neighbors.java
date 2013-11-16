package com.slauson.tactics.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.slauson.tactics.model.Neighbor.NeighborType;

/**
 * "List" of neighbors with different types.
 * @author josh
 *
 */
public class Neighbors implements Iterable<Neighbor> {
	
	private Set<Neighbor> neighbors;
	
	public Neighbors() {
		neighbors = new HashSet<Neighbor>(12);
	}
	
	/**
	 * Add region to list of neighbors.
	 * @param region
	 * @param type
	 */
	public void add(Region region, NeighborType type) {
		neighbors.add(new Neighbor(region, type));
	}
	
	/**
	 * Returns true if given region is a neighbor.
	 * @param region
	 * @return
	 */
	public boolean contains(Region region) {
		return neighbors.contains(region);
	}
	
	/**
	 * Returns neighbor type of given region, otherwise null.
	 * @param region
	 * @return
	 */
	public NeighborType getNeighborType(Region region) {
		for (Neighbor neighbor : neighbors) {
			if (neighbor.region == region) {
				return neighbor.type;
			}
		}
		
		return null;
	}

	@Override
	public Iterator<Neighbor> iterator() {
		return neighbors.iterator();
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
		for (Neighbor neighbor : neighbors) {
			System.out.println(neighbor);
		}
	}
}

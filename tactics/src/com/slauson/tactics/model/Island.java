package com.slauson.tactics.model;

import java.util.ArrayList;
import java.util.List;

public class Island {
	
	public int width, height;

	public List<Region> regions;
	
	public Island(int width, int height) {
		this.width = width;
		this.height = height;
		
		regions = new ArrayList<Region>(width * height);
	}
	
	@Override
	public String toString() {
		return String.format("%dx%d", width, height);
	}
	
	public void reset() {
		for (Region region : regions) {
			region.reset();
		}
	}
}

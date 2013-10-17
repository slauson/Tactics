package com.slauson.tactics.model;

import com.badlogic.gdx.math.Vector2;

public class Overworld {

	public int width, height;
	public Region[][] regions;
	
	public Overworld() {
		width = 10;
		height = 10;
		
		regions = new Region[width][height];
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				regions[i][j] = new Region(new Vector2(i, j));
			}
		}
	}
}

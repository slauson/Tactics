package com.slauson.tactics.utils;

import java.util.Random;

public class Utils {

	private static Random random;
	
	public static Random random() {
		if (random == null) {
			random = new Random();
		}
		
		return random;
	}
}

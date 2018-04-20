package com.borkam.ConferencePlanner.utilities;

public enum TrackType {
	MORNING(180),
	AFTERNOON(240);

	TrackType(int capacity) {
		this.capacity = capacity;
	}
	
	private int capacity;
	public int getCapacity() {
		return capacity;
	}
}

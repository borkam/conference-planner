package com.borkam.ConferencePlanner.services;

import java.util.List;
import java.util.Optional;

import com.borkam.ConferencePlanner.domain.KeyNote;
import com.borkam.ConferencePlanner.utilities.Track;

public interface Planner {
	boolean plan(Optional<KeyNote> keyNote);
	boolean isPlanned();
	List<Track> getTracks();
	void save(KeyNote keyNote);
}

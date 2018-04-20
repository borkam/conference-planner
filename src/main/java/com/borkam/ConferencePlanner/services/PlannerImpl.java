package com.borkam.ConferencePlanner.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.borkam.ConferencePlanner.domain.KeyNote;
import com.borkam.ConferencePlanner.repositories.KeyNoteRepository;
import com.borkam.ConferencePlanner.utilities.Track;
import com.borkam.ConferencePlanner.utilities.TrackType;

@Service
public class PlannerImpl implements Planner {
	private boolean planned;
	
	KeyNoteRepository keyNoteRepository;
	List<Track> tracks;

	public PlannerImpl(KeyNoteRepository keyNoteRepository, List<Track> tracks) {
		this.keyNoteRepository = keyNoteRepository;
		this.tracks = tracks;
	}
	
	@Override
	public boolean plan(Optional<KeyNote> keyNote) {
		List<KeyNote> keyNotes = new ArrayList<>();

		keyNoteRepository.findAll()
					     .iterator()
					     .forEachRemaining(keyNotes::add);
		keyNote.ifPresent(keyNotes::add);
		
		clearTracks();
		for(KeyNote kn: keyNotes) {
			if(!setKeyNoteIfTimeAvailable(kn)) {
				return planned = false;
			}
		}
		
		setNetworkingEventIfTimeAvailable();
		return planned = true;
	}
	@Override
	public boolean isPlanned() {
		return planned;
	}
	@Override
	public List<Track> getTracks() {
		return tracks;
	}
	@Override
	public void save(KeyNote keyNote) {
		keyNoteRepository.save(keyNote);
	}
	
	private boolean setKeyNoteIfTimeAvailable(KeyNote keyNote) {
		for(Track track : tracks) {
			if(keyNote.getDuration() <= track.getAvailableCapacity()) {
				track.addKeyNote(keyNote);
				return true;
			}
		}
		return false;
	}
	private void setNetworkingEventIfTimeAvailable() {
		for(Track track : tracks) {
			if(track.getTrackType().equals(TrackType.AFTERNOON)) {
				if(track.getAvailableCapacity() > 0) {
					KeyNote networkingEvent = new KeyNote();
					networkingEvent.setSubject("Networking Event");
					networkingEvent.setDuration((track.getAvailableCapacity() > 60) ? 60 : track.getAvailableCapacity());
					track.addNetworkinEvent(networkingEvent);
				}
			}
		}
	}
	private void clearTracks() {
		tracks.stream().forEach(Track::clear);
	}
}
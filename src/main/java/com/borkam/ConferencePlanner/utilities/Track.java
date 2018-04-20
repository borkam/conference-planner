package com.borkam.ConferencePlanner.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.borkam.ConferencePlanner.domain.KeyNote;

public class Track {
	private TrackType trackType;
	private List<String> keyNotes;
	
	private Integer availableCapacity;
	private Calendar availableTime;
	private DateFormat timeFormatter;
		
	public Track(TrackType trackType) {
		this.trackType = trackType;
		
		keyNotes = new ArrayList<>();
		availableCapacity = trackType.getCapacity();
		
		timeFormatter = new SimpleDateFormat("hh:mma");
		
		availableTime = Calendar.getInstance();
		availableTime.set(Calendar.AM_PM, trackType.equals(TrackType.MORNING) ? Calendar.AM : Calendar.PM);
		availableTime.set(Calendar.HOUR, trackType.equals(TrackType.MORNING) ? 9 : 1);
		availableTime.set(Calendar.MINUTE, 0);
	}
	
	public void addKeyNote(KeyNote keyNote) {
		keyNotes.add(timeFormatter.format(availableTime.getTime()) + " " + 
					 keyNote.getSubject() + " " + 
					 (keyNote.isLightning() ? "Lightning" : keyNote.getDuration() + "min"));
		availableTime.add(Calendar.MINUTE, keyNote.getDuration());
		availableCapacity -= keyNote.getDuration();
	}
	public void addNetworkinEvent(KeyNote keyNote) {
		if(trackType.equals(TrackType.MORNING))
			return;
		
		if(availableTime.get(Calendar.HOUR) < 4) {
			availableTime.set(Calendar.AM_PM, Calendar.PM);
			availableTime.set(Calendar.HOUR, 4);
			availableTime.set(Calendar.MINUTE, 0);
		}
		
		addKeyNote(keyNote);
	}
	public void clear() {
		keyNotes.clear();
		
		availableCapacity = trackType.getCapacity();
		
		availableTime.set(Calendar.AM_PM, trackType.equals(TrackType.MORNING) ? Calendar.AM : Calendar.PM);
		availableTime.set(Calendar.HOUR, trackType.equals(TrackType.MORNING) ? 9 : 1);
		availableTime.set(Calendar.MINUTE, 0);
	}

	
	public Integer getAvailableCapacity() {
		return availableCapacity;
	}
	public TrackType getTrackType() {
		return trackType;
	}
	public List<String> getKeyNotes() {
		return keyNotes;
	}
}
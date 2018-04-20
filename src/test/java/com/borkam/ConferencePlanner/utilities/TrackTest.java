package com.borkam.ConferencePlanner.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.borkam.ConferencePlanner.domain.KeyNote;
import com.borkam.ConferencePlanner.utilities.Track;
import com.borkam.ConferencePlanner.utilities.TrackType;

@RunWith(SpringRunner.class)
public class TrackTest {
	Track morningTrack;
	Track afternoonTrack;
	
	@Before
	public void setup() {
		morningTrack = new Track(TrackType.MORNING);
		afternoonTrack = new Track(TrackType.AFTERNOON);
	}
	
	@Test
	public void after_creation_keyNotes_size_must_zero_for_all_tracks() {
		int size = 0;
	
		assertEquals(size, morningTrack.getKeyNotes().size());
		assertEquals(size, afternoonTrack.getKeyNotes().size());
	}
	@Test
	public void after_creation_availableCapacity_must_equal_to_trackType_capacity_for_all_tracks() {
		Integer morningCapacity = TrackType.MORNING.getCapacity();
		Integer afternoonCapacity = TrackType.AFTERNOON.getCapacity();
	
		assertEquals(morningCapacity, morningTrack.getAvailableCapacity());
		assertEquals(afternoonCapacity, afternoonTrack.getAvailableCapacity());
	}
	
	@Test
	public void addKeyNote_keyNotes_size_must_increase_for_all_tracks() {
		int size = 1;
		KeyNote keyNote = new KeyNote(0L, "", 0, false);
		
		morningTrack.addKeyNote(keyNote);
		afternoonTrack.addKeyNote(keyNote);
		
		assertEquals(size, morningTrack.getKeyNotes().size());
		assertEquals(size, afternoonTrack.getKeyNotes().size());
	}
	@Test
	public void addKeyNote_non_lightning_keyNote_format_must_be_valid_for_morning_tracks() {
		String format = "09:00AM test keyNote 60min";
		KeyNote keyNote = new KeyNote(0L, "test keyNote", 60, false);
		
		morningTrack.addKeyNote(keyNote);
		
		assertEquals(format, morningTrack.getKeyNotes().get(0));
	}
	@Test
	public void addKeyNote_lightning_keyNote_format_must_be_valid_for_morning_tracks() {
		String format = "09:00AM test keyNote Lightning";
		KeyNote keyNote = new KeyNote(0L, "test keyNote", 5, true);
		
		morningTrack.addKeyNote(keyNote);
		
		assertEquals(format, morningTrack.getKeyNotes().get(0));
	}
	@Test
	public void addKeyNote_non_lightning_keyNote_format_must_be_valid_for_afternoon_tracks() {
		String format = "01:00PM test keyNote 60min";
		KeyNote keyNote = new KeyNote(0L, "test keyNote", 60, false);
		
		afternoonTrack.addKeyNote(keyNote);
		
		assertEquals(format, afternoonTrack.getKeyNotes().get(0));
	}
	@Test
	public void addKeyNote_lightning_keyNote_format_must_be_valid_for_afternoon_tracks() {
		String format = "01:00PM test keyNote Lightning";
		KeyNote keyNote = new KeyNote(0L, "test keyNote", 5, true);
		
		afternoonTrack.addKeyNote(keyNote);
		
		assertEquals(format, afternoonTrack.getKeyNotes().get(0));
	}
	@Test
	public void addKeyNote_availableCapacity_must_properly_decrease_for_morning_tracks() {
		Integer duration = 45;
		Integer availableCapacity = TrackType.MORNING.getCapacity() - duration;
		KeyNote keyNote = new KeyNote(0L, "test keyNote", duration, false);
		
		morningTrack.addKeyNote(keyNote);
		
		assertEquals(availableCapacity, morningTrack.getAvailableCapacity());
	}
	@Test
	public void addKeyNote_availableCapacity_must_properly_decrease_for_afternoon_tracks() {
		Integer duration = 45;
		Integer availableCapacity = TrackType.AFTERNOON.getCapacity() - duration;
		KeyNote keyNote = new KeyNote(0L, "test keyNote", duration, false);
		
		afternoonTrack.addKeyNote(keyNote);
		
		assertEquals(availableCapacity, afternoonTrack.getAvailableCapacity());
	}
	
	@Test
	public void addNetworkinEvent_add_keyNote_for_afternoon_tracks() {
		int size = 1;
		KeyNote keyNote = new KeyNote(0L, "Networking Event", 60, false);
		
		afternoonTrack.addNetworkinEvent(keyNote);
		
		assertEquals(size, afternoonTrack.getKeyNotes().size());
	}
	@Test
	public void addNetworkinEvent_not_add_keyNote_for_morning_tracks() {
		int size = 0;
		KeyNote keyNote = new KeyNote(0L, "Networking Event", 60, false);
		
		morningTrack.addNetworkinEvent(keyNote);
		
		assertEquals(size, morningTrack.getKeyNotes().size());
	}
	@Test
	public void addNetworkinEvent_keyNote_format_must_be_valid_for_afternoon_tracks() {
		String format = "04:00PM Networking Event 60min";
		KeyNote keyNote = new KeyNote(0L, "Networking Event", 60, false);
		
		afternoonTrack.addNetworkinEvent(keyNote);
		
		assertEquals(format, afternoonTrack.getKeyNotes().get(0));
	}
	
	@Test
	public void clear_keyNotes_size_must_zero_for_all_tracks() {
		int size = 0;
		KeyNote keyNote = new KeyNote(0L, "test keyNote", 60, false);
		
		morningTrack.addKeyNote(keyNote);
		morningTrack.clear();

		afternoonTrack.addKeyNote(keyNote);
		afternoonTrack.clear();
		
		assertEquals(size, morningTrack.getKeyNotes().size());
		assertEquals(size, afternoonTrack.getKeyNotes().size());
	}
	@Test
	public void clear_availableCapacity_must_equal_to_trackType_capacity_for_all_tracks() {
		Integer morningCapacity = TrackType.MORNING.getCapacity();
		Integer afternoonCapacity = TrackType.AFTERNOON.getCapacity();
		KeyNote keyNote = new KeyNote(0L, "test keyNote", 60, false);
		
		morningTrack.addKeyNote(keyNote);
		morningTrack.clear();
		
		afternoonTrack.addKeyNote(keyNote);
		afternoonTrack.clear();
	
		assertEquals(morningCapacity, morningTrack.getAvailableCapacity());
		assertEquals(afternoonCapacity, afternoonTrack.getAvailableCapacity());
	}
	@Test
	public void clear_keyNote_format_must_be_valid_for_morning_tracks() {
		String format = "09:00AM test keyNote 60min";
		KeyNote keyNote = new KeyNote(0L, "test keyNote", 60, false);
		
		morningTrack.addKeyNote(keyNote);
		morningTrack.clear();
		morningTrack.addKeyNote(keyNote);
		
		assertEquals(format, morningTrack.getKeyNotes().get(0));
	}
	@Test
	public void clear_keyNote_format_must_be_valid_for_afternoon_tracks() {
		String format = "01:00PM test keyNote 60min";
		KeyNote keyNote = new KeyNote(0L, "test keyNote", 60, false);
		
		afternoonTrack.addKeyNote(keyNote);
		afternoonTrack.clear();
		afternoonTrack.addKeyNote(keyNote);
		
		assertEquals(format, afternoonTrack.getKeyNotes().get(0));
	}
}

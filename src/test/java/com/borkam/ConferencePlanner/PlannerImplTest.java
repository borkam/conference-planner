package com.borkam.ConferencePlanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.borkam.ConferencePlanner.domain.KeyNote;
import com.borkam.ConferencePlanner.repositories.KeyNoteRepository;
import com.borkam.ConferencePlanner.services.PlannerImpl;
import com.borkam.ConferencePlanner.utilities.Track;
import com.borkam.ConferencePlanner.utilities.TrackType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlannerImplTest {
	PlannerImpl plannerImpl;
	
	@Autowired
	KeyNoteRepository keyNoteRepository;
	
	@Autowired
	List<Track> tracks;
	
	@Before
	public void setup() {
		plannerImpl = new PlannerImpl(keyNoteRepository, tracks);
		
		keyNoteRepository.deleteAll();
	}
	
	@Test
	public void plan_isPlanned_keyNote_must_be_added_in_empty_tracks() {
		int size = 1;
		boolean result = false;
		boolean expectedResult = true;
		KeyNote keyNote = new KeyNote(0L, "test keyNote", 60, false);
		
		result = plannerImpl.plan(Optional.of(keyNote));
		
		assertEquals(size, plannerImpl.getTracks().get(0).getKeyNotes().size());
		assertEquals(expectedResult, result);
		assertEquals(expectedResult, plannerImpl.isPlanned());
	}
	
	@Test
	public void plan_isPlanned_save_keyNotes_size_not_be_exceeded_for_morning_tracks() {
		int sizeForMorningTrack = 1;
		int sizeForAfternoonTrack = 2;
		boolean result = false;
		boolean expectedResult = true;
		boolean expectedResultForTest = true;
		KeyNote keyNoteForMorningTrack = new KeyNote(0L, "test keyNote", 180, false);
		KeyNote keyNoteForTest = new KeyNote(0L, "test keyNote", 1, false);
		
		result = plannerImpl.plan(Optional.of(keyNoteForMorningTrack));
		assertEquals(expectedResult, result);
		plannerImpl.save(keyNoteForMorningTrack);
		
		result = plannerImpl.plan(Optional.of(keyNoteForTest));
		
		assertEquals(sizeForMorningTrack, plannerImpl.getTracks().get(0).getKeyNotes().size());
		assertEquals(sizeForAfternoonTrack, plannerImpl.getTracks().get(1).getKeyNotes().size());
		assertEquals(expectedResultForTest, result);
		assertEquals(expectedResultForTest, plannerImpl.isPlanned());
	}
	@Test
	public void plan_isPlanned_save_keyNotes_size_not_be_exceeded_for_afternoon_tracks() {
		int size = 1;
		boolean result = false;
		boolean expectedResult = true;
		boolean expectedResultForTest = true;
		KeyNote keyNoteForMorningTrack = new KeyNote(0L, "test keyNote", 180, false);
		KeyNote keyNoteForAfternoonTrack = new KeyNote(0L, "test keyNote", 240, false);
		KeyNote keyNoteForTest = new KeyNote(0L, "test keyNote", 1, false);
		
		result = plannerImpl.plan(Optional.of(keyNoteForMorningTrack));
		assertEquals(expectedResult, result);
		plannerImpl.save(keyNoteForMorningTrack);
		
		result = plannerImpl.plan(Optional.of(keyNoteForAfternoonTrack));
		assertEquals(expectedResult, result);
		plannerImpl.save(keyNoteForAfternoonTrack);
		
		result = plannerImpl.plan(Optional.of(keyNoteForTest));
		
		assertEquals(size, plannerImpl.getTracks().get(0).getKeyNotes().size());
		assertEquals(size, plannerImpl.getTracks().get(1).getKeyNotes().size());
		assertEquals(size, plannerImpl.getTracks().get(2).getKeyNotes().size());
		assertEquals(expectedResultForTest, result);
		assertEquals(expectedResultForTest, plannerImpl.isPlanned());
	}
	@Test
	public void plan_isPlanned_save_keyNote_cannot_be_added_if_tracks_already_full() {
		int size = 1;
		boolean result = false;
		boolean expectedResult = true;
		boolean expectedResultForTest = false;
		KeyNote keyNoteForMorningTrack = new KeyNote(0L, "test keyNote", 180, false);
		KeyNote keyNoteForAfternoonTrack = new KeyNote(0L, "test keyNote", 240, false);
		KeyNote keyNoteForTest = new KeyNote(0L, "test keyNote", 1, false);
		
		result = plannerImpl.plan(Optional.of(keyNoteForMorningTrack));
		assertEquals(expectedResult, result);
		plannerImpl.save(keyNoteForMorningTrack);
		
		result = plannerImpl.plan(Optional.of(keyNoteForAfternoonTrack));
		assertEquals(expectedResult, result);
		plannerImpl.save(keyNoteForAfternoonTrack);
		
		result = plannerImpl.plan(Optional.of(keyNoteForMorningTrack));
		assertEquals(expectedResult, result);
		plannerImpl.save(keyNoteForMorningTrack);
		
		result = plannerImpl.plan(Optional.of(keyNoteForAfternoonTrack));
		assertEquals(expectedResult, result);
		plannerImpl.save(keyNoteForAfternoonTrack);
		
		result = plannerImpl.plan(Optional.of(keyNoteForTest));
		
		assertEquals(size, plannerImpl.getTracks().get(0).getKeyNotes().size());
		assertEquals(size, plannerImpl.getTracks().get(1).getKeyNotes().size());
		assertEquals(size, plannerImpl.getTracks().get(2).getKeyNotes().size());
		assertEquals(size, plannerImpl.getTracks().get(3).getKeyNotes().size());
		assertEquals(expectedResultForTest, result);
		assertEquals(expectedResultForTest, plannerImpl.isPlanned());
	}
	
	@Test
	public void getTracks_morning_tracks_must_have_even_indeces() {
		List<Track> tracksUnderTest = plannerImpl.getTracks();
		
		for(int i = 0; i < tracksUnderTest.size(); i++) {
			if((i % 2 == 0) && !tracksUnderTest.get(i).getTrackType().equals(TrackType.MORNING)) {
				fail("morning tracks must have even indeces");
			}
		}
	}
	@Test
	public void getTracks_afternoon_tracks_must_have_odd_indeces() {
		List<Track> tracksUnderTest = plannerImpl.getTracks();
		
		for(int i = 0; i < tracksUnderTest.size(); i++) {
			if((i % 2 != 0) && !tracksUnderTest.get(i).getTrackType().equals(TrackType.AFTERNOON)) {
				fail("afternoon tracks must have odd indeces");
			}
		}
	}
	@Test
	public void getTracks_tracks_size_must_be_four() {
		int size = 4;
		
		assertEquals(size, plannerImpl.getTracks().size());
	}
}
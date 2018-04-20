package com.borkam.ConferencePlanner.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.borkam.ConferencePlanner.controllers.RegisterController;
import com.borkam.ConferencePlanner.domain.KeyNote;
import com.borkam.ConferencePlanner.services.Planner;
import com.borkam.ConferencePlanner.utilities.Track;
import com.borkam.ConferencePlanner.utilities.TrackType;

@RunWith(SpringRunner.class)
public class RegisterControllerTest {
	@Mock
	Planner planner;
	
	@Mock
	Model model;
	
	@Mock
	BindingResult bindingResult;
	
	MockMvc mockMvc;
	
	RegisterController registerController;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		registerController = new RegisterController(planner);
		
		mockMvc = MockMvcBuilders.standaloneSetup(registerController)
								 .build();
	}
	
	@Test
	public void prepareRegister() {
		//given
		KeyNote keyNoteExpected = new KeyNote();
		ArgumentCaptor<KeyNote> argumentCaptor = ArgumentCaptor.forClass(KeyNote.class);
		
		//when
		String viewName = registerController.prepareRegister(model);

		//then
		assertEquals("register", viewName);
		verify(model, times(1)).addAttribute(eq("keyNote"), argumentCaptor.capture());
		KeyNote keyNoteActual = argumentCaptor.getValue();
		assertEquals(keyNoteExpected, keyNoteActual);
	}
	@Test
    public void prepareRegister_MVC() throws Exception {
        mockMvc.perform(
        			get("/register/new")
        		)
               .andExpect(status().isOk())
               .andExpect(view().name("register"))
               .andExpect(model().attributeExists("keyNote"));
    }

	@Test
	@SuppressWarnings("unchecked")
	public void register_validation_fail_MVC() throws Exception {
		when(planner.plan(any(Optional.class))).thenReturn(true);
		
		mockMvc.perform(
					post("/register/save").contentType(MediaType.APPLICATION_FORM_URLENCODED)
									      .param("subject", "")
									      .param("duration", "5")
									      .param("lightning", "true")
				)
	           .andExpect(status().isOk())
	           .andExpect(model().attributeExists("keyNote"))
	           .andExpect(view().name("register"));
	}
	@Test
	@SuppressWarnings("unchecked")
	public void register_planned_successful_MVC() throws Exception {
		when(planner.plan(any(Optional.class))).thenReturn(true);
		
		mockMvc.perform(
					post("/register/save").contentType(MediaType.APPLICATION_FORM_URLENCODED)
									      .param("subject", "test keynote")
									      .param("duration", "60")
									      .param("lightning", "false")
				)
	           .andExpect(status().is3xxRedirection())
	           .andExpect(model().attributeExists("keyNote"))
	           .andExpect(view().name("redirect:/agenda"));
	}
	@Test
	@SuppressWarnings("unchecked")
	public void register_planned_successful_save_called() throws Exception {
		//given
		KeyNote keyNoteExpected = new KeyNote(0L, "test keyNote", 60, false);
		ArgumentCaptor<KeyNote> argumentCaptor = ArgumentCaptor.forClass(KeyNote.class);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(planner.plan(any(Optional.class))).thenReturn(true);
		
		//when
		String viewName = registerController.register(keyNoteExpected, bindingResult, model);
		
		//then
		assertEquals("redirect:/agenda", viewName);
		verify(planner, times(1)).save(argumentCaptor.capture());
		KeyNote keyNoteActual = argumentCaptor.getValue();
		assertEquals(keyNoteExpected, keyNoteActual);
	}
	@Test
	@SuppressWarnings("unchecked")
	public void register_planned_fail_MVC() throws Exception {
		when(planner.plan(any(Optional.class))).thenReturn(false);
		
		mockMvc.perform(
					post("/register/save").contentType(MediaType.APPLICATION_FORM_URLENCODED)
									      .param("subject", "test keynote")
									      .param("duration", "60")
									      .param("lightning", "false")
				)
				.andExpect(status().isOk())
		        .andExpect(model().attributeExists("keyNote"))
		        .andExpect(view().name("register"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void agenda_planned_already() {
		//given
		List<Track> tracksExpected = new ArrayList<>();
		tracksExpected.add(new Track(TrackType.MORNING));
		tracksExpected.add(new Track(TrackType.AFTERNOON));
		tracksExpected.add(new Track(TrackType.MORNING));
		tracksExpected.add(new Track(TrackType.AFTERNOON));

		ArgumentCaptor<List<Track>> argumentCaptor = ArgumentCaptor.forClass(List.class);
		when(planner.isPlanned()).thenReturn(true);
		when(planner.getTracks()).thenReturn(tracksExpected);

		//when
		String viewName = registerController.agenda(model);
		
		//then
		assertEquals("agenda", viewName);
		verify(model, times(1)).addAttribute(eq("tracks"), argumentCaptor.capture());
		List<Track> tracksActual = argumentCaptor.getValue();
		assertEquals(tracksExpected, tracksActual);
	}
	@Test
	@SuppressWarnings("unchecked")
	public void agenda_not_planned_yet() {
		//given
		List<Track> tracksExpected = new ArrayList<>();
		tracksExpected.add(new Track(TrackType.MORNING));
		tracksExpected.add(new Track(TrackType.AFTERNOON));
		tracksExpected.add(new Track(TrackType.MORNING));
		tracksExpected.add(new Track(TrackType.AFTERNOON));

		ArgumentCaptor<List<Track>> argumentCaptor = ArgumentCaptor.forClass(List.class);
		when(planner.isPlanned()).thenReturn(false);
		when(planner.getTracks()).thenReturn(tracksExpected);

		//when
		String viewName = registerController.agenda(model);
		
		//then
		assertEquals("agenda", viewName);
		verify(model, times(1)).addAttribute(eq("tracks"), argumentCaptor.capture());
		List<Track> tracksActual = argumentCaptor.getValue();
		assertEquals(tracksExpected, tracksActual);
	}
	@Test
    public void agenda_MVC() throws Exception {
        mockMvc.perform(
        			get("", "/", "agenda")
        		)
               .andExpect(status().isOk())
               .andExpect(view().name("agenda"))
               .andExpect(model().attributeExists("tracks"));
    }
}
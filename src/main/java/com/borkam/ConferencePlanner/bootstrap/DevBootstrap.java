package com.borkam.ConferencePlanner.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.borkam.ConferencePlanner.domain.KeyNote;
import com.borkam.ConferencePlanner.repositories.KeyNoteRepository;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
	private KeyNoteRepository keyNoteRepository;
	
	public DevBootstrap(KeyNoteRepository keyNoteRepository) {
		this.keyNoteRepository = keyNoteRepository;
	}
	
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//initData();
	}
	private void initData() {
		KeyNote keyNoteInitData0 = new KeyNote();
		keyNoteInitData0.setSubject("Architecting Your Codebase");
		keyNoteInitData0.setDuration(60);
		keyNoteRepository.save(keyNoteInitData0);
		
		KeyNote keyNoteInitData1 = new KeyNote();
		keyNoteInitData1.setSubject("Overdoing it in Python");
		keyNoteInitData1.setDuration(45);
		keyNoteRepository.save(keyNoteInitData1);
		
		KeyNote keyNoteInitData2 = new KeyNote();
		keyNoteInitData2.setSubject("Flavors of Concurrency in Java");
		keyNoteInitData2.setDuration(30);
		keyNoteRepository.save(keyNoteInitData2);
		
		KeyNote keyNoteInitData3 = new KeyNote();
		keyNoteInitData3.setSubject("Cloud Native Java");
		keyNoteInitData3.setDuration(5);
		keyNoteInitData3.setLightning(true);
		keyNoteRepository.save(keyNoteInitData3);
	}
}

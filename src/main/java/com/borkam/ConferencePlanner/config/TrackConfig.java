package com.borkam.ConferencePlanner.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

import com.borkam.ConferencePlanner.utilities.Track;
import com.borkam.ConferencePlanner.utilities.TrackType;

@Configuration
public class TrackConfig {

	@Value("${planner.trackSize}")
	Integer trackSize;
	
	@SessionScope
	@Bean
	public List<Track> tracks() {
		List<Track> tracks = new ArrayList<>();
		
		for(int i = 0; i < trackSize; i++) {
			tracks.add(new Track((i % 2 == 0) ? TrackType.MORNING : TrackType.AFTERNOON));
		}

		return tracks;
	}
}

package com.borkam.ConferencePlanner.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.borkam.ConferencePlanner.domain.KeyNote;
import com.borkam.ConferencePlanner.services.Planner;

@Controller
public class RegisterController {
	Planner planner;
		
	public RegisterController(Planner planner) {
		this.planner = planner;
	}

	@GetMapping("register/new")
    public String prepareRegister(Model model){
        model.addAttribute("keyNote", new KeyNote());
        return "register";
    }
	
	@PostMapping("register/save")
	public String register(@Valid @ModelAttribute("keyNote") KeyNote keyNote, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "register";
		
        /** workaround
         *  - checklere ragmen duration null gelme ihtimali mevcut
         *  - client tarafli sorun var, server tarafinda duration icin @NotNull cozum degil.
         *  1. thymeleaf, onclick js degisikligini algilamadigi durumlar olabiliyor.
         * 	2. developer tool ile araya girip, lightning - duration korrelasyonu bozulabilir.
         *  burada lightning - duration korelasyonu dogrulaniyor.
         */
        if(keyNote.getDuration() == null) keyNote.setDuration(5);
        if(keyNote.getDuration() == 5) 	  keyNote.setLightning(true);
        if(keyNote.isLightning()) 		  keyNote.setDuration(5);
        
		if(!planner.plan(Optional.of(keyNote))) {
			model.addAttribute("availability", "no time available");
			return "register";
		}
		
		planner.save(keyNote);
		
		return "redirect:/agenda";
	}
	
	@RequestMapping({"", "/", "agenda"})
	public String agenda(Model model) {
		if(!planner.isPlanned())
			planner.plan(Optional.empty());
		
		model.addAttribute("tracks", planner.getTracks());
		
		return "agenda";
	}
}
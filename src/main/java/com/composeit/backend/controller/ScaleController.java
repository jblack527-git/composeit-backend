package com.composeit.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ScaleController {
	
	@GetMapping("/scales")
	public List<String> getScales() {
		// Replace with actual scale logic
        return List.of("C Major", "G Major", "D Minor");
	}
}

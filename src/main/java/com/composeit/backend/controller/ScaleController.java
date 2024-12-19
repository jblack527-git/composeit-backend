package com.composeit.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.composeit.backend.dto.SemitonesRequest;
import com.composeit.backend.dto.SemitonesResponse;
import com.composeit.backend.scaleservice.ScaleService;

import java.util.List;

@RestController
@RequestMapping("/api/scales")
public class ScaleController {

    private final ScaleService scaleService;

    public ScaleController(ScaleService scaleService) {
        this.scaleService = scaleService;
    }

    @GetMapping("/semitones")
    public ResponseEntity<SemitonesResponse> getSemitones(@RequestBody SemitonesRequest request) {
    	try {
            List<String> semitones = scaleService.getSemitones(request.getTonic(), request.getQuality());
            return ResponseEntity.ok(new SemitonesResponse(semitones));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new SemitonesResponse(List.of()));
        }
    }
}

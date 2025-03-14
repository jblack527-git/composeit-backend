package com.composeit.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.composeit.backend.dto.*;
import com.composeit.backend.scaleservice.ScaleService;
import com.composeit.backend.scaleservice.models.ScaleProfile;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ScaleController {

    private final ScaleService scaleService;

    public ScaleController(ScaleService scaleService) {
        this.scaleService = scaleService;
    }

    @PostMapping("/semitones")
    public ResponseEntity<SemitonesResponse> getSemitones(@Valid @RequestBody SemitonesRequest request) {
    	try {
            List<String> semitones = scaleService.getSemitones(request.getTonic(), request.getQuality());
            return ResponseEntity.ok(new SemitonesResponse(semitones));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new SemitonesResponse(List.of()));
        }
    }
    
    @PostMapping("/scales-from-semitones")
    public ResponseEntity<ScalesResponse> getScales(@Valid @RequestBody ScalesRequest request) {
    	try {
            List<String> scales = scaleService.getScalesFromSemitones(request.getSemitones());
            return ResponseEntity.ok(new ScalesResponse(scales));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ScalesResponse(List.of()));
        }
    }
    
    @PostMapping("/chords")
    public ResponseEntity<ChordsResponse> getChords(@Valid @RequestBody SemitonesRequest request) {
        try {
            List<String> chords = scaleService.getChords(request.getTonic(), request.getQuality());
            return ResponseEntity.ok(new ChordsResponse(chords));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ChordsResponse(List.of()));
        }
    }
    
    @PostMapping("/scales-from-chords")
    public ResponseEntity<ScalesResponse> getScalesFromChords(@Valid @RequestBody ChordsRequest request) {
        try {
            List<String> scales = scaleService.getScalesFromChords(request.getChords());
            return ResponseEntity.ok(new ScalesResponse(scales));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ScalesResponse(List.of()));
        }
    }
    
    @PostMapping("/profile")
    public ResponseEntity<ScaleProfileResponse> getScaleProfile(@Valid @RequestBody SemitonesRequest request) {
        try {
            ScaleProfile profile = scaleService.getScaleProfile(request.getTonic(), request.getQuality());
            return ResponseEntity.ok(new ScaleProfileResponse(profile));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ScaleProfileResponse());
        }
    }
}

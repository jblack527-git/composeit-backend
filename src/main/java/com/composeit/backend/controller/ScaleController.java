package com.composeit.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.composeit.backend.dto.ScalesRequest;
import com.composeit.backend.dto.ScalesResponse;
import com.composeit.backend.dto.SemitonesRequest;
import com.composeit.backend.dto.SemitonesResponse;
import com.composeit.backend.scaleservice.ScaleService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/scales")
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
    
    @PostMapping("/scales")
    public ResponseEntity<ScalesResponse> getScales(@Valid @RequestBody ScalesRequest request) {
    	try {
            List<String> scales = scaleService.getScales(request.getSemitones());
            return ResponseEntity.ok(new ScalesResponse(scales));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ScalesResponse(List.of()));
        }
    }
}

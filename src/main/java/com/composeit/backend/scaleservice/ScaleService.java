package com.composeit.backend.scaleservice;

import org.springframework.stereotype.Service;

import com.composeit.backend.scaleservice.models.Quality;

import java.util.List;

@Service
public class ScaleService {
	ScaleCalculator scalecalculator = new ScaleCalculator();

    public List<String> getSemitones(String semitone, Quality quality) {
        return scalecalculator.getSemitonesFromScale(semitone, quality);
    }
    
    public List<String> getScales(List<String> semitones) {
    	return scalecalculator.getScaleFromSemitones(semitones);
    }
}

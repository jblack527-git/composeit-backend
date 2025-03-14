package com.composeit.backend.scaleservice;

import org.springframework.stereotype.Service;

import com.composeit.backend.scaleservice.models.Quality;
import com.composeit.backend.scaleservice.models.ScaleProfile;

import java.util.List;

@Service
public class ScaleService {
	ScaleCalculator scalecalculator = new ScaleCalculator();

    public List<String> getSemitones(String semitone, Quality quality) {
        return scalecalculator.getSemitonesFromScale(semitone, quality);
    }
    
    public List<String> getScalesFromSemitones(List<String> semitones) {
    	return scalecalculator.getScaleFromSemitones(semitones);
    }
    
    public List<String> getChords(String tonic, Quality quality) {
        return scalecalculator.getChordsFromScale(tonic, quality);
    }
    
    public List<String> getScalesFromChords(List<String> chords) {
        return scalecalculator.getScaleFromChords(chords);
    }
    
    public ScaleProfile getScaleProfile(String tonic, Quality quality) {
        return scalecalculator.getScaleProfile(tonic, quality);
    }
}

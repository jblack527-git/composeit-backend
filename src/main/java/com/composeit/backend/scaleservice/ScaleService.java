package com.composeit.backend.scaleservice;

import org.springframework.stereotype.Service;

import com.composeit.backend.dto.QualitiesResponse;
import com.composeit.backend.dto.QualitiesResponse.QualityEntry;
import com.composeit.backend.scaleservice.models.Quality;
import com.composeit.backend.scaleservice.models.ScaleProfile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public QualitiesResponse getQualities() {
        List<QualityEntry> entries = Arrays.stream(Quality.values())
            .map(q -> new QualityEntry(q.name(), formatQualityLabel(q.name())))
            .collect(Collectors.toList());
        return new QualitiesResponse(entries);
    }

    private String formatQualityLabel(String enumName) {
        return Arrays.stream(enumName.split("_"))
            .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
            .collect(Collectors.joining(" "));
    }
}

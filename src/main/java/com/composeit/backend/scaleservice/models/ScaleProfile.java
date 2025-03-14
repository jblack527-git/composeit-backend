package com.composeit.backend.scaleservice.models;

import java.util.List;
import java.util.Map;

public class ScaleProfile {
    private final String tonic;
    private final Quality quality;
    private final List<String> semitones;
    private final Map<String, String> chords;

    public ScaleProfile(String tonic, Quality quality, List<String> semitones, Map<String, String> chords) {
        this.tonic = tonic;
        this.quality = quality;
        this.semitones = semitones;
        this.chords = chords;
    }

    public String getTonic() {
        return tonic;
    }

    public Quality getQuality() {
        return quality;
    }

    public List<String> getSemitones() {
        return semitones;
    }

    public Map<String, String> getChords() {
        return chords;
    }
}
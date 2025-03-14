package com.composeit.backend.scaleservice.models;

import java.util.List;
import java.util.Map;

public class ScaleProfile {
    private final String tonic;
    private final Quality quality;
    private final List<String> semitones;
    private final Map<String, String> chords;
    
    // Map of scale degree (1-7) to note name
    private final Map<Integer, String> scaleDegrees;
    
    // Map of interval names to notes
    private final Map<String, String> intervals;
    
    // Related scales
    private final String relativeScale;  // e.g., for C major -> A minor
    private final String parallelScale;  // e.g., for C major -> C minor
    
    // Common chord progressions in this scale using position notation
    private final List<List<String>> commonProgressions;
    
    // Mode information (e.g., "Ionian" for major, "Aeolian" for natural minor)
    private final String mode;

    public ScaleProfile(
            String tonic, 
            Quality quality, 
            List<String> semitones, 
            Map<String, String> chords,
            Map<Integer, String> scaleDegrees,
            Map<String, String> intervals,
            String relativeScale,
            String parallelScale,
            List<List<String>> commonProgressions,
            String mode) {
        this.tonic = tonic;
        this.quality = quality;
        this.semitones = semitones;
        this.chords = chords;
        this.scaleDegrees = scaleDegrees;
        this.intervals = intervals;
        this.relativeScale = relativeScale;
        this.parallelScale = parallelScale;
        this.commonProgressions = commonProgressions;
        this.mode = mode;
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
    
    public Map<Integer, String> getScaleDegrees() {
        return scaleDegrees;
    }
    
    public Map<String, String> getIntervals() {
        return intervals;
    }
    
    public String getRelativeScale() {
        return relativeScale;
    }
    
    public String getParallelScale() {
        return parallelScale;
    }
    
    public List<List<String>> getCommonProgressions() {
        return commonProgressions;
    }
    
    public String getMode() {
        return mode;
    }
}
package com.composeit.backend.dto;

import java.util.List;
import java.util.Map;

import com.composeit.backend.scaleservice.models.Quality;
import com.composeit.backend.scaleservice.models.ScaleProfile;

public class ScaleProfileResponse {
    private String tonic;
    private Quality quality;
    private List<String> semitones;
    private Map<String, String> chords;
    private Map<Integer, String> scaleDegrees;
    private Map<String, String> intervals;
    private String relativeScale;
    private String parallelScale;
    private List<List<String>> commonProgressions;
    private String mode;

    public ScaleProfileResponse() {
        // Default constructor for error responses
    }

    public ScaleProfileResponse(ScaleProfile profile) {
        this.tonic = profile.getTonic();
        this.quality = profile.getQuality();
        this.semitones = profile.getSemitones();
        this.chords = profile.getChords();
        this.scaleDegrees = profile.getScaleDegrees();
        this.intervals = profile.getIntervals();
        this.relativeScale = profile.getRelativeScale();
        this.parallelScale = profile.getParallelScale();
        this.commonProgressions = profile.getCommonProgressions();
        this.mode = profile.getMode();
    }

    public String getTonic() {
        return tonic;
    }

    public void setTonic(String tonic) {
        this.tonic = tonic;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public List<String> getSemitones() {
        return semitones;
    }

    public void setSemitones(List<String> semitones) {
        this.semitones = semitones;
    }

    public Map<String, String> getChords() {
        return chords;
    }

    public void setChords(Map<String, String> chords) {
        this.chords = chords;
    }

    public Map<Integer, String> getScaleDegrees() {
        return scaleDegrees;
    }

    public void setScaleDegrees(Map<Integer, String> scaleDegrees) {
        this.scaleDegrees = scaleDegrees;
    }

    public Map<String, String> getIntervals() {
        return intervals;
    }

    public void setIntervals(Map<String, String> intervals) {
        this.intervals = intervals;
    }

    public String getRelativeScale() {
        return relativeScale;
    }

    public void setRelativeScale(String relativeScale) {
        this.relativeScale = relativeScale;
    }

    public String getParallelScale() {
        return parallelScale;
    }

    public void setParallelScale(String parallelScale) {
        this.parallelScale = parallelScale;
    }

    public List<List<String>> getCommonProgressions() {
        return commonProgressions;
    }

    public void setCommonProgressions(List<List<String>> commonProgressions) {
        this.commonProgressions = commonProgressions;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
} 
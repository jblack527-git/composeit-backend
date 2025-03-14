package com.composeit.backend.dto;

import java.util.List;

public class ChordsResponse {
    private List<String> chords;

    public ChordsResponse(List<String> chords) {
        this.chords = chords;
    }

    public List<String> getChords() {
        return chords;
    }

    public void setChords(List<String> chords) {
        this.chords = chords;
    }
} 
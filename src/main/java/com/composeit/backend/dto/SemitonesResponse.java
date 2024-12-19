package com.composeit.backend.dto;

import java.util.List;

public class SemitonesResponse {
	private List<String> semitones;

    public SemitonesResponse(List<String> semitones) {
        this.semitones = semitones;
    }

    public List<String> getSemitones() {
        return semitones;
    }

    public void setSemitones(List<String> semitones) {
        this.semitones = semitones;
    }
}

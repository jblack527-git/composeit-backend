package com.composeit.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public class ScalesRequest {
	@NotEmpty(message = "Semitones must not be empty")
	private List<String> semitones;

    public List<String> getSemitones() {
        return semitones;
    }

    public void setSemitones(List<String> semitones) {
        this.semitones = semitones;
    }
}

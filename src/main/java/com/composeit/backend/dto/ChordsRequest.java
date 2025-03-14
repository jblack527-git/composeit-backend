package com.composeit.backend.dto;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;

public class ChordsRequest {
    @NotEmpty(message = "Chords must not be empty")
    private List<String> chords;

    public List<String> getChords() {
        return chords;
    }

    public void setChords(List<String> chords) {
        this.chords = chords;
    }
} 
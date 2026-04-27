package com.composeit.backend.dto;

import com.composeit.backend.scaleservice.models.Quality;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class SemitonesRequest {
	@NotEmpty(message = "Tonic must not be empty")
	@Pattern(
		regexp = "^(C|C#|Db|D|D#|Eb|E|F|F#|Gb|G|G#|Ab|A|A#|Bb|B)$",
		message = "Tonic must be a valid note"
	)
	private String tonic;
	
	@NotNull(message = "Quality must not be null")
	private Quality quality;
	
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
}

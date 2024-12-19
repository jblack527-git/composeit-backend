package com.composeit.backend.dto;

import com.composeit.backend.scaleservice.models.Quality;

public class SemitonesRequest {
	private String tonic;
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

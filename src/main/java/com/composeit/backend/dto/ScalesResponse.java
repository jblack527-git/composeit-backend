package com.composeit.backend.dto;

import java.util.List;

public class ScalesResponse {
	private List<String> scales;
	
	public ScalesResponse(List<String> scales) {
        this.scales = scales;
    }

    public List<String> getScales() {
        return scales;
    }

    public void setScales(List<String> scales) {
        this.scales = scales;
    }
}

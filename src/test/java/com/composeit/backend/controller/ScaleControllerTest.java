package com.composeit.backend.controller;

import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.composeit.backend.dto.SemitonesRequest;
import com.composeit.backend.common.Constants;
import com.composeit.backend.dto.ScalesRequest;
import com.composeit.backend.scaleservice.ScaleService;
import com.composeit.backend.scaleservice.models.Quality;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ScaleController.class)
public class ScaleControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockitoBean
    private ScaleService scaleService;

    @Autowired
    private ObjectMapper objectMapper;
	
	@Test
	public void shouldGetCorrectSemitones() throws Exception {
		String tonic = Constants.F;
		Quality quality = Quality.MAJOR;
		List<String> semitones = List.of(Constants.F, Constants.G, Constants.A, 
				Constants.A_SHARP, Constants.C, Constants.D, Constants.E);
		when(scaleService.getSemitones(tonic, quality)).thenReturn(semitones);
		
		SemitonesRequest request = new SemitonesRequest();
		request.setTonic(tonic);
		request.setQuality(quality);
		String jsonPayload = objectMapper.writeValueAsString(request);

		
		mockMvc.perform(post("/api/scales/semitones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.semitones", contains(Constants.F, Constants.G, Constants.A, 
                        Constants.A_SHARP, Constants.C, Constants.D, Constants.E)));
	}
	
	@Test
    void shouldReturnBadRequestForInvalidSemitoneRequest() throws Exception {
        String invalidJson = """
                {
                    "tonic": "C"
                }
                """;

        // Perform the POST request
        mockMvc.perform(post("/api/scales/semitones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest()); // Verify HTTP 400
    }
	
	@Test
	public void shouldGetCorrectScales() throws Exception {
		List<String> scales = List.of(Constants.F + " " + Quality.MAJOR.name(), 
				Constants.D + " " + Quality.MINOR.name());
		List<String> semitones = List.of(Constants.F, Constants.G, Constants.A, 
				Constants.A_SHARP, Constants.C, Constants.D, Constants.E);
		when(scaleService.getScales(semitones)).thenReturn(scales);
		
		ScalesRequest request = new ScalesRequest();
		request.setSemitones(semitones);
		String jsonPayload = objectMapper.writeValueAsString(request);

		
		mockMvc.perform(post("/api/scales/scales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scales", contains(Constants.F + " " + Quality.MAJOR.name(), 
                		Constants.D + " " + Quality.MINOR.name())));
	}
	
	@Test
    void shouldReturnBadRequestForInvalidScaleRequest() throws Exception {
        String invalidJson = """
                {
                    "semitones": []
                }
                """;

        // Perform the POST request
        mockMvc.perform(post("/api/scales/scales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest()); // Verify HTTP 400
    }
}

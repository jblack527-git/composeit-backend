package com.composeit.backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.composeit.backend.dto.SemitonesRequest;
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
		String tonic = "F";
		Quality quality = Quality.MAJOR;
		List<String> semitones = List.of("F", "G", "A", "A#", "C", "D", "E");
		when(scaleService.getSemitones(tonic, quality)).thenReturn(semitones);
		
		SemitonesRequest request = new SemitonesRequest();
		request.setTonic(tonic);
		request.setQuality(quality);
		String jsonPayload = objectMapper.writeValueAsString(request);

		
		mockMvc.perform(get("/api/scales/semitones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.semitones").value(semitones.toString()));
	}
	
	@Test
    void shouldReturnBadRequestForInvalidRequest() throws Exception {
        String invalidJson = """
                {
                    "tonic": "C"
                }
                """;

        // Perform the POST request
        mockMvc.perform(get("/api/scales/semitones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest()); // Verify HTTP 400
    }
}

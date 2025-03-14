package com.composeit.backend.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.composeit.backend.dto.*;
import com.composeit.backend.common.Constants;
import com.composeit.backend.scaleservice.ScaleService;
import com.composeit.backend.scaleservice.models.Quality;
import com.composeit.backend.scaleservice.models.ScaleProfile;
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

		mockMvc.perform(post("/api/semitones")
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

        mockMvc.perform(post("/api/semitones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
	
	@Test
	public void shouldGetCorrectScales() throws Exception {
		List<String> scales = List.of(Constants.F + " " + Quality.MAJOR.name(), 
				Constants.D + " " + Quality.MINOR.name());
		List<String> semitones = List.of(Constants.F, Constants.G, Constants.A, 
				Constants.A_SHARP, Constants.C, Constants.D, Constants.E);
		when(scaleService.getScalesFromSemitones(semitones)).thenReturn(scales);
		
		ScalesRequest request = new ScalesRequest();
		request.setSemitones(semitones);
		String jsonPayload = objectMapper.writeValueAsString(request);

		mockMvc.perform(post("/api/scales-from-semitones")
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

        mockMvc.perform(post("/api/scales-from-semitones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetCorrectChords() throws Exception {
        String tonic = Constants.C;
        Quality quality = Quality.MAJOR;
        List<String> chords = List.of("C", "Dm", "Em", "F", "G", "Am", "B°");
        when(scaleService.getChords(tonic, quality)).thenReturn(chords);
        
        SemitonesRequest request = new SemitonesRequest();
        request.setTonic(tonic);
        request.setQuality(quality);
        String jsonPayload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/chords")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chords", contains("C", "Dm", "Em", "F", "G", "Am", "B°")));
    }

    @Test
    public void shouldGetCorrectScalesFromChords() throws Exception {
        List<String> inputChords = List.of("C", "F", "G", "Am");
        List<String> scales = List.of("C MAJOR", "A MINOR");
        when(scaleService.getScalesFromChords(inputChords)).thenReturn(scales);
        
        ChordsRequest request = new ChordsRequest();
        request.setChords(inputChords);
        String jsonPayload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/scales-from-chords")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scales", contains("C MAJOR", "A MINOR")));
    }

    @Test
    void shouldReturnBadRequestForInvalidChordsRequest() throws Exception {
        String invalidJson = """
                {
                    "chords": []
                }
                """;

        mockMvc.perform(post("/api/scales-from-chords")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetCorrectScaleProfile() throws Exception {
        String tonic = Constants.C;
        Quality quality = Quality.MAJOR;
        
        ScaleProfile profile = new ScaleProfile(
            tonic,
            quality,
            List.of(Constants.C, Constants.D, Constants.E, Constants.F, Constants.G, Constants.A, Constants.B),
            Map.of(
                Constants.MAJOR_I, "C",
                Constants.MAJOR_II, "Dm",
                Constants.MAJOR_III, "Em",
                Constants.MAJOR_IV, "F",
                Constants.MAJOR_V, "G",
                Constants.MAJOR_VI, "Am",
                Constants.MAJOR_VII, "B°"
            ),
            Map.of(
                1, Constants.C,
                2, Constants.D,
                3, Constants.E,
                4, Constants.F,
                5, Constants.G,
                6, Constants.A,
                7, Constants.B
            ),
            Map.of(
                "Unison", Constants.C,
                "Major Second", Constants.D,
                "Major Third", Constants.E,
                "Perfect Fourth", Constants.F,
                "Perfect Fifth", Constants.G,
                "Major Sixth", Constants.A,
                "Major Seventh", Constants.B
            ),
            "A MINOR",
            "C MINOR",
            List.of(
                List.of(Constants.MAJOR_I, Constants.MAJOR_IV, Constants.MAJOR_V),
                List.of(Constants.MAJOR_I, Constants.MAJOR_V, Constants.MAJOR_VI, Constants.MAJOR_IV),
                List.of(Constants.MAJOR_II, Constants.MAJOR_V, Constants.MAJOR_I)
            ),
            "Ionian"
        );
        
        when(scaleService.getScaleProfile(tonic, quality)).thenReturn(profile);
        
        SemitonesRequest request = new SemitonesRequest();
        request.setTonic(tonic);
        request.setQuality(quality);
        String jsonPayload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tonic").value(tonic))
                .andExpect(jsonPath("$.quality").value(quality.name()))
                .andExpect(jsonPath("$.semitones", hasSize(7)))
                .andExpect(jsonPath("$.chords.*", hasSize(7)))
                .andExpect(jsonPath("$.scaleDegrees.*", hasSize(7)))
                .andExpect(jsonPath("$.intervals.*", hasSize(7)))
                .andExpect(jsonPath("$.relativeScale").value("A MINOR"))
                .andExpect(jsonPath("$.parallelScale").value("C MINOR"))
                .andExpect(jsonPath("$.commonProgressions", hasSize(3)))
                .andExpect(jsonPath("$.mode").value("Ionian"));
    }
}

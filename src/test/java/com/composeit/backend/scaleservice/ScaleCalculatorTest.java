package com.composeit.backend.scaleservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.composeit.backend.common.Constants;
import com.composeit.backend.scaleservice.models.Quality;
import com.composeit.backend.scaleservice.models.ScaleProfile;

class ScaleCalculatorTest {
	private ScaleCalculator calculator;

	@BeforeEach
	void setUp() {
		calculator = new ScaleCalculator();
	}

	@ParameterizedTest(name = "shouldGetCorrectSemitones - {0} {1}")
	@MethodSource("provideSemitoneArgs")
	void shouldGetCorrectSemitones(String tonic, Quality quality, List<String> expected) {
		List<String> actual = calculator.getSemitonesFromScale(tonic, quality);
		assertThat(actual).isEqualTo(expected);
	}
	
	private static Stream<Arguments> provideSemitoneArgs() {
		return Stream.of(
				Arguments.of(Constants.F, Quality.MAJOR, List.of(Constants.F, Constants.G, Constants.A, 
						Constants.A_SHARP, Constants.C, Constants.D, Constants.E)),
				Arguments.of(Constants.G_SHARP, Quality.MINOR, List.of(Constants.G_SHARP, Constants.A_SHARP, 
						Constants.B, Constants.C_SHARP, Constants.D_SHARP, Constants.E, Constants.F_SHARP)),
				Arguments.of(Constants.A_SHARP, Quality.MAJOR, List.of(Constants.A_SHARP, Constants.C, 
						Constants.D, Constants.D_SHARP, Constants.F, Constants.G, Constants.A)),
				Arguments.of(Constants.C, Quality.MAJOR, List.of(Constants.C, Constants.D, Constants.E, 
						Constants.F, Constants.G, Constants.A, Constants.B)),
				Arguments.of(Constants.A, Quality.MINOR, List.of(Constants.A, Constants.B, Constants.C, 
						Constants.D, Constants.E, Constants.F, Constants.G))
				);
	}
	
	@ParameterizedTest(name = "shouldGetCorrectScales - {0}")
	@MethodSource("provideScaleArgs")
	void shouldGetCorrectScales(List<String> semitones, List<String> expected) {
		List<String> actual = calculator.getScaleFromSemitones(semitones);
		assertThat(actual).containsAll(expected);
	}
	
	private static Stream<Arguments> provideScaleArgs() {
		return Stream.of(
				Arguments.of(List.of(Constants.F, Constants.G, Constants.A, Constants.A_SHARP, 
						Constants.C, Constants.D, Constants.E), 
						List.of(Constants.F + " " + Quality.MAJOR.name(),
								Constants.D + " " + Quality.MINOR.name())),
				Arguments.of(List.of(Constants.G_SHARP, Constants.A_SHARP, Constants.B, Constants.C_SHARP, 
						Constants.D_SHARP, Constants.E, Constants.F_SHARP), 
						List.of(Constants.G_SHARP + " " + Quality.MINOR.name(),
								Constants.B + " " + Quality.MAJOR.name())),
				Arguments.of(List.of(Constants.A_SHARP, Constants.C, Constants.D, Constants.D_SHARP, 
						Constants.F, Constants.G, Constants.A), 
						List.of(Constants.A_SHARP + " " + Quality.MAJOR.name(),
								Constants.G + " " + Quality.MINOR.name())),
				Arguments.of(List.of(Constants.C, Constants.D, Constants.E, Constants.F, 
						Constants.G, Constants.A, Constants.B), 
						List.of(Constants.C + " " + Quality.MAJOR.name())),
				Arguments.of(List.of(Constants.A, Constants.B, Constants.C, Constants.D, 
						Constants.E, Constants.F, Constants.G), 
						List.of(Constants.A + " " + Quality.MINOR.name()))
				);
	}

	@ParameterizedTest(name = "shouldGetCorrectChords - {0} {1}")
	@MethodSource("provideChordArgs")
	void shouldGetCorrectChords(String tonic, Quality quality, List<String> expected) {
		List<String> actual = calculator.getChordsFromScale(tonic, quality);
		assertThat(actual).isEqualTo(expected);
	}

	private static Stream<Arguments> provideChordArgs() {
		return Stream.of(
				Arguments.of(Constants.C, Quality.MAJOR, 
						List.of("C", "Dm", "Em", "F", "G", "Am", "B°")),
				Arguments.of(Constants.A, Quality.MINOR, 
						List.of("Am", "B°", "C", "Dm", "Em", "F", "G")),
				Arguments.of(Constants.F, Quality.MAJOR, 
						List.of("F", "Gm", "Am", "A#/Bb", "C", "Dm", "E°")),
				Arguments.of(Constants.G_SHARP, Quality.MINOR, 
						List.of("G#/Abm", "A#/Bb°", "B", "C#/Dbm", "D#/Ebm", "E", "F#/Gb"))
				);
	}

	@ParameterizedTest(name = "shouldGetCorrectScalesFromChords - {0}")
	@MethodSource("provideChordToScaleArgs")
	void shouldGetCorrectScalesFromChords(List<String> chords, List<String> expected) {
		List<String> actual = calculator.getScaleFromChords(chords);
		assertThat(actual).containsAll(expected);
	}

	private static Stream<Arguments> provideChordToScaleArgs() {
		return Stream.of(
			Arguments.of(
				List.of("C", "Dm", "Em", "F", "G", "Am", "B°"),
				List.of("C MAJOR")
			),
			Arguments.of(
				List.of("Am", "B°", "C", "Dm", "Em", "F", "G"),
				List.of("A MINOR")
			),
			Arguments.of(
				List.of("F", "Gm", "Am", "A#/Bb", "C", "Dm", "E°"),
				List.of("F MAJOR")
			),
			Arguments.of(
				List.of("G#/Abm", "A#/Bb°", "B", "C#/Dbm", "D#/Ebm", "E", "F#/Gb"),
				List.of("G#/Ab MINOR")
			),
			Arguments.of(
				List.of("C", "F", "G", "Am"),
				List.of("C MAJOR", "A MINOR")
			),
			Arguments.of(
				List.of("C", "G", "Am", "F"),
				List.of("C MAJOR", "A MINOR")
			)
		);
	}

	@Test
	void testGetScaleFromChords_InvalidProgression() {
		List<String> inputChords = Arrays.asList("C", "Dm", "Em", "F#", "G", "Am", "B°");
		List<String> result = calculator.getScaleFromChords(inputChords);
		assertTrue(result.isEmpty());
	}

	@Test
	void testGetScaleFromSemitones_InvalidScale() {
		List<String> inputSemitones = Arrays.asList("C", "D", "E", "F#", "G", "A", "B");
		List<String> result = calculator.getScaleFromSemitones(inputSemitones);
		assertTrue(result.isEmpty());
	}

	@ParameterizedTest(name = "shouldGetCorrectScaleProfile - {0} {1}")
	@MethodSource("provideScaleProfileArgs")
	void shouldGetCorrectScaleProfile(String tonic, Quality quality, List<String> expectedSemitones, 
			Map<String, String> expectedChords, Map<Integer, String> expectedDegrees,
			Map<String, String> expectedIntervals, String expectedRelative,
			String expectedParallel, List<List<String>> expectedProgressions,
			String expectedMode) {
		ScaleProfile profile = calculator.getScaleProfile(tonic, quality);
		
		assertThat(profile.getTonic()).isEqualTo(tonic);
		assertThat(profile.getQuality()).isEqualTo(quality);
		assertThat(profile.getSemitones()).isEqualTo(expectedSemitones);
		assertThat(profile.getChords()).isEqualTo(expectedChords);
		assertThat(profile.getScaleDegrees()).isEqualTo(expectedDegrees);
		assertThat(profile.getIntervals()).isEqualTo(expectedIntervals);
		assertThat(profile.getRelativeScale()).isEqualTo(expectedRelative);
		assertThat(profile.getParallelScale()).isEqualTo(expectedParallel);
		assertThat(profile.getCommonProgressions()).isEqualTo(expectedProgressions);
		assertThat(profile.getMode()).isEqualTo(expectedMode);
	}
	
	private static Stream<Arguments> provideScaleProfileArgs() {
		return Stream.of(
			Arguments.of(
				Constants.C, Quality.MAJOR,
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
			),
			Arguments.of(
				Constants.A, Quality.MINOR,
				List.of(Constants.A, Constants.B, Constants.C, Constants.D, Constants.E, Constants.F, Constants.G),
				Map.of(
					Constants.MINOR_I, "Am",
					Constants.MINOR_II, "B°",
					Constants.MINOR_III, "C",
					Constants.MINOR_IV, "Dm",
					Constants.MINOR_V, "Em",
					Constants.MINOR_VI, "F",
					Constants.MINOR_VII, "G"
				),
				Map.of(
					1, Constants.A,
					2, Constants.B,
					3, Constants.C,
					4, Constants.D,
					5, Constants.E,
					6, Constants.F,
					7, Constants.G
				),
				Map.of(
					"Unison", Constants.A,
					"Major Second", Constants.B,
					"Minor Third", Constants.C,
					"Perfect Fourth", Constants.D,
					"Perfect Fifth", Constants.E,
					"Minor Sixth", Constants.F,
					"Minor Seventh", Constants.G
				),
				"C MAJOR",
				"A MAJOR",
				List.of(
					List.of(Constants.MINOR_I, Constants.MINOR_IV, Constants.MINOR_V),
					List.of(Constants.MINOR_I, Constants.MINOR_VI, Constants.MINOR_VII, Constants.MINOR_V),
					List.of(Constants.MINOR_II, Constants.MINOR_V, Constants.MINOR_I)
				),
				"Aeolian"
			)
		);
	}
}

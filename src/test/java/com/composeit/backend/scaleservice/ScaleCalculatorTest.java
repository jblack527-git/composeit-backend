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
						Constants.B_FLAT, Constants.C, Constants.D, Constants.E)),
				Arguments.of(Constants.F_SHARP, Quality.MINOR, 
						List.of(Constants.F_SHARP, Constants.G_SHARP, Constants.A, 
								Constants.B, Constants.C_SHARP, Constants.D, Constants.E)),
				Arguments.of(Constants.B_FLAT, Quality.MAJOR, List.of(Constants.B_FLAT, Constants.C, 
						Constants.D, Constants.E_FLAT, Constants.F, Constants.G, Constants.A)),
				Arguments.of(Constants.C, Quality.MAJOR, List.of(Constants.C, Constants.D, Constants.E, 
						Constants.F, Constants.G, Constants.A, Constants.B)),
				Arguments.of(Constants.A, Quality.MINOR, List.of(Constants.A, Constants.B, Constants.C, 
						Constants.D, Constants.E, Constants.F, Constants.G)),
				Arguments.of(Constants.A, Quality.HARMONIC_MINOR, List.of(Constants.A, Constants.B, Constants.C,
						Constants.D, Constants.E, Constants.F, Constants.G_SHARP)),
				Arguments.of(Constants.C, Quality.MELODIC_MINOR, List.of(Constants.C, Constants.D, Constants.D_SHARP,
						Constants.F, Constants.G, Constants.A, Constants.B)),
				Arguments.of(Constants.D, Quality.DORIAN, List.of(Constants.D, Constants.E, Constants.F,
						Constants.G, Constants.A, Constants.B, Constants.C)),
				Arguments.of(Constants.E, Quality.PHRYGIAN, List.of(Constants.E, Constants.F, Constants.G,
						Constants.A, Constants.B, Constants.C, Constants.D)),
				Arguments.of(Constants.F, Quality.LYDIAN, List.of(Constants.F, Constants.G, Constants.A,
						Constants.B, Constants.C, Constants.D, Constants.E)),
				Arguments.of(Constants.G, Quality.MIXOLYDIAN, List.of(Constants.G, Constants.A, Constants.B,
						Constants.C, Constants.D, Constants.E, Constants.F)),
				Arguments.of(Constants.C, Quality.PENTATONIC_MAJOR, List.of(
					Constants.C, Constants.D, Constants.E, Constants.G, Constants.A)),
				Arguments.of(Constants.A, Quality.PENTATONIC_MINOR, List.of(
					Constants.A, Constants.C, Constants.D, Constants.E, Constants.G))
				);
	}
	
	@ParameterizedTest(name = "shouldGetCorrectScalesFromSemitones - {0}")
	@MethodSource("provideScaleArgs")
	void shouldGetCorrectScalesFromSemitones(List<String> semitones, List<String> expected) {
		List<String> actual = calculator.getScaleFromSemitones(semitones);
		assertThat(actual).containsAll(expected);
	}
	
	private static Stream<Arguments> provideScaleArgs() {
		return Stream.of(
				// Full scales (7 notes)
				Arguments.of(
						List.of(Constants.F, Constants.G, Constants.A, Constants.B_FLAT, Constants.C, Constants.D, Constants.E),
						List.of(
							"F MAJOR",          // Primary scale (Ionian mode)
							"G DORIAN",         // Second mode
							"A PHRYGIAN",       // Third mode
							"Bb LYDIAN",        // Fourth mode (A# excluded as it's less common in this context)
							"C MIXOLYDIAN",     // Fifth mode
							"D MINOR",          // Sixth mode (Aeolian)
							"E LOCRIAN"         // Seventh mode
						)
				),
				// Single note - C - testing all supported qualities from Quality enum
				Arguments.of(
						List.of(Constants.C),
						List.of(
							// Scales where C is the tonic
							"C MAJOR",              // C is tonic
							"C MINOR",              // C is tonic
							"C HARMONIC_MINOR",     // C is tonic
							"C MELODIC_MINOR",      // C is tonic
							"C DORIAN",             // C is tonic
							"C PHRYGIAN",           // C is tonic
							"C LYDIAN",             // C is tonic
							"C MIXOLYDIAN",         // C is tonic
							"C LOCRIAN",            // C is tonic
							"C PENTATONIC_MAJOR",   // C is tonic
							"C PENTATONIC_MINOR",   // C is tonic
							"C DIMINISHED",         // C is tonic
							
							// Major scales containing C
							"F MAJOR",              // C is the fifth
							"G MAJOR",              // C is the fourth
							"Bb MAJOR",             // C is the second
							
							// Minor scales containing C
							"A MINOR",              // C is the third
							"D MINOR",              // C is the seventh
							"F MINOR",              // C is the fifth
							"G MINOR",              // C is the fourth
							
							// Other scales containing C
							"D DORIAN",             // C is the seventh
							"E PHRYGIAN",           // C is the sixth
							"F LYDIAN",             // C is the fifth
							"G MIXOLYDIAN",         // C is the fourth
							"F MELODIC_MINOR",      // C is the fifth
							"A PENTATONIC_MINOR"    // C is the third
						)
				),
				// Three notes - C, E, G - testing all supported qualities from Quality enum
				Arguments.of(
						List.of(Constants.C, Constants.E, Constants.G),
						List.of(
							// Major family (Ionian)
							"C MAJOR",              // Contains C-E-G as 1-3-5
							"C PENTATONIC_MAJOR",   // Contains C-E-G as 1-3-5
							"C LYDIAN",             // Contains C-E-G as 1-3-5
							"C MIXOLYDIAN",         // Contains C-E-G as 1-3-5
							
							// Minor family
							"A MINOR",              // Contains C-E-G as 3-5-7
							"A PENTATONIC_MINOR",   // Contains C-E-G as 3-5-7
							
							// Modes containing C-E-G
							"D DORIAN",             // Contains C-E-G as 7-2-4
							"E PHRYGIAN",           // Contains C-E-G as 6-1-3
							"F LYDIAN",             // Contains C-E-G as 5-7-2
							"G MIXOLYDIAN",         // Contains C-E-G as 4-6-1
							
							// Other major scales containing C-E-G
							"F MAJOR",              // Contains C-E-G as 5-7-2
							"G MAJOR",              // Contains C-E-G as 4-6-1
							
							// Diminished scales containing C-E-G
							"C# DIMINISHED",        // Contains C-E-G
							"E DIMINISHED",         // Contains C-E-G
							"G DIMINISHED"          // Contains C-E-G
						)
				),
				// Test case for theoretical notes - should return empty list
				Arguments.of(
						List.of(Constants.B_SHARP, Constants.E_SHARP),
						List.of()  // Theoretical scales should be excluded
				),
				// Test case for enharmonic equivalents
				Arguments.of(
						List.of(Constants.G_FLAT),
						List.of(
							"Gb MAJOR",         // Both Gb and F# versions should be included
							"F# MAJOR"          // as they're both commonly used
						)
				)
		);
	}

	@Test
	void testGetScaleFromSemitones_TheoreticalNotes() {
		// Test with theoretical notes
		List<String> theoreticalNotes = Arrays.asList(Constants.B_SHARP, Constants.E_SHARP);
		List<String> result = calculator.getScaleFromSemitones(theoreticalNotes);
		assertTrue(result.isEmpty(), "Scales with theoretical notes should be excluded");

		// Test with theoretical equivalents
		List<String> theoreticalEquivalents = Arrays.asList(Constants.C_FLAT, Constants.F_FLAT);
		result = calculator.getScaleFromSemitones(theoreticalEquivalents);
		assertTrue(result.stream().noneMatch(scale -> 
			scale.startsWith("Cb") || scale.startsWith("Fb")), 
			"Scales with theoretical equivalents should be excluded");
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
						List.of("F", "Gm", "Am", "Bb", "C", "Dm", "E°")),
				Arguments.of(Constants.G_SHARP, Quality.MINOR, 
						List.of("G#m", "A#°", "B", "C#m", "D#m", "E", "F#")),
				Arguments.of(Constants.A, Quality.HARMONIC_MINOR,
						List.of("Am", "B°", "C", "Dm", "E", "F", "G#°")),
				Arguments.of(Constants.D, Quality.DORIAN,
						List.of("Dm", "Em", "F", "G", "Am", "B°", "C")),
				Arguments.of(Constants.E, Quality.PHRYGIAN,
						List.of("Em", "F", "G", "Am", "B°", "C", "Dm")),
				Arguments.of(Constants.F, Quality.LYDIAN,
						List.of("F", "G", "Am", "B°", "C", "Dm", "Em")),
				Arguments.of(Constants.G, Quality.MIXOLYDIAN,
						List.of("G", "Am", "B°", "C", "Dm", "Em", "F")),
				Arguments.of(Constants.C, Quality.PENTATONIC_MAJOR,
						List.of("C", "Dm", "Em", "G", "Am")),
				Arguments.of(Constants.A, Quality.PENTATONIC_MINOR,
						List.of("Am", "C", "Dm", "Em", "G"))
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
						List.of("F", "Gm", "Am", "Bb", "C", "Dm", "E°"),
						List.of("F MAJOR")
				),
				Arguments.of(
						List.of("C", "Dm", "Em", "G", "Am"),
						List.of("C PENTATONIC_MAJOR")
				),
				Arguments.of(
						List.of("Am", "C", "Dm", "Em", "G"),
						List.of("A PENTATONIC_MINOR")
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
	void testGetScaleFromSemitones_InvalidSemitones() {
		List<String> invalidNotes = Arrays.asList("H", "I", "J");
		List<String> result = calculator.getScaleFromSemitones(invalidNotes);
		assertTrue(result.isEmpty());
	}

	@ParameterizedTest(name = "shouldGetCorrectScaleProfile - {0} {1}")
	@MethodSource("provideScaleProfileArgs")
	void shouldGetCorrectScaleProfile(String tonic, Quality quality, ScaleProfile expected) {
		ScaleProfile actual = calculator.getScaleProfile(tonic, quality);
		
		assertThat(actual.getTonic()).isEqualTo(expected.getTonic());
		assertThat(actual.getQuality()).isEqualTo(expected.getQuality());
		assertThat(actual.getSemitones()).isEqualTo(expected.getSemitones());
		assertThat(actual.getChords()).isEqualTo(expected.getChords());
		assertThat(actual.getScaleDegrees()).isEqualTo(expected.getScaleDegrees());
		assertThat(actual.getIntervals()).isEqualTo(expected.getIntervals());
		assertThat(actual.getRelativeScale()).isEqualTo(expected.getRelativeScale());
		assertThat(actual.getParallelScale()).isEqualTo(expected.getParallelScale());
		assertThat(actual.getCommonProgressions()).isEqualTo(expected.getCommonProgressions());
		assertThat(actual.getMode()).isEqualTo(expected.getMode());
	}
	
	private static Stream<Arguments> provideScaleProfileArgs() {
		return Stream.of(
				// Major scale and its relationships
				Arguments.of(
						Constants.C, Quality.MAJOR,
						new ScaleProfile(
								Constants.C,
								Quality.MAJOR,
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
								"A MINOR",           // Relative minor
								"C MINOR",           // Parallel minor
								List.of(
										List.of(Constants.MAJOR_I, Constants.MAJOR_IV, Constants.MAJOR_V),               // I-IV-V (Blues)
										List.of(Constants.MAJOR_I, Constants.MAJOR_V, Constants.MAJOR_VI, Constants.MAJOR_IV),     // I-V-vi-IV (Pop)
										List.of(Constants.MAJOR_II, Constants.MAJOR_V, Constants.MAJOR_I),               // ii-V-I (Jazz)
										List.of(Constants.MAJOR_VI, Constants.MAJOR_IV, Constants.MAJOR_I, Constants.MAJOR_V),     // vi-IV-I-V (Pop)
										List.of(Constants.MAJOR_I, Constants.MAJOR_VI, Constants.MAJOR_IV, Constants.MAJOR_V),     // I-vi-IV-V (50s)
										List.of(Constants.MAJOR_I, Constants.MAJOR_IV, Constants.MAJOR_VI, Constants.MAJOR_V)      // I-IV-vi-V
								),
								"Ionian"
						)
				),
				// Natural minor scale and its relationships
				Arguments.of(
						Constants.A, Quality.MINOR,
						new ScaleProfile(
								Constants.A,
								Quality.MINOR,
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
								"C MAJOR",           // Relative major
								"A MAJOR",           // Parallel major
								List.of(
										List.of(Constants.MINOR_I, Constants.MINOR_IV, Constants.MINOR_V),               // i-iv-v
										List.of(Constants.MINOR_I, Constants.MINOR_VI, Constants.MINOR_VII, Constants.MINOR_V),    // i-VI-VII-v
										List.of(Constants.MINOR_II, Constants.MINOR_V, Constants.MINOR_I),               // ii°-v-i
										List.of(Constants.MINOR_I, Constants.MINOR_III, Constants.MINOR_VII, Constants.MINOR_VI),  // i-III-VII-VI
										List.of(Constants.MINOR_I, Constants.MINOR_VII, Constants.MINOR_VI, Constants.MINOR_V),    // i-VII-VI-v
										List.of(Constants.MINOR_I, Constants.MINOR_IV, Constants.MINOR_VII, Constants.MINOR_III)   // i-iv-VII-III
								),
								"Aeolian"
						)
				),
				// Pentatonic major
				Arguments.of(
						Constants.C, Quality.PENTATONIC_MAJOR,
						new ScaleProfile(
								Constants.C,
								Quality.PENTATONIC_MAJOR,
								List.of(Constants.C, Constants.D, Constants.E, Constants.G, Constants.A),
								Map.of(
										Constants.PENT_MAJOR_I, "C",
										Constants.PENT_MAJOR_II, "Dm",
										Constants.PENT_MAJOR_III, "Em",
										Constants.PENT_MAJOR_V, "G",
										Constants.PENT_MAJOR_VI, "Am"
								),
								Map.of(
										1, Constants.C,
										2, Constants.D,
										3, Constants.E,
										4, Constants.G,
										5, Constants.A
								),
								Map.of(
										"Unison", Constants.C,
										"Major Second", Constants.D,
										"Major Third", Constants.E,
										"Perfect Fifth", Constants.G,
										"Major Sixth", Constants.A
								),
								"A PENTATONIC_MINOR",    // Relative pentatonic minor
								"C PENTATONIC_MINOR",    // Parallel pentatonic minor
								List.of(
										List.of(Constants.PENT_MAJOR_I, Constants.PENT_MAJOR_V),
										List.of(Constants.PENT_MAJOR_I, Constants.PENT_MAJOR_VI, Constants.PENT_MAJOR_V),
										List.of(Constants.PENT_MAJOR_I, Constants.PENT_MAJOR_II, Constants.PENT_MAJOR_V),
										List.of(Constants.PENT_MAJOR_VI, Constants.PENT_MAJOR_V, Constants.PENT_MAJOR_I),
										List.of(Constants.PENT_MAJOR_I, Constants.PENT_MAJOR_III, Constants.PENT_MAJOR_VI)
								),
								"Major Pentatonic"
						)
				),
				// Pentatonic minor
				Arguments.of(
						Constants.A, Quality.PENTATONIC_MINOR,
						new ScaleProfile(
								Constants.A,
								Quality.PENTATONIC_MINOR,
								List.of(Constants.A, Constants.C, Constants.D, Constants.E, Constants.G),
								Map.of(
										Constants.PENT_MINOR_I, "Am",
										Constants.PENT_MINOR_III, "C",
										Constants.PENT_MINOR_IV, "Dm",
										Constants.PENT_MINOR_V, "Em",
										Constants.PENT_MINOR_VII, "G"
								),
								Map.of(
										1, Constants.A,
										2, Constants.C,
										3, Constants.D,
										4, Constants.E,
										5, Constants.G
								),
								Map.of(
										"Unison", Constants.A,
										"Minor Third", Constants.C,
										"Perfect Fourth", Constants.D,
										"Perfect Fifth", Constants.E,
										"Minor Seventh", Constants.G
								),
								"C PENTATONIC_MAJOR",    // Relative pentatonic major
								"A PENTATONIC_MAJOR",    // Parallel pentatonic major
								List.of(
										List.of(Constants.PENT_MINOR_I, Constants.PENT_MINOR_IV, Constants.PENT_MINOR_V),
										List.of(Constants.PENT_MINOR_I, Constants.PENT_MINOR_VII, Constants.PENT_MINOR_V),
										List.of(Constants.PENT_MINOR_I, Constants.PENT_MINOR_III, Constants.PENT_MINOR_VII),
										List.of(Constants.PENT_MINOR_IV, Constants.PENT_MINOR_I, Constants.PENT_MINOR_V),
										List.of(Constants.PENT_MINOR_I, Constants.PENT_MINOR_V, Constants.PENT_MINOR_IV)
								),
								"Minor Pentatonic"
						)
				),
				// Dorian mode
				Arguments.of(
						Constants.D, Quality.DORIAN,
						new ScaleProfile(
								Constants.D,
								Quality.DORIAN,
								List.of(Constants.D, Constants.E, Constants.F, Constants.G, Constants.A, Constants.B, Constants.C),
								Map.of(
										Constants.MINOR_I, "Dm",
										Constants.MINOR_II, "Em",
										Constants.MINOR_III, "F",
										Constants.MINOR_IV, "G",
										Constants.MINOR_V, "Am",
										Constants.MINOR_VI, "B°",
										Constants.MINOR_VII, "C"
								),
								Map.of(
										1, Constants.D,
										2, Constants.E,
										3, Constants.F,
										4, Constants.G,
										5, Constants.A,
										6, Constants.B,
										7, Constants.C
								),
								Map.of(
										"Unison", Constants.D,
										"Major Second", Constants.E,
										"Minor Third", Constants.F,
										"Perfect Fourth", Constants.G,
										"Perfect Fifth", Constants.A,
										"Major Sixth", Constants.B,
										"Minor Seventh", Constants.C
								),
								"C MAJOR",           // Relative major
								"D MAJOR",           // Parallel major
								List.of(
										List.of(Constants.MINOR_I, Constants.MINOR_IV, Constants.MINOR_V),
										List.of(Constants.MINOR_I, Constants.MINOR_VI, Constants.MINOR_VII, Constants.MINOR_V),
										List.of(Constants.MINOR_II, Constants.MINOR_V, Constants.MINOR_I)
								),
								"Dorian"
						)
				)
		);
	}
}

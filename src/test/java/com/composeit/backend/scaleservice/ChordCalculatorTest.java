package com.composeit.backend.scaleservice;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.composeit.backend.common.Constants.*;
import com.composeit.backend.scaleservice.models.Quality;

class ChordCalculatorTest {
    private ChordCalculator calculator;
    private ScalePatternCalculator patternCalculator;

    @BeforeEach
    void setUp() {
        patternCalculator = new ScalePatternCalculator();
        calculator = new ChordCalculator(patternCalculator);
    }

    @ParameterizedTest(name = "shouldGetCorrectChords - {0} {1}")
    @MethodSource("provideChordArgs")
    void shouldGetCorrectChords(String tonic, Quality quality, List<String> expected) {
        List<String> actual = calculator.getChordsFromScale(tonic, quality);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideChordArgs() {
        return Stream.of(
            Arguments.of(C, Quality.MAJOR, 
                List.of("C", "Dm", "Em", "F", "G", "Am", "B°")),  // C major uses natural notes
            Arguments.of(A, Quality.MINOR, 
                List.of("Am", "B°", "C", "Dm", "Em", "F", "G")),  // A minor uses natural notes
            Arguments.of(F, Quality.MAJOR,
                List.of("F", "Gm", "Am", "Bb", "C", "Dm", "E°")), // F major uses flat notation
            Arguments.of(G_SHARP, Quality.MINOR,
                List.of("G#m", "A#°", "B", "C#m", "D#m", "E", "F#")), // G# minor uses sharp notation
            Arguments.of(C, Quality.PENTATONIC_MAJOR,
                List.of("C", "Dm", "Em", "G", "Am")),  // C pentatonic major uses natural notes
            Arguments.of(A, Quality.PENTATONIC_MINOR,
                List.of("Am", "C", "Dm", "Em", "G"))   // A pentatonic minor uses natural notes
        );
    }

    @ParameterizedTest(name = "shouldCreateCorrectChordsMap - {1}")
    @MethodSource("provideChordsMapArgs")
    void shouldCreateCorrectChordsMap(List<String> chords, Quality quality, Map<String, String> expected) {
        Map<String, String> actual = calculator.createChordsMap(chords, quality);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideChordsMapArgs() {
        return Stream.of(
            Arguments.of(
                List.of("C", "Dm", "Em", "F", "G", "Am", "B°"),
                Quality.MAJOR,
                Map.of(
                    MAJOR_I, "C",
                    MAJOR_II, "Dm",
                    MAJOR_III, "Em",
                    MAJOR_IV, "F",
                    MAJOR_V, "G",
                    MAJOR_VI, "Am",
                    MAJOR_VII, "B°"
                )
            ),
            Arguments.of(
                List.of("F", "Gm", "Am", "Bb", "C", "Dm", "E°"),
                Quality.MAJOR,
                Map.of(
                    MAJOR_I, "F",
                    MAJOR_II, "Gm",
                    MAJOR_III, "Am",
                    MAJOR_IV, "Bb",
                    MAJOR_V, "C",
                    MAJOR_VI, "Dm",
                    MAJOR_VII, "E°"
                )
            ),
            Arguments.of(
                List.of("Am", "C", "Dm", "Em", "G"),
                Quality.PENTATONIC_MINOR,
                Map.of(
                    PENT_MINOR_I, "Am",
                    PENT_MINOR_III, "C",
                    PENT_MINOR_IV, "Dm",
                    PENT_MINOR_V, "Em",
                    PENT_MINOR_VII, "G"
                )
            )
        );
    }

    @ParameterizedTest(name = "shouldParseChordCorrectly - {0}")
    @MethodSource("provideChordParsingArgs")
    void shouldParseChordCorrectly(String chord, String expectedRoot, Quality expectedQuality) {
        Map.Entry<String, Quality> actual = calculator.parseChord(chord);
        assertThat(actual.getKey()).isEqualTo(expectedRoot);
        assertThat(actual.getValue()).isEqualTo(expectedQuality);
    }

    private static Stream<Arguments> provideChordParsingArgs() {
        return Stream.of(
            Arguments.of("C", "C", Quality.MAJOR),
            Arguments.of("Dm", "D", Quality.MINOR),
            Arguments.of("B°", "B", Quality.DIMINISHED),
            Arguments.of("F#m", "F#", Quality.MINOR),
            Arguments.of("Bb", "Bb", Quality.MAJOR),
            Arguments.of("G#m", "G#", Quality.MINOR)
        );
    }

    @Test
    void shouldHandleInvalidChord() {
        Map.Entry<String, Quality> actual = calculator.parseChord("Hmaj");
        assertThat(actual).isNull();
    }

    @Test
    void shouldHandleEmptyChordsList() {
        Map<String, String> actual = calculator.createChordsMap(List.of(), Quality.MAJOR);
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldHandleComplexChordSymbols() {
        Map.Entry<String, Quality> actual = calculator.parseChord("Cmaj7");
        assertThat(actual.getKey()).isEqualTo("C");
        assertThat(actual.getValue()).isEqualTo(Quality.MAJOR);
    }

    @Test
    void shouldHandleSlashChords() {
        Map.Entry<String, Quality> actual = calculator.parseChord("Am/C");
        assertThat(actual.getKey()).isEqualTo("A");
        assertThat(actual.getValue()).isEqualTo(Quality.MINOR);
    }

    @Test
    void shouldHandleNullInput() {
        List<String> actual = calculator.getChordsFromScale(null, Quality.MAJOR);
        assertThat(actual).isEmpty();
    }
} 
package com.composeit.backend.scaleservice;

import static com.composeit.backend.common.Constants.A;
import static com.composeit.backend.common.Constants.B;
import static com.composeit.backend.common.Constants.B_FLAT;
import static com.composeit.backend.common.Constants.C;
import static com.composeit.backend.common.Constants.C_SHARP;
import static com.composeit.backend.common.Constants.D;
import static com.composeit.backend.common.Constants.E;
import static com.composeit.backend.common.Constants.E_FLAT;
import static com.composeit.backend.common.Constants.F;
import static com.composeit.backend.common.Constants.F_SHARP;
import static com.composeit.backend.common.Constants.G;
import static com.composeit.backend.common.Constants.G_SHARP;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.composeit.backend.scaleservice.models.Quality;

class ScalePatternCalculatorTest {
    private ScalePatternCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ScalePatternCalculator();
    }

    @ParameterizedTest(name = "shouldGetCorrectSemitones - {0} {1}")
    @MethodSource("provideSemitoneArgs")
    void shouldGetCorrectSemitones(String tonic, Quality quality, List<String> expected) {
        List<String> actual = calculator.getSemitonesFromScale(tonic, quality);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideSemitoneArgs() {
        return Stream.of(
            Arguments.of(C, Quality.MAJOR, 
                List.of(C, D, E, F, G, A, B)),
            Arguments.of(A, Quality.MINOR, 
                List.of(A, B, C, D, E, F, G)),
            Arguments.of(F, Quality.MAJOR, 
                List.of(F, G, A, B_FLAT, C, D, E)),
            Arguments.of(G, Quality.MAJOR,
                List.of(G, A, B, C, D, E, F_SHARP)),
            Arguments.of(B_FLAT, Quality.MAJOR,
                List.of(B_FLAT, C, D, E_FLAT, F, G, A)),
            Arguments.of(F_SHARP, Quality.MINOR,
                List.of(F_SHARP, G_SHARP, A, B, C_SHARP, D, E)),
            Arguments.of(C, Quality.PENTATONIC_MAJOR, 
                List.of(C, D, E, G, A)),
            Arguments.of(A, Quality.PENTATONIC_MINOR, 
                List.of(A, C, D, E, G))
        );
    }

    @ParameterizedTest(name = "shouldCreateCorrectIntervals - {1}")
    @MethodSource("provideIntervalArgs")
    void shouldCreateCorrectIntervals(List<String> semitones, Quality quality, Map<String, String> expected) {
        Map<String, String> actual = calculator.createIntervals(semitones, quality);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideIntervalArgs() {
        return Stream.of(
            Arguments.of(
                List.of(C, D, E, F, G, A, B),
                Quality.MAJOR,
                Map.of(
                    "Unison", C,
                    "Major Second", D,
                    "Major Third", E,
                    "Perfect Fourth", F,
                    "Perfect Fifth", G,
                    "Major Sixth", A,
                    "Major Seventh", B
                )
            ),
            Arguments.of(
                List.of(A, C, D, E, G),
                Quality.PENTATONIC_MINOR,
                Map.of(
                    "Unison", A,
                    "Minor Third", C,
                    "Perfect Fourth", D,
                    "Perfect Fifth", E,
                    "Minor Seventh", G
                )
            )
        );
    }

    @Test
    void shouldHandleInvalidTonic() {
        List<String> actual = calculator.getSemitonesFromScale("H", Quality.MAJOR);
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldHandleEmptySemitones() {
        Map<String, String> actual = calculator.createIntervals(List.of(), Quality.MAJOR);
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldHandleNullQuality() {
        List<String> actual = calculator.getSemitonesFromScale(C, null);
        assertThat(actual).isEmpty();
    }
} 
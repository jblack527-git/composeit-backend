package com.composeit.backend.scaleservice;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;

import static com.composeit.backend.common.Constants.*;
import com.composeit.backend.scaleservice.models.Quality;

class ProgressionCalculatorTest {
    private ProgressionCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ProgressionCalculator();
    }

    @ParameterizedTest(name = "shouldCreateCorrectProgressions - {0}")
    @MethodSource("provideProgressionArgs")
    void shouldCreateCorrectProgressions(Quality quality, List<List<String>> expected) {
        List<List<String>> actual = calculator.createCommonProgressions(quality);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideProgressionArgs() {
        return Stream.of(
            Arguments.of(
                Quality.MAJOR,
                List.of(
                    List.of(MAJOR_I, MAJOR_IV, MAJOR_V),
                    List.of(MAJOR_I, MAJOR_V, MAJOR_VI, MAJOR_IV),
                    List.of(MAJOR_II, MAJOR_V, MAJOR_I),
                    List.of(MAJOR_VI, MAJOR_IV, MAJOR_I, MAJOR_V),
                    List.of(MAJOR_I, MAJOR_VI, MAJOR_IV, MAJOR_V),
                    List.of(MAJOR_I, MAJOR_IV, MAJOR_VI, MAJOR_V)
                )
            ),
            Arguments.of(
                Quality.PENTATONIC_MAJOR,
                List.of(
                    List.of(PENT_MAJOR_I, PENT_MAJOR_V),
                    List.of(PENT_MAJOR_I, PENT_MAJOR_VI, PENT_MAJOR_V),
                    List.of(PENT_MAJOR_I, PENT_MAJOR_II, PENT_MAJOR_V),
                    List.of(PENT_MAJOR_VI, PENT_MAJOR_V, PENT_MAJOR_I),
                    List.of(PENT_MAJOR_I, PENT_MAJOR_III, PENT_MAJOR_VI)
                )
            )
        );
    }

    @ParameterizedTest(name = "shouldFindCorrectRelativeScale - {0} {1}")
    @MethodSource("provideRelativeScaleArgs")
    void shouldFindCorrectRelativeScale(List<String> semitones, Quality quality, String expected) {
        String actual = calculator.findRelativeScale(semitones, quality);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideRelativeScaleArgs() {
        return Stream.of(
            Arguments.of(
                List.of(C, D, E, F, G, A, B),
                Quality.MAJOR,
                A + " MINOR"  // Relative minor of C major
            ),
            Arguments.of(
                List.of(A, B, C, D, E, F, G),
                Quality.MINOR,
                C + " MAJOR"  // Relative major of A minor
            ),
            Arguments.of(
                List.of(F, G, A, B_FLAT, C, D, E),
                Quality.MAJOR,
                D + " MINOR"  // Relative minor of F major
            ),
            Arguments.of(
                List.of(G_SHARP, A_SHARP, B, C_SHARP, D_SHARP, E, F_SHARP),
                Quality.MINOR,
                B + " MAJOR"  // Relative major of G# minor
            )
        );
    }

    @ParameterizedTest(name = "shouldFindCorrectParallelScale - {0} {1}")
    @MethodSource("provideParallelScaleArgs")
    void shouldFindCorrectParallelScale(String tonic, Quality quality, String expected) {
        String actual = calculator.findParallelScale(tonic, quality);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideParallelScaleArgs() {
        return Stream.of(
            // Natural notes - no accidentals
            Arguments.of(C, Quality.MAJOR, C + " MINOR"),
            Arguments.of(A, Quality.MINOR, A + " MAJOR"),
            
            // Sharp keys
            Arguments.of(F_SHARP, Quality.MAJOR, F_SHARP + " MINOR"),
            Arguments.of(G_SHARP, Quality.MINOR, G_SHARP + " MAJOR"),
            Arguments.of(D, Quality.MAJOR, D + " MINOR"),
            Arguments.of(E, Quality.MINOR, E + " MAJOR"),
            
            // Flat keys
            Arguments.of(B_FLAT, Quality.MAJOR, B_FLAT + " MINOR"),
            Arguments.of(E_FLAT, Quality.MINOR, E_FLAT + " MAJOR"),
            Arguments.of(A_FLAT, Quality.MAJOR, A_FLAT + " MINOR"),
            Arguments.of(D_FLAT, Quality.MINOR, D_FLAT + " MAJOR")
        );
    }

    @Test
    void shouldHandleNullQuality() {
        List<List<String>> actual = calculator.createCommonProgressions(null);
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldHandleEmptySemitonesForRelativeScale() {
        String actual = calculator.findRelativeScale(List.of(), Quality.MAJOR);
        assertThat(actual).isNull();
    }

    @Test
    void shouldHandleInvalidTonicForParallelScale() {
        String actual = calculator.findParallelScale("H", Quality.MAJOR);
        assertThat(actual).isNull();
    }

    @Test
    void shouldHandleNullTonicForParallelScale() {
        String actual = calculator.findParallelScale(null, Quality.MAJOR);
        assertThat(actual).isNull();
    }
} 
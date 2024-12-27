package com.composeit.backend.scaleservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import com.composeit.backend.common.Constants;
import com.composeit.backend.scaleservice.models.Quality;

@SpringBootTest
public class ScaleCalculatorTest {
	ScaleCalculator classUnderTest = new ScaleCalculator();
	
	@ParameterizedTest
	@MethodSource("provideSemitoneArgs")
	public void shouldGetCorrectSemitones(String tonic, Quality quality, List<String> expected) {
		List<String> actual = classUnderTest.getSemitones(tonic, quality);
		
		assertThat(actual).isEqualTo(expected);
	}
	
	private static Stream<Arguments> provideSemitoneArgs() {
		return Stream.of(
				Arguments.of("F", Quality.MAJOR, List.of("F", "G", "A", "A#", "C", "D", "E")),
				Arguments.of("G#", Quality.MINOR, List.of("G#", "A#", "B", "C#", "D#", "E", "F#")),
				Arguments.of("A#", Quality.MAJOR, List.of("A#", "C", "D", "D#", "F", "G", "A"))
				);
	}
	
	@ParameterizedTest
	@MethodSource("provideScaleArgs")
	public void shouldGetCorrectScales(List<String> semitones, List<String> expected) {
		List<String> actual = classUnderTest.getScale(semitones);
		
		assertThat(actual).containsAll(actual);
	}
	
	private static Stream<Arguments> provideScaleArgs() {
		return Stream.of(
				Arguments.of(List.of("F", "G", "A", "A#", "C", "D", "E"), 
						List.of(Constants.F + " " + Quality.MAJOR.name(),
								Constants.D + " " + Quality.MINOR.name())),
				Arguments.of(List.of("G#", "A#", "B", "C#", "D#", "E", "F#"), 
						List.of(Constants.G_SHARP, Quality.MINOR.name(),
								Constants.F, Quality.MINOR.name())),
				Arguments.of(List.of("A#", "C", "D", "D#", "F", "G", "A"), 
						List.of(Constants.A_SHARP, Quality.MAJOR.name(),
								Constants.G, Quality.MINOR.name()))
				);
	}
}

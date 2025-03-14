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
		List<String> actual = classUnderTest.getSemitonesFromScale(tonic, quality);
		
		assertThat(actual).isEqualTo(expected);
	}
	
	private static Stream<Arguments> provideSemitoneArgs() {
		return Stream.of(
				Arguments.of(Constants.F, Quality.MAJOR, List.of(Constants.F, Constants.G, Constants.A, 
						Constants.A_SHARP, Constants.C, Constants.D, Constants.E)),
				Arguments.of(Constants.G_SHARP, Quality.MINOR, List.of(Constants.G_SHARP, Constants.A_SHARP, 
						Constants.B, Constants.C_SHARP, Constants.D_SHARP, Constants.E, Constants.F_SHARP)),
				Arguments.of(Constants.A_SHARP, Quality.MAJOR, List.of(Constants.A_SHARP, Constants.C, 
						Constants.D, Constants.D_SHARP, Constants.F, Constants.G, Constants.A))
				);
	}
	
	@ParameterizedTest
	@MethodSource("provideScaleArgs")
	public void shouldGetCorrectScales(List<String> semitones, List<String> expected) {
		List<String> actual = classUnderTest.getScaleFromSemitones(semitones);
		
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
								Constants.G + " " + Quality.MINOR.name()))
				);
	}
}

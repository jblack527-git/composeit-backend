package com.composeit.backend.scaleservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.composeit.backend.common.Constants;
import com.composeit.backend.scaleservice.models.Quality;

public class ScaleCalculator {
	private static final List<String> semitones = 
		List.of(Constants.C, Constants.C_SHARP, Constants.D, 
				Constants.D_SHARP, Constants.E, Constants.F, 
				Constants.F_SHARP, Constants.G, Constants.G_SHARP, 
				Constants.A, Constants.A_SHARP, Constants.B);
	private static final int[] majorSteps = {2, 2, 1, 2, 2, 2};
	private static final int[] minorSteps = {2, 1, 2, 2, 1, 2};
	
	
	public List<String> getSemitones(String semitone, Quality quality) {
		OptionalInt indexOpt = IntStream.range(0, semitones.size())
				.filter(i -> semitone.equals(semitones.get(i))).findFirst();
		
		return semitonesFromScale(indexOpt.getAsInt(), getPattern(quality));
	}
	
	public List<String> getScale(List<String> inputSemitones) {
		return semitones.stream()
				.flatMap(semitone -> Arrays.stream(Quality.values())
						.map(quality -> Map.entry(semitone, quality)))
				.filter(entry -> {
					List<String> scaleSemitones = getSemitones(entry.getKey(), entry.getValue());
					return scaleSemitones.containsAll(inputSemitones);
				})
				.map(entry -> entry.getKey() + " " + entry.getValue().name())
				.collect(Collectors.toList());

	}
	
	
	private List<String> semitonesFromScale(int index, int[] steps) {
		AtomicInteger atomicIndex = new AtomicInteger(index);

		return IntStream.range(0, steps.length + 1)
				.mapToObj(i -> {
					if (i == 0) { return semitones.get(atomicIndex.get()); }
					int step = steps[i - 1];
					int newIndex = (atomicIndex.get() + step) % semitones.size();
					atomicIndex.set(newIndex);
					return semitones.get(newIndex);
					})
				.collect(Collectors.toList());
	}
	
	private int[] getPattern(Quality quality) {
		if (quality == Quality.MAJOR) {
			return majorSteps;
		} else {
			return minorSteps;
		}
	}

}

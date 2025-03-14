package com.composeit.backend.scaleservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.composeit.backend.common.Constants.SEMITONES;
import com.composeit.backend.scaleservice.models.Quality;

public class ScaleCalculator {
	private static final int[] majorSteps = {2, 2, 1, 2, 2, 2};
	private static final int[] minorSteps = {2, 1, 2, 2, 1, 2};
	
	
	public List<String> getSemitonesFromScale(String tonic, Quality quality) {
		OptionalInt indexOpt = IntStream.range(0, SEMITONES.size())
				.filter(i -> tonic.equals(SEMITONES.get(i))).findFirst();
		
		return semitonesFromScale(indexOpt.getAsInt(), getPattern(quality));
	}
	
	public List<String> getScaleFromSemitones(List<String> inputSemitones) {
		return SEMITONES.stream()
				.flatMap(tonic -> Arrays.stream(Quality.values())
						.map(quality -> Map.entry(tonic, quality)))
				.filter(entry -> {
					List<String> scaleSemitones = getSemitonesFromScale(entry.getKey(), entry.getValue());
					return scaleSemitones.containsAll(inputSemitones);
				})
				.map(entry -> entry.getKey() + " " + entry.getValue().name())
				.collect(Collectors.toList());

	}

	public List<String> getChordsFromScale(String tonic, Quality quality) {
		return null;
	}
	
	public List<String> getScaleFromChords(List<String> inputChords) {
		return null;

	}
	
	
	private List<String> semitonesFromScale(int index, int[] steps) {
		AtomicInteger atomicIndex = new AtomicInteger(index);

		return IntStream.range(0, steps.length + 1)
				.mapToObj(i -> {
					if (i == 0) { return SEMITONES.get(atomicIndex.get()); }
					int step = steps[i - 1];
					int newIndex = (atomicIndex.get() + step) % SEMITONES.size();
					atomicIndex.set(newIndex);
					return SEMITONES.get(newIndex);
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

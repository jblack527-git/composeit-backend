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
import static com.composeit.backend.common.Constants.MAJOR_STEPS;
import static com.composeit.backend.common.Constants.MINOR_STEPS;
import static com.composeit.backend.common.Constants.MAJOR_CHORD_PATTERN;
import static com.composeit.backend.common.Constants.MINOR_CHORD_PATTERN;
import com.composeit.backend.scaleservice.models.Quality;

public class ScaleCalculator {
	public List<String> getSemitonesFromScale(String tonic, Quality quality) {
		// find the index of the tonic in the semitones list
		OptionalInt indexOpt = IntStream.range(0, SEMITONES.size())
				.filter(i -> tonic.equals(SEMITONES.get(i))).findFirst();
		
		return semitonesFromScale(indexOpt.getAsInt(), getPattern(quality));
	}

	private int[] getPattern(Quality quality) {
		if (quality == Quality.MAJOR) {
			return MAJOR_STEPS;
		} else {
			return MINOR_STEPS;
		}
	}

	private List<String> semitonesFromScale(int index, int[] steps) {
		AtomicInteger atomicIndex = new AtomicInteger(index);

		// iterate over the scale and create a list of semitones based on the steps
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
	
	public List<String> getScaleFromSemitones(List<String> inputSemitones) {
		// iterate over the semitones and create a list of scales based on the semitones
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
		List<String> scale = getSemitonesFromScale(tonic, quality);
		Quality[] chordPattern = (quality == Quality.MAJOR) ? MAJOR_CHORD_PATTERN : MINOR_CHORD_PATTERN;
		
		// iterate over the scale and create a chord for each note based on the chord pattern
		List<String> chords = new ArrayList<>();
		for (int i = 0; i < scale.size(); i++) {
			String note = scale.get(i);
			Quality chordQuality = chordPattern[i];
			String chord = note + (chordQuality == Quality.MAJOR ? "" : 
								 chordQuality == Quality.MINOR ? "m" : "°");
			chords.add(chord);
		}
		return chords;
	}
	
	public List<String> getScaleFromChords(List<String> inputChords) {
		// Parse input chords to get root notes and qualities
		List<Map.Entry<String, Quality>> parsedChords = inputChords.stream()
			.map(this::parseChord)
			.collect(Collectors.toList());

		// Try each possible tonic and quality
		return SEMITONES.stream()
			.flatMap(tonic -> Arrays.stream(Quality.values())
				.filter(quality -> quality != Quality.DIMINISHED) // Only major and minor scales
				.map(quality -> Map.entry(tonic, quality)))
			.filter(entry -> {
				List<String> scaleChords = getChordsFromScale(entry.getKey(), entry.getValue());
				return parsedChords.stream()
					.allMatch(chord -> scaleChords.contains(chord.getKey() + 
						(chord.getValue() == Quality.MAJOR ? "" : 
						 chord.getValue() == Quality.MINOR ? "m" : "°")));
			})
			.map(entry -> entry.getKey() + " " + entry.getValue().name())
			.collect(Collectors.toList());
	}
	
	private Map.Entry<String, Quality> parseChord(String chord) {
		String root = chord.replaceAll("[m°]$", "");
		Quality quality;
		if (chord.endsWith("°")) {
			quality = Quality.DIMINISHED;
		} else if (chord.endsWith("m")) {
			quality = Quality.MINOR;
		} else {
			quality = Quality.MAJOR;
		}
		return Map.entry(root, quality);
	}
}

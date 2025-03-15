package com.composeit.backend.scaleservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.HashMap;
import java.util.stream.Stream;

import static com.composeit.backend.common.Constants.*;
import com.composeit.backend.scaleservice.models.Quality;
import com.composeit.backend.scaleservice.models.ScaleProfile;

public class ScaleCalculator {
	private final ScalePatternCalculator patternCalculator;
	private final ChordCalculator chordCalculator;
	private final ProgressionCalculator progressionCalculator;

	public ScaleCalculator() {
		this.patternCalculator = new ScalePatternCalculator();
		this.chordCalculator = new ChordCalculator(patternCalculator);
		this.progressionCalculator = new ProgressionCalculator();
	}

	public List<String> getSemitonesFromScale(String tonic, Quality quality) {
		return patternCalculator.getSemitonesFromScale(tonic, quality);
	}

	public List<String> getScaleFromSemitones(List<String> inputSemitones) {
		if (inputSemitones == null || inputSemitones.isEmpty()) {
			return List.of();
		}

		// Validate that all input notes are valid
		if (inputSemitones.stream().anyMatch(note -> note == null || !ALL_NOTES.contains(note))) {
			return List.of();
		}

		// Add enharmonic equivalents to the input notes
		List<String> notesWithEquivalents = new ArrayList<>(inputSemitones);
		for (String note : inputSemitones) {
			if (ENHARMONIC_MAP.containsKey(note)) {
				// Add the alternate version (flat/sharp) of the note
				notesWithEquivalents.add(ENHARMONIC_MAP.get(note)[1]);
			}
		}

		return SEMITONES.stream()
			.filter(tonic -> !tonic.equals(B_SHARP) && !tonic.equals(E_SHARP)) // Skip theoretical scales
			.flatMap(tonic -> Arrays.stream(Quality.values())
				.map(quality -> Map.entry(tonic, quality)))
			.filter(entry -> {
				List<String> scaleSemitones = getSemitonesFromScale(entry.getKey(), entry.getValue());
				// Check if scale contains any version of each input note
				return inputSemitones.stream().allMatch(inputNote -> 
					scaleSemitones.stream().anyMatch(scaleNote -> 
						scaleNote.equals(inputNote) || 
						(ENHARMONIC_MAP.containsKey(scaleNote) && 
						 Arrays.asList(ENHARMONIC_MAP.get(scaleNote)).contains(inputNote))
					)
				);
			})
			.flatMap(entry -> {
				String scaleName = entry.getKey() + " " + entry.getValue().name();
				// If tonic has an enharmonic equivalent, add both versions of the scale name
				if (ENHARMONIC_MAP.containsKey(entry.getKey())) {
					String alternateTonic = ENHARMONIC_MAP.get(entry.getKey())[1];
					// but skip theoretical equivalents (C_FLAT and F_FLAT)
					if (!alternateTonic.equals(C_FLAT) && !alternateTonic.equals(F_FLAT)) {
						String alternateScaleName = alternateTonic + " " + entry.getValue().name();
						return Stream.of(scaleName, alternateScaleName);
					}
				}
				return Stream.of(scaleName);
			})
			.collect(Collectors.toList());
	}

	public List<String> getChordsFromScale(String tonic, Quality quality) {
		return chordCalculator.getChordsFromScale(tonic, quality);
	}
	
	public List<String> getScaleFromChords(List<String> inputChords) {
		List<Map.Entry<String, Quality>> parsedChords = inputChords.stream()
			.map(chordCalculator::parseChord)
			.collect(Collectors.toList());

		return SEMITONES.stream()
			.flatMap(tonic -> Arrays.stream(Quality.values())
				.filter(quality -> quality != Quality.DIMINISHED)
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

	public ScaleProfile getScaleProfile(String tonic, Quality quality) {
		if (tonic == null || quality == null) {
			return null;
		}

		// Get the normalized tonic for consistent notation
		String normalizedTonic = patternCalculator.normalizeNote(tonic);
		if (normalizedTonic == null || !SEMITONES.contains(normalizedTonic)) {
			return null;
		}
		
		// Get scale components
		List<String> semitones = getSemitonesFromScale(normalizedTonic, quality);
		if (semitones.isEmpty()) {
			return null;
		}

		List<String> chords = getChordsFromScale(normalizedTonic, quality);
		Map<String, String> chordsMap = chordCalculator.createChordsMap(chords, quality);
		Map<Integer, String> scaleDegrees = createScaleDegrees(semitones);
		Map<String, String> intervals = patternCalculator.createIntervals(semitones, quality);
		
		// Get related scales
		String relativeScale = progressionCalculator.findRelativeScale(semitones, quality);
		String parallelScale = progressionCalculator.findParallelScale(normalizedTonic, quality);
		List<List<String>> progressions = progressionCalculator.createCommonProgressions(quality);
		
		return new ScaleProfile(
				normalizedTonic,
				quality,
				semitones,
				chordsMap,
				scaleDegrees,
				intervals,
				relativeScale,
				parallelScale,
				progressions,
				determineMode(quality)
		);
	}

	private Map<Integer, String> createScaleDegrees(List<String> semitones) {
		Map<Integer, String> scaleDegrees = new HashMap<>();
		for (int i = 0; i < semitones.size(); i++) {
			scaleDegrees.put(i + 1, semitones.get(i));
		}
		return scaleDegrees;
	}
	
	private String determineMode(Quality quality) {
		switch (quality) {
			case MAJOR:
				return "Ionian";
			case MINOR:
				return "Aeolian";
			case DORIAN:
				return "Dorian";
			case PHRYGIAN:
				return "Phrygian";
			case LYDIAN:
				return "Lydian";
			case MIXOLYDIAN:
				return "Mixolydian";
			case PENTATONIC_MAJOR:
				return "Major Pentatonic";
			case PENTATONIC_MINOR:
				return "Minor Pentatonic";
			case HARMONIC_MINOR:
				return "Harmonic Minor";
			case MELODIC_MINOR:
				return "Melodic Minor";
			default:
				return quality.name();
		}
	}
}

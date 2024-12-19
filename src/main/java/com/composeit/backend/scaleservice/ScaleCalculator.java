package com.composeit.backend.scaleservice;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import com.composeit.backend.scaleservice.models.Quality;

public class ScaleCalculator {
	private static final List<String> semitones = 
		List.of("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");
	private static final int[] majorSteps = {2, 2, 1, 2, 2, 2};
	private static final int[] minorSteps = {2, 1, 2, 2, 1, 2};
	
	
	public List<String> getSemitones(String semitone, Quality quality) {
		OptionalInt indexOpt = IntStream.range(0, semitones.size())
				.filter(i -> semitone.equals(semitones.get(i))).findFirst();
		
		return semitonesFromScale(indexOpt.getAsInt(), getPattern(quality));
	}
	
	
	private List<String> semitonesFromScale(int index, int[] steps) {
		int smtCounter = index;
		int stepCounter = 0;
		List<String> scales = new ArrayList<String>();
		scales.add(semitones.get(index));
		for (int i = 0; i < steps.length; i++) {			
			int step = steps[stepCounter];
			
			if (smtCounter + step > semitones.size() - 1) {
				int diff = (smtCounter + step) - (semitones.size());
				smtCounter = diff;
			} else {
				smtCounter += step;
			}
			
			scales.add(semitones.get(smtCounter));
			stepCounter++;
		}
		
		return scales;
	}
	
	private int[] getPattern(Quality quality) {
		if (quality == Quality.MAJOR) {
			return majorSteps;
		} else {
			return minorSteps;
		}
	}

}

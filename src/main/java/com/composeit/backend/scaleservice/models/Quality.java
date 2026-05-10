package com.composeit.backend.scaleservice.models;

import java.util.Arrays;
import java.util.List;

public enum Quality {
	MAJOR,              // Ionian mode
	MINOR,              // Natural minor / Aeolian mode
	HARMONIC_MINOR,     // Minor with raised 7th
	MELODIC_MINOR,      // Minor with raised 6th and 7th ascending
	DORIAN,             // 2nd mode of major scale
	PHRYGIAN,           // 3rd mode of major scale
	LYDIAN,             // 4th mode of major scale
	MIXOLYDIAN,         // 5th mode of major scale
	LOCRIAN,            // 7th mode of major scale
	PENTATONIC_MAJOR,   // Major scale without 4th and 7th
	PENTATONIC_MINOR,   // Minor scale without 2nd and 6th
	DIMINISHED;         // Whole-half diminished scale

	/** Returns the set of qualities visible in the given mode.
	 *  advanced=false → only MAJOR and MINOR; advanced=true → all 12. */
	public static List<Quality> allowed(boolean advanced) {
		return advanced ? Arrays.asList(values()) : List.of(MAJOR, MINOR);
	}
}

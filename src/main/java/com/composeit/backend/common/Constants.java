package com.composeit.backend.common;

import java.util.Arrays;
import java.util.List;
import com.composeit.backend.scaleservice.models.Quality;

public class Constants {
	public static String C = "C";
	public static String C_SHARP = "C#/Db";
	public static String D = "D";
	public static String D_SHARP = "D#/Eb";
	public static String E = "E";
	public static String F = "F";
	public static String F_SHARP = "F#/Gb";
	public static String G = "G";
	public static String G_SHARP = "G#/Ab";
	public static String A = "A";
	public static String A_SHARP = "A#/Bb";
	public static String B = "B";
	
	public static final List<String> SEMITONES = Arrays.asList(
		C, C_SHARP, D, D_SHARP, E, F,
		F_SHARP, G, G_SHARP, A, A_SHARP, B
	);

	// Steps between notes in given scale
	public static final int[] MAJOR_STEPS = {2, 2, 1, 2, 2, 2};
	public static final int[] MINOR_STEPS = {2, 1, 2, 2, 1, 2};

	// Major scale position notation
	public static final String MAJOR_I = "I";
	public static final String MAJOR_II = "ii";
	public static final String MAJOR_III = "iii";
	public static final String MAJOR_IV = "IV";
	public static final String MAJOR_V = "V";
	public static final String MAJOR_VI = "vi";
	public static final String MAJOR_VII = "vii°";

	// Minor scale position notation
	public static final String MINOR_I = "i";
	public static final String MINOR_II = "ii°";
	public static final String MINOR_III = "III";
	public static final String MINOR_IV = "iv";
	public static final String MINOR_V = "v";
	public static final String MINOR_VI = "VI";
	public static final String MINOR_VII = "VII";

	// Lists of positions for each scale
	public static final List<String> MAJOR_POSITIONS = Arrays.asList(
		MAJOR_I, MAJOR_II, MAJOR_III, MAJOR_IV, 
		MAJOR_V, MAJOR_VI, MAJOR_VII
	);

	public static final List<String> MINOR_POSITIONS = Arrays.asList(
		MINOR_I, MINOR_II, MINOR_III, MINOR_IV, 
		MINOR_V, MINOR_VI, MINOR_VII
	);

	// Chord patterns for major scale (I, ii, iii, IV, V, vi, vii°)
	public static final Quality[] MAJOR_CHORD_PATTERN = {
		Quality.MAJOR, Quality.MINOR, Quality.MINOR,
		Quality.MAJOR, Quality.MAJOR, Quality.MINOR,
		Quality.DIMINISHED
	};
	
	// Chord patterns for minor scale (i, ii°, III, iv, v, VI, VII)
	public static final Quality[] MINOR_CHORD_PATTERN = {
		Quality.MINOR, Quality.DIMINISHED, Quality.MAJOR,
		Quality.MINOR, Quality.MINOR, Quality.MAJOR,
		Quality.MAJOR
	};
}

package com.composeit.backend.common;

import java.util.Arrays;
import java.util.List;

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
}

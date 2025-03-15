package com.composeit.backend.common;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import com.composeit.backend.scaleservice.models.Quality;

public class Constants {
	/**
	 * Musical Notes and Accidentals
	 * 
	 * In Western music, notes are represented using letters A through G, with accidentals:
	 * - Sharp (#): raises a note by one semitone
	 * - Flat (b): lowers a note by one semitone
	 * 
	 * Enharmonic equivalents are notes that sound the same but are written differently:
	 * F# = Gb, C# = Db, etc.
	 * 
	 * We use sharp notation (#) as our internal standard for consistency, but display
	 * notes according to traditional musical conventions based on the key signature.
	 */
	// Basic Notes (using sharp notation as internal standard)
	public static final String C = "C";
	public static final String C_SHARP = "C#";
	public static final String D = "D";
	public static final String D_SHARP = "D#";
	public static final String E = "E";
	public static final String F = "F";
	public static final String F_SHARP = "F#";
	public static final String G = "G";
	public static final String G_SHARP = "G#";
	public static final String A = "A";
	public static final String A_SHARP = "A#";
	public static final String B = "B";

	// Flat notation constants (for display purposes)
	public static final String C_FLAT = "Cb";
	public static final String D_FLAT = "Db";
	public static final String E_FLAT = "Eb";
	public static final String F_FLAT = "Fb";
	public static final String G_FLAT = "Gb";
	public static final String A_FLAT = "Ab";
	public static final String B_FLAT = "Bb";

	/**
	 * Theoretical Notes
	 * 
	 * These notes are enharmonically equivalent to other notes but are rarely used in practice.
	 * They appear in certain theoretical contexts or in specific key signatures:
	 * 
	 * - B# = C (appears in C# major as the leading tone)
	 * - E# = F (appears in F# major as the leading tone)
	 * - Cb = B (appears in Gb major as the subdominant)
	 * - Fb = E (appears in Cb major as the subdominant)
	 */
	public static final String B_SHARP = "B#";  // Theoretical: enharmonically equivalent to C
	public static final String E_SHARP = "E#";  // Theoretical: enharmonically equivalent to F

	/**
	 * The Circle of Fifths is a fundamental concept in music theory that shows the relationship
	 * between different keys. Moving clockwise:
	 * Sharp keys (adding sharps): C → G → D → A → E → B → F#
	 * Flat keys (adding flats): C → F → Bb → Eb → Ab → Db → Gb
	 *
	 * Convention for accidentals (sharps vs flats):
	 * - Sharp keys typically use sharp accidentals (#)
	 * - Flat keys typically use flat accidentals (b)
	 * This makes the notation more readable and follows standard musical practice.
	 *
	 * Key signatures and their accidentals:
	 * Sharp keys:
	 * - G: F#
	 * - D: F#, C#
	 * - A: F#, C#, G#
	 * - E: F#, C#, G#, D#
	 * - B: F#, C#, G#, D#, A#
	 * - F#: F#, C#, G#, D#, A#, E#
	 *
	 * Flat keys:
	 * - F: Bb
	 * - Bb: Bb, Eb
	 * - Eb: Bb, Eb, Ab
	 * - Ab: Bb, Eb, Ab, Db
	 * - Db: Bb, Eb, Ab, Db, Gb
	 */
	
	// Keys that traditionally use flat notation (following the Circle of Fifths)
	public static final Set<String> FLAT_SCALES = Set.of("F", "Bb", "Eb", "Ab", "Db");

	// Keys that traditionally use sharp notation (following the Circle of Fifths)
	public static final Set<String> SHARP_SCALES = Set.of("G", "D", "A", "E", "B", "F#");

	// Semitones in chromatic order (using sharp notation)
	public static final List<String> SEMITONES = Arrays.asList(
		C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B
	);

	// All possible note representations (natural, sharp, and flat)
	public static final List<String> ALL_NOTES = Arrays.asList(
		C, C_SHARP, C_FLAT, D, D_SHARP, D_FLAT, E, E_SHARP, E_FLAT,
		F, F_SHARP, F_FLAT, G, G_SHARP, G_FLAT, A, A_SHARP, A_FLAT,
		B, B_SHARP, B_FLAT
	);

	/**
	 * Scale Degree Names (used in chord progressions)
	 * In traditional music theory, scale degrees are named using Roman numerals:
	 * Major: I, ii, iii, IV, V, vi, vii°
	 * Minor: i, ii°, III, iv, v, VI, VII
	 * 
	 * Upper case = Major chord
	 * Lower case = Minor chord
	 * ° = Diminished chord
	 */
	
	// Steps between notes in given scale
	public static final int[] MAJOR_STEPS = {2, 2, 1, 2, 2, 2};           // W W H W W W H
	public static final int[] MINOR_STEPS = {2, 1, 2, 2, 1, 2};           // W H W W H W W
	public static final int[] HARMONIC_MINOR_STEPS = {2, 1, 2, 2, 1, 3};  // W H W W H WH W
	public static final int[] MELODIC_MINOR_STEPS = {2, 1, 2, 2, 2, 2};   // W H W W W W H
	public static final int[] DORIAN_STEPS = {2, 1, 2, 2, 2, 1};         // W H W W W H W
	public static final int[] PHRYGIAN_STEPS = {1, 2, 2, 2, 1, 2};       // H W W W H W W
	public static final int[] LYDIAN_STEPS = {2, 2, 2, 1, 2, 2};         // W W W H W W H
	public static final int[] MIXOLYDIAN_STEPS = {2, 2, 1, 2, 2, 1};     // W W H W W H W
	public static final int[] LOCRIAN_STEPS = {1, 2, 2, 1, 2, 2};        // H W W H W W W
	public static final int[] PENTATONIC_MAJOR_STEPS = {2, 2, 3, 2};     // W W W+H W W
	public static final int[] PENTATONIC_MINOR_STEPS = {3, 2, 2, 3};     // W+H W W W+H W
	public static final int[] DIMINISHED_STEPS = {2, 1, 2, 1, 2, 1, 2};  // W H W H W H W H

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

	// Harmonic minor position notation
	public static final String HARMONIC_I = "i";
	public static final String HARMONIC_II = "ii°";
	public static final String HARMONIC_III = "III+";
	public static final String HARMONIC_IV = "iv";
	public static final String HARMONIC_V = "V";
	public static final String HARMONIC_VI = "VI";
	public static final String HARMONIC_VII = "vii°";

	// Pentatonic major position notation
	public static final String PENT_MAJOR_I = "I";
	public static final String PENT_MAJOR_II = "ii";
	public static final String PENT_MAJOR_III = "iii";
	public static final String PENT_MAJOR_V = "V";
	public static final String PENT_MAJOR_VI = "vi";

	// Pentatonic minor position notation
	public static final String PENT_MINOR_I = "i";
	public static final String PENT_MINOR_III = "III";
	public static final String PENT_MINOR_IV = "iv";
	public static final String PENT_MINOR_V = "v";
	public static final String PENT_MINOR_VII = "VII";

	// Lists of positions for each scale
	public static final List<String> MAJOR_POSITIONS = Arrays.asList(
		MAJOR_I, MAJOR_II, MAJOR_III, MAJOR_IV, 
		MAJOR_V, MAJOR_VI, MAJOR_VII
	);

	public static final List<String> MINOR_POSITIONS = Arrays.asList(
		MINOR_I, MINOR_II, MINOR_III, MINOR_IV, 
		MINOR_V, MINOR_VI, MINOR_VII
	);

	public static final List<String> HARMONIC_POSITIONS = Arrays.asList(
		HARMONIC_I, HARMONIC_II, HARMONIC_III, HARMONIC_IV,
		HARMONIC_V, HARMONIC_VI, HARMONIC_VII
	);

	public static final List<String> PENT_MAJOR_POSITIONS = Arrays.asList(
		PENT_MAJOR_I, PENT_MAJOR_II, PENT_MAJOR_III,
		PENT_MAJOR_V, PENT_MAJOR_VI
	);

	public static final List<String> PENT_MINOR_POSITIONS = Arrays.asList(
		PENT_MINOR_I, PENT_MINOR_III, PENT_MINOR_IV,
		PENT_MINOR_V, PENT_MINOR_VII
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

	// Chord patterns for harmonic minor (i, ii°, III+, iv, V, VI, vii°)
	public static final Quality[] HARMONIC_MINOR_CHORD_PATTERN = {
		Quality.MINOR, Quality.DIMINISHED, Quality.MAJOR,
		Quality.MINOR, Quality.MAJOR, Quality.MAJOR,
		Quality.DIMINISHED
	};

	// Chord patterns for other modes
	public static final Quality[] DORIAN_CHORD_PATTERN = {
		Quality.MINOR, Quality.MINOR, Quality.MAJOR,
		Quality.MAJOR, Quality.MINOR, Quality.DIMINISHED,
		Quality.MAJOR
	};

	public static final Quality[] PHRYGIAN_CHORD_PATTERN = {
		Quality.MINOR, Quality.MAJOR, Quality.MAJOR,
		Quality.MINOR, Quality.DIMINISHED, Quality.MAJOR,
		Quality.MINOR
	};

	public static final Quality[] LYDIAN_CHORD_PATTERN = {
		Quality.MAJOR, Quality.MAJOR, Quality.MINOR,
		Quality.DIMINISHED, Quality.MAJOR, Quality.MINOR,
		Quality.MINOR
	};

	public static final Quality[] MIXOLYDIAN_CHORD_PATTERN = {
		Quality.MAJOR, Quality.MINOR, Quality.DIMINISHED,
		Quality.MAJOR, Quality.MINOR, Quality.MINOR,
		Quality.MAJOR
	};

	public static final Quality[] LOCRIAN_CHORD_PATTERN = {
		Quality.DIMINISHED, Quality.MAJOR, Quality.MINOR,
		Quality.MINOR, Quality.MAJOR, Quality.MAJOR,
		Quality.MINOR
	};

	// Chord patterns for pentatonic scales
	public static final Quality[] PENTATONIC_MAJOR_CHORD_PATTERN = {
		Quality.MAJOR, Quality.MINOR, Quality.MINOR,
		Quality.MAJOR, Quality.MINOR
	};
	
	public static final Quality[] PENTATONIC_MINOR_CHORD_PATTERN = {
		Quality.MINOR, Quality.MAJOR, Quality.MINOR,
		Quality.MINOR, Quality.MAJOR
	};

	// Map of enharmonic equivalents with preferred notation
	public static final Map<String, String[]> ENHARMONIC_MAP;
	static {
		Map<String, String[]> map = new HashMap<>();
		map.put(F_SHARP, new String[]{F_SHARP, G_FLAT});
		map.put(G_FLAT, new String[]{F_SHARP, G_FLAT});
		map.put(G_SHARP, new String[]{G_SHARP, A_FLAT});
		map.put(A_FLAT, new String[]{G_SHARP, A_FLAT});
		map.put(A_SHARP, new String[]{A_SHARP, B_FLAT});
		map.put(B_FLAT, new String[]{A_SHARP, B_FLAT});
		map.put(C_SHARP, new String[]{C_SHARP, D_FLAT});
		map.put(D_FLAT, new String[]{C_SHARP, D_FLAT});
		map.put(D_SHARP, new String[]{D_SHARP, E_FLAT});
		map.put(E_FLAT, new String[]{D_SHARP, E_FLAT});
		// Theoretical enharmonic equivalents (not commonly used in practice)
		map.put(B_SHARP, new String[]{C, B_SHARP});  // B# is theoretically equivalent to C
		map.put(C_FLAT, new String[]{B, C_FLAT});   // Cb is theoretically equivalent to B
		map.put(E_SHARP, new String[]{F, E_SHARP});  // E# is theoretically equivalent to F
		map.put(F_FLAT, new String[]{E, F_FLAT});   // Fb is theoretically equivalent to E
		ENHARMONIC_MAP = Collections.unmodifiableMap(map);
	}
}

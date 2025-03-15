package com.composeit.backend.scaleservice;

import static com.composeit.backend.common.Constants.*;
import com.composeit.backend.scaleservice.models.Quality;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.HashMap;
import java.util.Collections;
import java.util.Set;
import java.util.ArrayList;

public class ScalePatternCalculator {
    /**
     * Maps between enharmonic equivalents. The first note in each array is the preferred sharp notation,
     * the second is the preferred flat notation. The choice of which to display depends on the scale context.
     */
    private static final Map<String, String[]> ENHARMONIC_MAP;
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

    /**
     * Normalizes a note to its sharp representation for internal processing.
     */
    public String normalizeNote(String note) {
        if (note == null) {
            return null;
        }

        // Check if the note has an enharmonic equivalent
        if (ENHARMONIC_MAP.containsKey(note)) {
            return ENHARMONIC_MAP.get(note)[0];  // Use sharp notation for internal processing
        }

        // For natural notes or notes without enharmonic equivalents, return as is
        return note;
    }

    /**
     * Formats a note for display based on the scale context.
     * In flat scales (F, Bb, Eb, Ab, Db), use flat notation.
     * In sharp scales (G, D, A, E, B, F#), use sharp notation.
     * 
     * This follows standard musical notation conventions based on the Circle of Fifths:
     * - Keys with flats: F (1♭) → B♭ (2♭) → E♭ (3♭) → A♭ (4♭) → D♭ (5♭)
     * - Keys with sharps: G (1♯) → D (2♯) → A (3♯) → E (4♯) → B (5♯) → F♯ (6♯)
     * 
     * Example:
     * - In F major (1 flat), we write "Bb" instead of "A#"
     * - In G major (1 sharp), we write "F#" instead of "Gb"
     */
    private String formatNoteForDisplay(String note, String tonic) {
        // If the note doesn't need formatting, return as is
        if (!ENHARMONIC_MAP.containsKey(note)) {
            return note;
        }

        // Use flat notation for flat scales
        if (FLAT_SCALES.contains(tonic)) {
            return ENHARMONIC_MAP.get(note)[1];  // Use flat notation
        }
        
        // Use sharp notation for sharp scales
        if (SHARP_SCALES.contains(tonic) || tonic.contains("#")) {
            return ENHARMONIC_MAP.get(note)[0];  // Use sharp notation
        }
        
        // For natural note scales, prefer flat notation for Bb
        if (note.equals("A#") || note.equals("Bb")) {
            return "Bb";
        }
        
        return ENHARMONIC_MAP.get(note)[0];  // Default to sharp notation
    }

    public List<String> getSemitonesFromScale(String tonic, Quality quality) {
        if (tonic == null || quality == null) {
            return Collections.emptyList();
        }

        // Handle enharmonic equivalents
        final String normalizedTonic = normalizeNote(tonic);
        
        if (!SEMITONES.contains(normalizedTonic)) {
            return Collections.emptyList();
        }

        OptionalInt indexOpt = IntStream.range(0, SEMITONES.size())
                .filter(i -> normalizedTonic.equals(SEMITONES.get(i))).findFirst();
        
        if (!indexOpt.isPresent()) {
            return Collections.emptyList();
        }

        List<String> semitones = semitonesFromScale(indexOpt.getAsInt(), getPattern(quality));
        
        // Format notes appropriately for display based on the scale context
        return semitones.stream()
                .map(note -> formatNoteForDisplay(note, tonic))
                .collect(Collectors.toList());
    }

    private int[] getPattern(Quality quality) {
        return switch (quality) {
            case MAJOR -> MAJOR_STEPS;
            case MINOR -> MINOR_STEPS;
            case HARMONIC_MINOR -> HARMONIC_MINOR_STEPS;
            case MELODIC_MINOR -> MELODIC_MINOR_STEPS;
            case DORIAN -> DORIAN_STEPS;
            case PHRYGIAN -> PHRYGIAN_STEPS;
            case LYDIAN -> LYDIAN_STEPS;
            case MIXOLYDIAN -> MIXOLYDIAN_STEPS;
            case LOCRIAN -> LOCRIAN_STEPS;
            case PENTATONIC_MAJOR -> PENTATONIC_MAJOR_STEPS;
            case PENTATONIC_MINOR -> PENTATONIC_MINOR_STEPS;
            case DIMINISHED -> DIMINISHED_STEPS;
        };
    }

    private List<String> semitonesFromScale(int index, int[] steps) {
        List<String> result = new ArrayList<>();
        result.add(SEMITONES.get(index));  // Add tonic
        
        int currentIndex = index;
        for (int step : steps) {
            currentIndex = (currentIndex + step) % SEMITONES.size();
            result.add(SEMITONES.get(currentIndex));
        }
        
        return result;
    }

    public Map<String, String> createIntervals(List<String> semitones, Quality quality) {
        if (semitones == null || semitones.isEmpty() || quality == null) {
            return Collections.emptyMap();
        }

        Map<String, String> intervals = new HashMap<>();
        String[] intervalNames = getIntervalNames(quality);
        
        for (int i = 0; i < semitones.size(); i++) {
            intervals.put(intervalNames[i], semitones.get(i));
        }
        return intervals;
    }

    private String[] getIntervalNames(Quality quality) {
        return switch (quality) {
            case MAJOR -> new String[]{
                "Unison", "Major Second", "Major Third", 
                "Perfect Fourth", "Perfect Fifth", "Major Sixth", "Major Seventh"
            };
            case MINOR -> new String[]{
                "Unison", "Major Second", "Minor Third", 
                "Perfect Fourth", "Perfect Fifth", "Minor Sixth", "Minor Seventh"
            };
            case HARMONIC_MINOR -> new String[]{
                "Unison", "Major Second", "Minor Third", 
                "Perfect Fourth", "Perfect Fifth", "Minor Sixth", "Major Seventh"
            };
            case MELODIC_MINOR -> new String[]{
                "Unison", "Major Second", "Minor Third", 
                "Perfect Fourth", "Perfect Fifth", "Major Sixth", "Major Seventh"
            };
            case DORIAN -> new String[]{
                "Unison", "Major Second", "Minor Third", 
                "Perfect Fourth", "Perfect Fifth", "Major Sixth", "Minor Seventh"
            };
            case PHRYGIAN -> new String[]{
                "Unison", "Minor Second", "Minor Third", 
                "Perfect Fourth", "Perfect Fifth", "Minor Sixth", "Minor Seventh"
            };
            case LYDIAN -> new String[]{
                "Unison", "Major Second", "Major Third", 
                "Augmented Fourth", "Perfect Fifth", "Major Sixth", "Major Seventh"
            };
            case MIXOLYDIAN -> new String[]{
                "Unison", "Major Second", "Major Third", 
                "Perfect Fourth", "Perfect Fifth", "Major Sixth", "Minor Seventh"
            };
            case LOCRIAN -> new String[]{
                "Unison", "Minor Second", "Minor Third", 
                "Perfect Fourth", "Diminished Fifth", "Minor Sixth", "Minor Seventh"
            };
            case PENTATONIC_MAJOR -> new String[]{
                "Unison", "Major Second", "Major Third", 
                "Perfect Fifth", "Major Sixth"
            };
            case PENTATONIC_MINOR -> new String[]{
                "Unison", "Minor Third", "Perfect Fourth",
                "Perfect Fifth", "Minor Seventh"
            };
            case DIMINISHED -> new String[]{
                "Unison", "Major Second", "Minor Third", 
                "Perfect Fourth", "Diminished Fifth", "Minor Sixth", "Diminished Seventh", "Major Seventh"
            };
        };
    }
} 
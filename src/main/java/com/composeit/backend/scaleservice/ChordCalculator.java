package com.composeit.backend.scaleservice;

import static com.composeit.backend.common.Constants.*;
import com.composeit.backend.scaleservice.models.Quality;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.AbstractMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ChordCalculator {
    private final ScalePatternCalculator patternCalculator;

    /**
     * Regular expression pattern for parsing chord symbols. The pattern matches:
     * 
     * ^                     - Start of string
     * (                     - Start capturing group 1 (root note)
     *   [A-G]              - Base note (A through G)
     *   [#b]?              - Optional accidental (sharp or flat)
     * )                     - End capturing group 1
     * (                     - Start capturing group 2 (quality symbol)
     *   [mM°]?             - Optional quality:
     *                         m = minor
     *                         M = major (explicit)
     *                         ° = diminished
     * )                     - End capturing group 2
     * (?:maj|min|dim)?     - Optional long-form quality
     * \d*                   - Optional extension number (e.g., 7 for maj7)
     * (?:/[A-G][#b]?)?     - Optional slash chord (e.g., /C)
     * $                     - End of string
     * 
     * Examples of valid chords:
     * - "C"      → C major
     * - "Am"     → A minor
     * - "F#m"    → F# minor
     * - "Gb"     → Gb major
     * - "B°"     → B diminished
     * - "Am/C"   → A minor with C bass
     * - "Cmaj7"  → C major seventh
     * - "Dm7"    → D minor seventh
     */
    private static final Pattern CHORD_PATTERN = Pattern.compile("^([A-G][#b]?)([mM°])?(?:maj|min|dim)?\\d*(?:/[A-G][#b]?)?$");

    public ChordCalculator(ScalePatternCalculator patternCalculator) {
        this.patternCalculator = patternCalculator;
    }

    public List<String> getChordsFromScale(String tonic, Quality quality) {
        if (tonic == null || quality == null) {
            return Collections.emptyList();
        }

        List<String> semitones = patternCalculator.getSemitonesFromScale(tonic, quality);
        if (semitones.isEmpty()) {
            return Collections.emptyList();
        }

        Quality[] chordPattern = getChordPattern(quality);
        
        List<String> chords = new ArrayList<>();
        for (int i = 0; i < semitones.size(); i++) {
            String note = semitones.get(i);
            Quality chordQuality = chordPattern[i];
            String chord = note + (chordQuality == Quality.MAJOR ? "" : 
                                 chordQuality == Quality.MINOR ? "m" : "°");
            chords.add(chord);
        }
        return chords;
    }

    private Quality[] getChordPattern(Quality quality) {
        return switch (quality) {
            case MAJOR -> MAJOR_CHORD_PATTERN;
            case MINOR -> MINOR_CHORD_PATTERN;
            case HARMONIC_MINOR -> HARMONIC_MINOR_CHORD_PATTERN;
            case DORIAN -> DORIAN_CHORD_PATTERN;
            case PHRYGIAN -> PHRYGIAN_CHORD_PATTERN;
            case LYDIAN -> LYDIAN_CHORD_PATTERN;
            case MIXOLYDIAN -> MIXOLYDIAN_CHORD_PATTERN;
            case LOCRIAN -> LOCRIAN_CHORD_PATTERN;
            case PENTATONIC_MAJOR -> PENTATONIC_MAJOR_CHORD_PATTERN;
            case PENTATONIC_MINOR -> PENTATONIC_MINOR_CHORD_PATTERN;
            default -> MINOR_CHORD_PATTERN;
        };
    }

    public Map<String, String> createChordsMap(List<String> chords, Quality quality) {
        if (chords == null || chords.isEmpty() || quality == null) {
            return Collections.emptyMap();
        }

        Map<String, String> chordsMap = new HashMap<>();
        List<String> positions = getPositions(quality);
        
        for (int i = 0; i < chords.size(); i++) {
            chordsMap.put(positions.get(i), chords.get(i));
        }
        return chordsMap;
    }

    private List<String> getPositions(Quality quality) {
        return switch (quality) {
            case MAJOR -> MAJOR_POSITIONS;
            case MINOR -> MINOR_POSITIONS;
            case HARMONIC_MINOR -> HARMONIC_POSITIONS;
            case PENTATONIC_MAJOR -> PENT_MAJOR_POSITIONS;
            case PENTATONIC_MINOR -> PENT_MINOR_POSITIONS;
            case MELODIC_MINOR, DORIAN, PHRYGIAN, LYDIAN, MIXOLYDIAN, LOCRIAN -> MINOR_POSITIONS;
            case DIMINISHED -> MAJOR_POSITIONS;
        };
    }

    public Map.Entry<String, Quality> parseChord(String chord) {
        if (chord == null || chord.isEmpty()) {
            return null;
        }

        Matcher matcher = CHORD_PATTERN.matcher(chord);
        if (!matcher.matches()) {
            System.out.println("DEBUG: Pattern did not match for chord: " + chord);
            return null;
        }

        String root = matcher.group(1);
        String quality = matcher.group(2);

        // Handle slash chords by taking the root note only
        if (root.contains("/")) {
            root = root.split("/")[0];
        }

        Quality chordQuality = determineQuality(quality);
        return new AbstractMap.SimpleEntry<>(root, chordQuality);
    }

    private Quality determineQuality(String quality) {
        if (quality == null || quality.isEmpty() || quality.equals("M")) {
            return Quality.MAJOR;
        }
        if (quality.equals("m")) {
            return Quality.MINOR;
        }
        if (quality.equals("°")) {
            return Quality.DIMINISHED;
        }
        return Quality.MAJOR; // Default to major if unspecified
    }
} 
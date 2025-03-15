package com.composeit.backend.scaleservice;

import static com.composeit.backend.common.Constants.*;
import com.composeit.backend.scaleservice.models.Quality;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProgressionCalculator {
    public List<List<String>> createCommonProgressions(Quality quality) {
        if (quality == null) {
            return Collections.emptyList();
        }

        List<List<String>> commonProgressions = new ArrayList<>();
        switch (quality) {
            case MAJOR:
                commonProgressions.add(Arrays.asList(MAJOR_I, MAJOR_IV, MAJOR_V));               // I-IV-V (Blues)
                commonProgressions.add(Arrays.asList(MAJOR_I, MAJOR_V, MAJOR_VI, MAJOR_IV));     // I-V-vi-IV (Pop)
                commonProgressions.add(Arrays.asList(MAJOR_II, MAJOR_V, MAJOR_I));               // ii-V-I (Jazz)
                commonProgressions.add(Arrays.asList(MAJOR_VI, MAJOR_IV, MAJOR_I, MAJOR_V));     // vi-IV-I-V (Pop)
                commonProgressions.add(Arrays.asList(MAJOR_I, MAJOR_VI, MAJOR_IV, MAJOR_V));     // I-vi-IV-V (50s)
                commonProgressions.add(Arrays.asList(MAJOR_I, MAJOR_IV, MAJOR_VI, MAJOR_V));     // I-IV-vi-V
                break;
            case MINOR:
                commonProgressions.add(Arrays.asList(MINOR_I, MINOR_IV, MINOR_V));               // i-iv-v
                commonProgressions.add(Arrays.asList(MINOR_I, MINOR_VI, MINOR_VII, MINOR_V));    // i-VI-VII-v
                commonProgressions.add(Arrays.asList(MINOR_II, MINOR_V, MINOR_I));               // ii°-v-i
                commonProgressions.add(Arrays.asList(MINOR_I, MINOR_III, MINOR_VII, MINOR_VI));  // i-III-VII-VI
                commonProgressions.add(Arrays.asList(MINOR_I, MINOR_VII, MINOR_VI, MINOR_V));    // i-VII-VI-v
                commonProgressions.add(Arrays.asList(MINOR_I, MINOR_IV, MINOR_VII, MINOR_III));  // i-iv-VII-III
                break;
            case HARMONIC_MINOR:
                commonProgressions.add(Arrays.asList(HARMONIC_I, HARMONIC_IV, HARMONIC_V));      // i-iv-V
                commonProgressions.add(Arrays.asList(HARMONIC_I, HARMONIC_V, HARMONIC_I));       // i-V-i
                commonProgressions.add(Arrays.asList(HARMONIC_II, HARMONIC_V, HARMONIC_I));      // ii°-V-i
                commonProgressions.add(Arrays.asList(HARMONIC_VI, HARMONIC_V, HARMONIC_I));      // VI-V-i
                commonProgressions.add(Arrays.asList(HARMONIC_I, HARMONIC_IV, HARMONIC_V, HARMONIC_I)); // i-iv-V-i
                break;
            case PENTATONIC_MAJOR:
                commonProgressions.add(Arrays.asList(PENT_MAJOR_I, PENT_MAJOR_V));              // I-V
                commonProgressions.add(Arrays.asList(PENT_MAJOR_I, PENT_MAJOR_VI, PENT_MAJOR_V)); // I-vi-V
                commonProgressions.add(Arrays.asList(PENT_MAJOR_I, PENT_MAJOR_II, PENT_MAJOR_V)); // I-ii-V
                commonProgressions.add(Arrays.asList(PENT_MAJOR_VI, PENT_MAJOR_V, PENT_MAJOR_I)); // vi-V-I
                commonProgressions.add(Arrays.asList(PENT_MAJOR_I, PENT_MAJOR_III, PENT_MAJOR_VI)); // I-iii-vi
                break;
            case PENTATONIC_MINOR:
                commonProgressions.add(Arrays.asList(PENT_MINOR_I, PENT_MINOR_IV, PENT_MINOR_V)); // i-iv-v
                commonProgressions.add(Arrays.asList(PENT_MINOR_I, PENT_MINOR_VII, PENT_MINOR_V)); // i-VII-v
                commonProgressions.add(Arrays.asList(PENT_MINOR_I, PENT_MINOR_III, PENT_MINOR_VII)); // i-III-VII
                commonProgressions.add(Arrays.asList(PENT_MINOR_IV, PENT_MINOR_I, PENT_MINOR_V)); // iv-i-v
                commonProgressions.add(Arrays.asList(PENT_MINOR_I, PENT_MINOR_V, PENT_MINOR_IV)); // i-v-iv (Blues)
                break;
            default:
                // For other modes, use minor progressions
                commonProgressions.add(Arrays.asList(MINOR_I, MINOR_IV, MINOR_V));
                commonProgressions.add(Arrays.asList(MINOR_I, MINOR_VI, MINOR_VII, MINOR_V));
                commonProgressions.add(Arrays.asList(MINOR_II, MINOR_V, MINOR_I));
        }
        return commonProgressions;
    }

    public String findRelativeScale(List<String> semitones, Quality quality) {
        if (semitones == null || semitones.isEmpty() || quality == null) {
            return null;
        }

        switch (quality) {
            // Major family - relative is 6th degree minor
            case MAJOR:
                return semitones.get(5) + " MINOR";
            case PENTATONIC_MAJOR:
                return semitones.get(4) + " PENTATONIC_MINOR";  // 5th note in pentatonic
                
            // Minor family - relative is 3rd degree major
            case MINOR:
            case HARMONIC_MINOR:
            case MELODIC_MINOR:
                return semitones.get(2) + " MAJOR";
            case PENTATONIC_MINOR:
                return semitones.get(1) + " PENTATONIC_MAJOR";  // 2nd note in pentatonic
                
            // Modes - relative is the major scale starting on different degrees
            case DORIAN:        // 2nd mode
                return semitones.get(6) + " MAJOR";  // Relative major starts a whole step below
            case PHRYGIAN:      // 3rd mode
                return semitones.get(5) + " MAJOR";  // Relative major starts a major third below
            case LYDIAN:        // 4th mode
                return semitones.get(4) + " MAJOR";  // Relative major starts a perfect fourth below
            case MIXOLYDIAN:    // 5th mode
                return semitones.get(3) + " MAJOR";  // Relative major starts a perfect fifth below
            case LOCRIAN:       // 7th mode
                return semitones.get(1) + " MAJOR";  // Relative major starts a major seventh below
                
            default:
                return null;
        }
    }
    
    public String findParallelScale(String tonic, Quality quality) {
        if (tonic == null || quality == null) {
            return null;
        }

        // Validate that the tonic is a valid note
        if (!ALL_NOTES.contains(tonic)) {
            return null;
        }

        switch (quality) {
            // Major family - parallel is same tonic minor
            case MAJOR:
                return tonic + " MINOR";
            case PENTATONIC_MAJOR:
                return tonic + " PENTATONIC_MINOR";
                
            // Minor family - parallel is same tonic major
            case MINOR:
            case HARMONIC_MINOR:
            case MELODIC_MINOR:
                return tonic + " MAJOR";
            case PENTATONIC_MINOR:
                return tonic + " PENTATONIC_MAJOR";
                
            // Modes - parallel is the same tonic major
            case DORIAN:
            case PHRYGIAN:
            case LYDIAN:
            case MIXOLYDIAN:
            case LOCRIAN:
                return tonic + " MAJOR";
                
            default:
                return null;
        }
    }

    private String normalizeNote(String note) {
        if (note == null) {
            return null;
        }

        System.out.println("Normalizing note: " + note);
        System.out.println("Is in ENHARMONIC_MAP? " + ENHARMONIC_MAP.containsKey(note));
        // Check if the note has an enharmonic equivalent
        if (ENHARMONIC_MAP.containsKey(note)) {
            String result = ENHARMONIC_MAP.get(note)[0];
            System.out.println("Normalized to: " + result);
            return result;  // Use sharp notation for internal processing
        }

        System.out.println("No normalization needed, returning: " + note);
        // For natural notes or notes without enharmonic equivalents, return as is
        return note;
    }

    private String formatNoteForDisplay(String note) {
        // If the note doesn't need formatting, return as is
        if (!ENHARMONIC_MAP.containsKey(note)) {
            return note;
        }

        // Get both sharp and flat versions of the note
        String sharpVersion = ENHARMONIC_MAP.get(note)[0];
        String flatVersion = ENHARMONIC_MAP.get(note)[1];

        // Use flat notation for flat scales - check both versions
        if (FLAT_SCALES.contains(sharpVersion) || FLAT_SCALES.contains(flatVersion)) {
            return flatVersion;
        }

        // Use sharp notation for sharp scales
        if (SHARP_SCALES.contains(sharpVersion) || SHARP_SCALES.contains(flatVersion)) {
            return sharpVersion;
        }

        // For natural note scales, prefer flat notation for Bb
        if (note.equals(A_SHARP) || note.equals(B_FLAT)) {
            return B_FLAT;
        }

        // For G#/Ab specifically, use G# notation only if not in a flat scale
        if ((note.equals(G_SHARP) || note.equals(A_FLAT) || 
             sharpVersion.equals(G_SHARP) || flatVersion.equals(A_FLAT)) &&
            !FLAT_SCALES.contains(sharpVersion) && !FLAT_SCALES.contains(flatVersion)) {
            return G_SHARP;
        }

        return sharpVersion;  // Default to sharp notation
    }
} 
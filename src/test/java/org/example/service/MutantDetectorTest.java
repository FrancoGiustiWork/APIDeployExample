package org.example.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MutantDetectorTest {

    private final MutantDetector detector = new MutantDetector();

    static Stream<Arguments> mutantDnaProvider() {
        return Stream.of(
                Arguments.of("Horizontal and Diagonal", new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"}, true),
                Arguments.of("Two Vertical", new String[]{"CAGA", "CATT", "CAGT", "CAGG"}, true),
                Arguments.of("Two Horizontal", new String[]{"AAAA", "CCCC", "TTAT", "AGAC"}, true),
                Arguments.of("Two Diagonal", new String[]{"AGTC", "GACT", "TCAA", "CTGA"}, true),
                Arguments.of("All Same Character", new String[]{"AAAA", "AAAA", "AAAA", "AAAA"}, true),
                Arguments.of("Large DNA", new String[]{"ATGCGAATGC", "CAGTGCATGC", "TTATGTATGC", "AGAAGGATGC", "CCCCTAATGC", "TCACTGATGC", "ATGCGAATGC", "CAGTGCATGC", "TTATGTATGC", "AGAAGGATGC"}, true)
        );
    }

    static Stream<Arguments> humanDnaProvider() {
        return Stream.of(
                Arguments.of("Only one sequence", new String[]{"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"}, false),
                Arguments.of("No sequences", new String[]{"ATGCGA", "CAGTGC", "TTATGC", "AGACGG", "GCGTCA", "TCACTG"}, false),
                Arguments.of("Small DNA no sequence", new String[]{"ATGC", "CAGT", "TTAT", "AGAC"}, false)
        );
    }

    static Stream<Arguments> invalidDnaProvider() {
        return Stream.of(
                Arguments.of("Null DNA", null, false),
                Arguments.of("Empty DNA", new String[]{}, false),
                Arguments.of("Too Small DNA", new String[]{"AT", "CG"}, false)
        );
    }

    @ParameterizedTest(name = "DNA isMutant: {0}")
    @MethodSource("mutantDnaProvider")
    @DisplayName("Should return true for mutant DNA")
    void testIsMutant_WhenDnaIsMutant(String name, String[] dna, boolean expected) {
        assertEquals(expected, detector.isMutant(dna));
    }

    @ParameterizedTest(name = "DNA isMutant: {0}")
    @MethodSource("humanDnaProvider")
    @DisplayName("Should return false for human DNA")
    void testIsMutant_WhenDnaIsHuman(String name, String[] dna, boolean expected) {
        assertEquals(expected, detector.isMutant(dna));
    }

    @ParameterizedTest(name = "DNA isMutant: {0}")
    @MethodSource("invalidDnaProvider")
    @DisplayName("Should return false for invalid DNA")
    void testIsMutant_WhenDnaIsInvalid(String name, String[] dna, boolean expected) {
        assertEquals(expected, detector.isMutant(dna));
    }
}
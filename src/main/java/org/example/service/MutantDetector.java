package org.example.service;

import org.springframework.stereotype.Component;

@Component
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final int MIN_SEQUENCES_FOR_MUTANT = 2;

    public boolean isMutant(String[] dna) {
        if (dna == null) return false;
        final int n = dna.length;
        if (n < SEQUENCE_LENGTH) return false;

        char[][] matrix = new char[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        int sequenceCount = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Horizontal (→)
                if (j <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, i, j)) {
                        sequenceCount++;
                    }
                }
                // Vertical (↓)
                if (i <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, i, j)) {
                        sequenceCount++;
                    }
                }
                // Diagonal Descending (↘)
                if (i <= n - SEQUENCE_LENGTH && j <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalDescending(matrix, i, j)) {
                        sequenceCount++;
                    }
                }
                // Diagonal Ascending (↗)
                if (i >= SEQUENCE_LENGTH - 1 && j <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalAscending(matrix, i, j)) {
                        sequenceCount++;
                    }
                }

                if (sequenceCount >= MIN_SEQUENCES_FOR_MUTANT) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
               matrix[row][col + 2] == base &&
               matrix[row][col + 3] == base;
    }

    private boolean checkVertical(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col] == base &&
               matrix[row + 2][col] == base &&
               matrix[row + 3][col] == base;
    }

    private boolean checkDiagonalDescending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col + 1] == base &&
               matrix[row + 2][col + 2] == base &&
               matrix[row + 3][col + 3] == base;
    }

    private boolean checkDiagonalAscending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row - 1][col + 1] == base &&
               matrix[row - 2][col + 2] == base &&
               matrix[row - 3][col + 3] == base;
    }
}
package org.example.service;

import org.example.dto.DnaRequest;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @Mock
    private MutantDetector mutantDetector;

    @InjectMocks
    private MutantService mutantService;

    private DnaRequest mutantDnaRequest;
    private DnaRequest humanDnaRequest;

    @BeforeEach
    void setUp() {
        mutantDnaRequest = new DnaRequest();
        mutantDnaRequest.setDna(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"});

        humanDnaRequest = new DnaRequest();
        humanDnaRequest.setDna(new String[]{"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"});
    }

    @Test
    @DisplayName("Should return true from cache when DNA is mutant")
    void testIsMutant_ReturnsTrueFromCache_WhenDnaIsMutant() {
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(new DnaRecord("somehash", true)));

        boolean result = mutantService.isMutant(mutantDnaRequest);

        assertTrue(result);
        verify(dnaRecordRepository).findByDnaHash(anyString());
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should analyze and save when DNA is not in cache and is mutant")
    void testIsMutant_AnalyzesAndSaves_WhenNotInCacheAndIsMutant() {
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(mutantDnaRequest.getDna())).thenReturn(true);

        boolean result = mutantService.isMutant(mutantDnaRequest);

        assertTrue(result);
        verify(dnaRecordRepository).findByDnaHash(anyString());
        verify(mutantDetector).isMutant(mutantDnaRequest.getDna());
        verify(dnaRecordRepository).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Should analyze and save when DNA is not in cache and is human")
    void testIsMutant_AnalyzesAndSaves_WhenNotInCacheAndIsHuman() {
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(humanDnaRequest.getDna())).thenReturn(false);

        boolean result = mutantService.isMutant(humanDnaRequest);

        assertFalse(result);
        verify(dnaRecordRepository).findByDnaHash(anyString());
        verify(mutantDetector).isMutant(humanDnaRequest.getDna());
        verify(dnaRecordRepository).save(any(DnaRecord.class));
    }
}
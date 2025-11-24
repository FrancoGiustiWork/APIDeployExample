package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.DnaRequest;
import org.example.entity.DnaRecord;
import org.example.exeption.DnaHashCalculationException;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MutantService {

    private final DnaRecordRepository dnaRecordRepository;
    private final MutantDetector mutantDetector;

    @Transactional
    public boolean isMutant(DnaRequest dnaRequest) {
        String[] dna = dnaRequest.getDna();
        String dnaHash = calculateDnaHash(dna);

        Optional<DnaRecord> cachedResult = dnaRecordRepository.findByDnaHash(dnaHash);
        if (cachedResult.isPresent()) {
            return cachedResult.get().isMutant();
        }

        boolean isMutantResult = mutantDetector.isMutant(dna);
        DnaRecord newRecord = new DnaRecord(dnaHash, isMutantResult);
        dnaRecordRepository.save(newRecord);

        return isMutantResult;
    }

    private String calculateDnaHash(String[] dna) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String dnaString = String.join("", dna);
            byte[] hash = digest.digest(dnaString.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new DnaHashCalculationException("Error calculating SHA-256 hash", e);
        }
    }
}

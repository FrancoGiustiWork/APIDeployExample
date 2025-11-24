package org.example.dto;

import lombok.Data;
import org.example.validation.ValidDnaSequence;

@Data
public class DnaRequest {

    @ValidDnaSequence
    private String[] dna;
}
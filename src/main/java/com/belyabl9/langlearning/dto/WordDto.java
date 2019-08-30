package com.belyabl9.langlearning.dto;

import lombok.Data;

import java.util.Collection;

public @Data class WordDto {
    private final long id;
    private final String original;
    private final String translation;
    private final Collection<String> usageExamples;
    private final String associationImgUrl;
}

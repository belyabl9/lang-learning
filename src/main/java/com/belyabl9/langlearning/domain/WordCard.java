package com.belyabl9.langlearning.domain;

import lombok.Data;

import java.util.Collection;

public @Data class WordCard {
    private final Long id;
    private final String original;
    private final String translation;
    private final String category;
    private final String associationImgUrl;
    private final Collection<String> usageExamples;

    public WordCard(Long id, String original, String translation, String category, String associationImgUrl, Collection<String> usageExamples) {
        this.id = id;
        this.original = original;
        this.translation = translation;
        this.category = category;
        this.associationImgUrl = associationImgUrl;
        this.usageExamples = usageExamples;
    }
}
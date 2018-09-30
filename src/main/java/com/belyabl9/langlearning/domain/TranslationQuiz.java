package com.belyabl9.langlearning.domain;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

public @Data class TranslationQuiz {
    private final Long id;
    private final String original;
    private final String translation;
    private final String category;
    private final LinkedHashSet<String> answers;
    private final String associationImgUrl;

    public TranslationQuiz(Long id, String original, String translation, String category, Set<String> answers, String associationImgUrl) {
        this.id = id;
        this.original = original;
        this.translation = translation;
        this.category = category;
        this.answers = new LinkedHashSet<>(answers);
        this.associationImgUrl = associationImgUrl;
    }
}
package com.belyabl9.langlearning.domain;

import lombok.Getter;

@Getter
public enum Language {
    ENGLISH("en"),
    GERMAN("de");

    private final String code;

    Language(String code) {
        this.code = code;
    }
    
    public static Language fromCode(String code) {
        for (Language lang : values()) {
            if (lang.getCode().equals(code)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Can not find a language for a specified code");
    }
}

package com.belyabl9.langlearning.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class WordLearningSetup {
    
    private Set<Long> categoryIds;

    public WordLearningSetup() {}

    public WordLearningSetup(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}

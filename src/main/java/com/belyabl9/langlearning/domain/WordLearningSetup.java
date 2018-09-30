package com.belyabl9.langlearning.domain;

import java.util.Set;

public class WordLearningSetup {
    
    private Set<Long> categoryIds;

    public WordLearningSetup() {
    }

    public WordLearningSetup(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Set<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}

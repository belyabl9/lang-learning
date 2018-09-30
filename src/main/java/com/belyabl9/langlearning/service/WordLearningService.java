package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.domain.TranslationQuiz;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Service
public interface WordLearningService {

    /**
     * Collects questions with answers for a specified category 
     * @param categoryId
     */
    @NotNull
    List<TranslationQuiz> collectQuestions(long categoryId);

    /**
     * Collects questions with answers for specified categories 
     * @param categoryIds set of category identifiers
     */
    @NotNull
    List<TranslationQuiz> collectQuestions(@NotNull Set<Long> categoryIds);
}

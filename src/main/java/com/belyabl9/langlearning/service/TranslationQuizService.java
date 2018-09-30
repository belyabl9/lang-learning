package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.domain.TranslationQuiz;
import com.belyabl9.langlearning.domain.Word;
import com.google.common.base.Optional;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public interface TranslationQuizService {

    /**
     * Creates a translation quiz (1 questioned word and N translation variants to choose from)
     *
     * @param wordsForTranslations a list of words which translations are chosen for a quiz
     * @param questionedWord a word that is being learned
     *
     * @return TranslationQuiz object or absent
     */
    @NotNull
    Optional<TranslationQuiz> makeTranslationQuiz(@NotNull List<Word> wordsForTranslations,
                                                  @NotNull Word questionedWord);
    
}

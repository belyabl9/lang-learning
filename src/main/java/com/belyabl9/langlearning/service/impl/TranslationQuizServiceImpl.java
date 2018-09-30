package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.*;
import com.belyabl9.langlearning.service.TranslationQuizService;
import com.google.common.base.Optional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TranslationQuizServiceImpl implements TranslationQuizService {

    @Value("${learning.translation_quiz.min_answers_num:4}")
    private int minNumberOfAnswers;
    
    @Override
    public Optional<TranslationQuiz> makeTranslationQuiz(@NonNull List<Word> wordsForTranslations,
                                                         @NonNull Word questionedWord) {
        LinkedHashSet<String> selectedTranslations = new LinkedHashSet<>(minNumberOfAnswers);
        
        // Add correct answer
        selectedTranslations.add(questionedWord.getTranslation());

        // filter questioned word, synonyms (to avoid multiple correct answers) and words with the same translation
        List<Word> answerVariants = wordsForTranslations.stream()
                .filter((word) -> {
                    boolean isSameTranslation = word.getTranslation().equals(questionedWord.getTranslation());
                    boolean isSynonym = questionedWord.getSynonyms().contains(word.getOriginal());
                    
                    return !isSynonym && !isSameTranslation;
                })
                .collect(Collectors.toList());

        
        for (int i = 0; i < minNumberOfAnswers - 1; i++) {
            Optional<String> translationAnswer = findTranslationAnswer(answerVariants, selectedTranslations);
            if (!translationAnswer.isPresent()) {
                return Optional.absent();
            }
            selectedTranslations.add(translationAnswer.get());
        }

        return Optional.of(
                new TranslationQuiz(
                        questionedWord.getId(),
                        questionedWord.getOriginal(),
                        questionedWord.getTranslation(),
                        questionedWord.getCategory().getName(),
                        shuffleSelectedTranslations(selectedTranslations),
                        questionedWord.getAssociationImg() != null ? questionedWord.getAssociationImg().getUrl() : null
                )
        );
    }

    /**
     * Loops through a list of shuffled words and searched for a unique translation for a list of selected ones
     *
     * @param answerVariants A list of available words to get translations from.
     *                       Words will be shuffled and selected one will be removed 
     * @param selectedTranslations Set of selected translations.
     *                             Used to check uniqueness of a newly selected translation
     */
    private Optional<String> findTranslationAnswer(@NonNull List<Word> answerVariants,
                                                   @NonNull Set<String> selectedTranslations) {
        Collections.shuffle(answerVariants);
        Iterator<Word> answerVariantIterator = answerVariants.iterator();
        while (answerVariantIterator.hasNext()) {
            Word answerVariant = answerVariantIterator.next();
            String translation = answerVariant.getTranslation();
            if (!selectedTranslations.contains(translation)) {
                answerVariantIterator.remove();
                return Optional.of(translation);
            }
        }
        return Optional.absent();
    }
    
    private LinkedHashSet<String> shuffleSelectedTranslations(@NonNull LinkedHashSet<String> selectedTranslations) {
        ArrayList<String> list = new ArrayList<>(selectedTranslations);
        Collections.shuffle(list);
        return new LinkedHashSet<>(list);
    }
}

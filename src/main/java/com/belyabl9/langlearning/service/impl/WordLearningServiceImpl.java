package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.TranslationQuiz;
import com.belyabl9.langlearning.domain.Word;
import com.belyabl9.langlearning.service.CategoryService;
import com.belyabl9.langlearning.service.TranslationQuizService;
import com.belyabl9.langlearning.service.WordLearningService;
import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class WordLearningServiceImpl implements WordLearningService {

    private static final List<PriorityRange> RISK_RANGES = ImmutableList.of(
            new PriorityRange(0, 5, 2),
            new PriorityRange(5, 10, 3),
            new PriorityRange(10, 15, 4),
            new PriorityRange(15, 20, 5),
            new PriorityRange(20, 1_000, 10)
    );

    private final CategoryService categoryService;
    private final TranslationQuizService translationQuizService;
    
    @Autowired
    public WordLearningServiceImpl(CategoryService categoryService,
                                   TranslationQuizService translationQuizService) {
        this.categoryService = categoryService;
        this.translationQuizService = translationQuizService;
    }

    @Override
    public List<TranslationQuiz> collectQuestions(long categoryId) {
        List<Word> words = collectWordsByCategory(categoryId);
        if (words.isEmpty()) {
            return Collections.emptyList();
        }
        List<TranslationQuiz> finalTranslationQuizzes = new ArrayList<>(words.size());

        // Group words by importance
        Multimap<PriorityRange, TranslationQuiz> priorityRangeQuestionsMap = ArrayListMultimap.create();
        for (Word word : words) {
            PriorityRange priorityRange = findRiskRange(word);
            Optional<TranslationQuiz> translationQuestion = translationQuizService.makeTranslationQuiz(words, word);
            if (translationQuestion.isPresent()) {
                priorityRangeQuestionsMap.put(priorityRange, translationQuestion.get());
            }
        }

        List<PriorityRange> priorityRanges = new ArrayList<>(priorityRangeQuestionsMap.keySet());
        Collections.sort(priorityRanges);


//          Words are divided into groups by importance.
//          Words which have high number of wrong guesses (high priority) will be repeated more times during a learning session.
//          Every word group is shuffled. Therefore, a user chooses answers not based on memorized order.
//          
//          Example:
//          
//          DIFFICULT_WORDS NORMAL_WORDS EASY_WORDS
//          DIFFICULT_WORDS NORMAL_WORDS EASY_WORDS
//          DIFFICULT_WORDS NORMAL_WORDS
//          DIFFICULT_WORDS
//          DIFFICULT_WORDS
        Multimap<Integer, TranslationQuiz> questionMap = ArrayListMultimap.create();
        for (PriorityRange priorityRange : priorityRanges) {
            for (int i = 0; i < priorityRange.getRepetitions(); i++) {
                List<TranslationQuiz> questionGroup = new ArrayList<>(priorityRangeQuestionsMap.get(priorityRange));
                Collections.shuffle(questionGroup);
                for (TranslationQuiz question : questionGroup) {
                    questionMap.put(i, question);
                }
            }
        }

        for (Integer groupNumber : questionMap.keySet()) {
            finalTranslationQuizzes.addAll(questionMap.get(groupNumber));
        }

        return finalTranslationQuizzes;
    }
    
    @Override
    public List<TranslationQuiz> collectQuestions(@NonNull Set<Long> categoryIds) {
        List<TranslationQuiz> translationQuizes = new ArrayList<>(50);
        for (long categoryId : categoryIds) {
            translationQuizes.addAll(collectQuestions(categoryId));
        }
        return translationQuizes;
    }

    private List<Word> collectWordsByCategory(long categoryId) {
        Category category = categoryService.findById(categoryId);
        if (category == null) {
            throw new RuntimeException("Can not find a category with a specified id: " + categoryId);
        }
        return category.getWords();
    }

    private static class PriorityRange implements Comparable<PriorityRange> {
        private final int min;
        private final int max;
        private final int repetitions;

        public PriorityRange(int min, int max, int repetitions) {
            this.min = min;
            this.max = max;
            this.repetitions = repetitions;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public int getRepetitions() {
            return repetitions;
        }

        public boolean isInRange(int value) {
            return value >= min && value <= max;
        }

        @Override
        public int compareTo(PriorityRange o) {
            return Integer.compare(o.getRepetitions(), getRepetitions());
        }
    }
    
    private PriorityRange findRiskRange(Word word) {
        for (PriorityRange priorityRange : RISK_RANGES) {
            if (priorityRange.isInRange(word.getPriority())) {
                return priorityRange;
            }
        }
        throw new UnsupportedOperationException();
    }

}

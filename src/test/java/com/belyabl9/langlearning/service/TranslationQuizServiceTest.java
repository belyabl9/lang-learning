package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.TestConfiguration;
import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.TranslationQuiz;
import com.belyabl9.langlearning.domain.Word;
import com.belyabl9.langlearning.service.impl.TranslationQuizServiceImpl;
import com.google.common.base.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class TranslationQuizServiceTest {

    @Autowired
    private TranslationQuizService translationQuizService;

    @Test
    public void makeQuestion() throws Exception {
        Category category = new Category("category");
        Word questionedWord = new Word("angry", "злой", category);
        
        List<Word> words = Arrays.asList(
                questionedWord,
                new Word("happy", "счастливый", category),
                new Word("pleased", "довольный", category),
                new Word("sad", "грустный", category)
        );

        Optional<TranslationQuiz> translationQuestion = translationQuizService.makeTranslationQuiz(words, questionedWord);
        Set<String> answers = translationQuestion.get().getAnswers();
        
        assertThat(answers.size()).isEqualTo(4);
    }
    
    @Test
    public void verifyTranslationsAreShuffled() {
        Category category = new Category("category");
        Word questionedWord = new Word("angry", "злой", category);

        List<Word> words = Arrays.asList(
                questionedWord,
                new Word("happy", "счастливый", category),
                new Word("pleased", "довольный", category),
                new Word("sad", "грустный", category)
        );

        Set<String> firstTranslationSet = new HashSet<>();
        LinkedHashSet<String> answers;
        
        for (int i = 0; i < 10; i++) {
            Optional<TranslationQuiz> translationQuestion = translationQuizService.makeTranslationQuiz(words, questionedWord);
            answers = translationQuestion.get().getAnswers();

            Iterator<String> iterator = answers.iterator();
            String firstTranslation = iterator.next();
            firstTranslationSet.add(firstTranslation);
        }
        
        assertThat(firstTranslationSet.size()).isGreaterThan(1);
    }

    @Test
    public void whenNotEnoughAnswersReturnAbsent() throws Exception {
        Category category = new Category("category");
        Word questionedWord = new Word("angry", "злой", category);

        List<Word> words = Arrays.asList(
                questionedWord,
                new Word("happy", "счастливый", category),
                new Word("pleased", "довольный", category)
        );

        Optional<TranslationQuiz> translationQuestion = translationQuizService.makeTranslationQuiz(words, questionedWord);
        assertThat(translationQuestion.isPresent()).isFalse();
    }

    @Test
    public void whenSynonymsAreOfferedSkipThem() throws Exception {
        Category category = new Category("category");
        Word questionedWord = new Word("angry", "злой", category);

        List<Word> words = Arrays.asList(
                questionedWord,
                new Word("happy", "счастливый", category),
                new Word("pleased", "довольный", category),
                new Word("cross", "злой", category)
        );

        Optional<TranslationQuiz> translationQuestion = translationQuizService.makeTranslationQuiz(words, questionedWord);
        assertThat(translationQuestion.isPresent()).isFalse();
    }

}
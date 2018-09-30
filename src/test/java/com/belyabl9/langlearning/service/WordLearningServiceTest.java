package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.domain.*;
import com.belyabl9.langlearning.service.impl.WordLearningServiceImpl;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class WordLearningServiceTest {

    private WordLearningService wordLearningService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private TranslationQuizService translationQuizService;

    @Before
    public void setUp() {
        initMocks(this);
        wordLearningService = new WordLearningServiceImpl(categoryService, translationQuizService);
    }
    
    @Test
    public void collectWords() throws Exception {
        User user = new InternalUser("Ivan Ivanov", "test@gmail.com", "login", "password", true);
        user.setId(1l);
        Category category = makeCategory(user);

        when(categoryService.findById(1l)).thenReturn(category);
        when(translationQuizService.makeTranslationQuiz(notNull(List.class), notNull(Word.class))).thenReturn(
                Optional.of(
                        new TranslationQuiz(1l, "word1", "translation", "category", ImmutableSet.of(
                        "translation 1",
                        "translation 2",
                        "translation 3",
                        "translation 4"
                        ), null)
                )
        );
        List<TranslationQuiz> words = wordLearningService.collectQuestions(1l);

        // 3*2 + 2*3 + 2*4 = 20
        assertThat(words.size()).isEqualTo(20);
    }
    
    private Category makeCategory(User user) {
        Category category = new Category("category");
        category.setWords(
                ImmutableList.of(
                    // 3 (words) * 2 (repetitions based on priority)
                    new Word("word1", "translation", category, 0),
                    new Word("word2", "translation", category, 1),
                    new Word("word3", "translation", category, 5),

                    // 2 (words) * 3 (repetitions based on priority)
                    new Word("word4", "translation", category, 6),
                    new Word("word5", "translation", category, 10),

                    // 2 (words) * 4 (repetitions based on priority)
                    new Word("word6", "translation", category, 11),
                    new Word("word7", "translation", category, 15)
                )
        );
        category.setUser(user);
        return category;
    }

}
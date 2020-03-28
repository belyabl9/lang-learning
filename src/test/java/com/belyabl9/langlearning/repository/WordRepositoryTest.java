package com.belyabl9.langlearning.repository;

import com.belyabl9.langlearning.TestConfiguration;
import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.Language;
import com.belyabl9.langlearning.domain.Word;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class WordRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WordRepository wordRepository;

    @Before
    public void cleanUp() {
        entityManager.clear();
    }
    
    @Test
    public void findAllByCategoryOrderByPriority() throws Exception {
        Category categoryOne = new Category("categoryOne", Language.ENGLISH);
        categoryOne.setWords(ImmutableList.of(
                new Word("word1", "translation1", categoryOne, 5),
                new Word("word2", "translation2", categoryOne, 3),
                new Word("word3", "translation3", categoryOne, 4)
        ));
        categoryOne = entityManager.persistAndFlush(categoryOne);
        
        Category categoryTwo = new Category("categoryTwo", Language.ENGLISH);
        categoryTwo.setWords(ImmutableList.of(
                new Word("word4", "translation4", categoryTwo, 5),
                new Word("word5", "translation5", categoryTwo, 3),
                new Word("word6", "translation6", categoryTwo, 4)
        ));
        categoryTwo = entityManager.persistAndFlush(categoryTwo);

        List<Word> words = wordRepository.findAllByCategoryOrderByPriorityDesc(categoryOne);
        assertThat(words.size()).isEqualTo(3);
        assertThat(words.get(0).getOriginal()).isEqualTo("word1");
        assertThat(words.get(1).getOriginal()).isEqualTo("word3");
        assertThat(words.get(2).getOriginal()).isEqualTo("word2");

        assertThat(words.get(0).getPriority()).isGreaterThan(words.get(1).getPriority());
        assertThat(words.get(1).getPriority()).isGreaterThan(words.get(2).getPriority());
    }

    @Test
    public void incrementPriority() throws Exception {
        Category category = new Category("category", Language.ENGLISH);
        Word word = new Word("word1", "translation1", category);
        category.setWords(ImmutableList.of(word));
        category = entityManager.persistAndFlush(category);
        
        word = category.getWords().get(0);
        wordRepository.incrementPriority(ImmutableSet.of(word.getId()));
        wordRepository.incrementPriority(ImmutableSet.of(word.getId()));
        wordRepository.incrementPriority(ImmutableSet.of(word.getId()));

        // don't know why, but it's required here
        entityManager.refresh(word);
        
        assertThat(word.getPriority()).isEqualTo(3);
    }

    @Test
    public void decrementPriority() throws Exception {
        Category category = new Category("category", Language.ENGLISH);
        Word word = new Word("word1", "translation1", category, 5);
        category.setWords(ImmutableList.of(word));
        category = entityManager.persistAndFlush(category);

        word = category.getWords().get(0);
        wordRepository.decrementPriority(ImmutableSet.of(word.getId()));
        wordRepository.decrementPriority(ImmutableSet.of(word.getId()));
        wordRepository.decrementPriority(ImmutableSet.of(word.getId()));
        
        // don't know why, but it's required here
        entityManager.refresh(word);

        assertThat(word.getPriority()).isEqualTo(2);
    }
    
}
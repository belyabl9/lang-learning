package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.TestConfiguration;
import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.InternalUser;
import com.belyabl9.langlearning.domain.Language;
import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.domain.Word;
import com.belyabl9.langlearning.service.CategoryService;
import com.belyabl9.langlearning.service.UserService;
import com.belyabl9.langlearning.service.WordImporterService;
import com.belyabl9.langlearning.service.WordService;
import com.belyabl9.langlearning.service.impl.importer.ActionOnDuplicate;
import com.belyabl9.langlearning.service.impl.importer.ImporterSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class WordImporterServiceImplTest {
    private static final String IMPORT_FILE_CONTENT = "word1 ; translation1\nword2 ; translation2\n wordN ; translation N";

    @Autowired
    private WordImporterService wordImporterService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private WordService wordService;

    /**
     * Stored words: Word0 Word1 (translation="translation1_to_be_replaced")
     * Words to be imported: Word1 (translation="translation1") Word2 WordN
     * Expected result: Word0 Word1 (translation="translation1") Word2 WordN
     */
    @Test
    public void importCategoryWordsReplacingDuplicates() throws Exception {
        Category category = prepareCategory();

        Word word = new Word("word0", "translation0", category);
        wordService.insert(word);
        word = new Word("word1", "translation1_to_be_replaced", category);
        wordService.insert(word);

        wordImporterService.importCategoryWords(
                category,
                new ByteArrayInputStream(IMPORT_FILE_CONTENT.getBytes(StandardCharsets.UTF_8)),
                new ImporterSettings(ActionOnDuplicate.REPLACE)
        );

        List<Word> wordsByCategory = wordService.findAllByCategory(category);
        assertThat(wordsByCategory).hasSize(4);
        Word replacedWord = wordService.findByOriginal(category, "word1");
        assertThat(replacedWord.getTranslation()).isNotEqualTo("translation1_to_be_replaced");
    }

    /**
     * Stored words: Word0 Word1 (translation="translation1_to_be_kept") Word10
     * Words to be imported: Word1 (translation="translation1") Word2 WordN
     * Expected result: Word0 Word1 (translation="translation1_to_be_kept") Word10 Word2 WordN
     */
    @Test
    public void importCategoryWordsSkippingDuplicates() throws Exception {
        Category category = prepareCategory();

        Word word = new Word("word0", "translation0", category);
        wordService.insert(word);
        word = new Word("word1", "translation1_to_be_kept", category);
        wordService.insert(word);
        word = new Word("word10", "translation10", category);
        wordService.insert(word);

        wordImporterService.importCategoryWords(
                category,
                new ByteArrayInputStream(IMPORT_FILE_CONTENT.getBytes(StandardCharsets.UTF_8)),
                new ImporterSettings(ActionOnDuplicate.SKIP)
        );

        List<Word> wordsByCategory = wordService.findAllByCategory(category);
        assertThat(wordsByCategory).hasSize(5);
        Word replacedWord = wordService.findByOriginal(category, "word1");
        assertThat(replacedWord.getTranslation()).isEqualTo("translation1_to_be_kept");
    }

    /**
     * Stored words: WordA WordB Word2
     * Words to be imported: Word1 Word2 WordN
     * Expected result: WordA WordB Word2
     */
    @Test
    public void importCategoryWordsRollingBackIfDuplicateFound() throws Exception {
        Category category = prepareCategory();

        Word word = new Word("wordA", "translationA", category);
        wordService.insert(word);
        word = new Word("wordB", "translationB", category);
        wordService.insert(word);
        word = new Word("word2", "translation2", category);
        wordService.insert(word);

        wordImporterService.importCategoryWords(
                category,
                new ByteArrayInputStream(IMPORT_FILE_CONTENT.getBytes(StandardCharsets.UTF_8)),
                new ImporterSettings(ActionOnDuplicate.ERROR_ROLLBACK)
        );

        List<Word> wordsByCategory = wordService.findAllByCategory(category);
        assertThat(wordsByCategory).hasSize(3);
    }

    /**
     * Stored words: WordA WordB Word2
     * Words to be imported: Word1 Word2 WordN
     * Expected result: WordA WordB Word2 Word1
     */
    @Test
    public void importCategoryWordsStopButKeepImportedIfDuplicateFound() throws Exception {
        Category category = prepareCategory();

        Word word = new Word("wordA", "translationA", category);
        wordService.insert(word);
        word = new Word("wordB", "translationB", category);
        wordService.insert(word);
        word = new Word("word2", "translation2", category);
        wordService.insert(word);

        wordImporterService.importCategoryWords(
                category,
                new ByteArrayInputStream(IMPORT_FILE_CONTENT.getBytes(StandardCharsets.UTF_8)),
                new ImporterSettings(ActionOnDuplicate.ERROR)
        );

        List<Word> wordsByCategory = wordService.findAllByCategory(category);
        assertThat(wordsByCategory).hasSize(4);
    }

    private Category prepareCategory() {
        User user = new InternalUser("Test Test", "ivan.ivanov@gmail.com", "login", "password", true);
        user = userService.insert(user);

        Category category = new Category("category", new ArrayList<>(), Language.ENGLISH, user);
        category = categoryService.insert(category);
        return category;
    }
}
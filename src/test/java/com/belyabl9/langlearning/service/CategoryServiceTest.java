package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.TestConfiguration;
import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.InternalUser;
import com.belyabl9.langlearning.domain.Language;
import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.domain.Word;
import com.belyabl9.langlearning.exception.EntityExistsException;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class CategoryServiceTest {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WordService wordService;

    @Test
    public void findById() throws Exception {
        User user = userService.insert(new InternalUser("Ivan Ivanov", "ivan.ivanov@gmail.com", "login", "password", true));

        Category categoryOne = categoryService.insert(
                new Category("categoryOne", new ArrayList<>(), Language.ENGLISH, user)
        );

        Category category = categoryService.findById(categoryOne.getId());
        assertThat(categoryOne).isEqualTo(category);
    }

    /**
     * Built-in categories are not bound to any user
     */
    @Test
    public void findBuiltInCategories() throws Exception {
        User user = userService.insert(new InternalUser("Ivan Ivanov", "ivan.ivanov@gmail.com", "login", "password", true));
        List<Category> categories = ImmutableList.of(
                // built-in categories
                new Category("builtInCategoryOne", new ArrayList<>(), Language.ENGLISH),
                new Category("builtInCategoryTwo", new ArrayList<>(), Language.ENGLISH),
                // user categories
                new Category("userCategoryOne", new ArrayList<>(), Language.ENGLISH, user)
        );
        for (Category category : categories) {
            categoryService.insert(category);
        }

        List<Category> builtInCategories = categoryService.findBuiltInCategories();
        assertThat(builtInCategories).hasSize(2);
        for (Category builtInCategory : builtInCategories) {
            assertThat(builtInCategory.getUser()).isNull();
        }
    }

    @Test
    public void findUserCategories() throws Exception {
        User user = userService.insert(new InternalUser("Ivan Ivanov", "ivan.ivanov@gmail.com", "login", "password", true));
        user.setLearningLang(Language.ENGLISH);
        List<Category> categories = ImmutableList.of(
                // built-in categories
                new Category("builtInCategoryOne", new ArrayList<>(), Language.ENGLISH),
                new Category("builtInCategoryTwo", new ArrayList<>(), Language.ENGLISH),
                // user categories
                new Category("userCategoryOne", new ArrayList<>(), Language.ENGLISH, user)
        );
        for (Category category : categories) {
            categoryService.insert(category);
        }

        List<Category> userCategories = categoryService.findUserCategories(user);
        assertThat(userCategories.size()).isEqualTo(1);
        assertThat(userCategories.get(0).getName()).isEqualTo("userCategoryOne");
    }
    
    @Test
    public void copy() {
        User user = userService.insert(new InternalUser("Ivan Ivanov", "ivan.ivanov@gmail.com", "login", "password", true));

        Category categoryOne = makeCategory("categoryOne", user);

        Category copiedCategory = categoryService.copy(categoryOne, user, "categoryTwo");

        assertThat(copiedCategory.getId()).isNotNull();
        assertThat(copiedCategory.getId()).isNotSameAs(categoryOne.getId());

        assertThat(categoryOne.getWords()).hasSameSizeAs(copiedCategory.getWords());
        
        Word copiedWord = copiedCategory.getWords().get(0);
        Word word = categoryOne.getWords().get(0);

        assertThat(copiedWord).isNotSameAs(word);

        assertThat(copiedWord).isEqualTo(word);
        assertThat(copiedWord.getId()).isNotNull();
        assertThat(word.getId()).isNotNull();
        assertThat(copiedWord.getId()).isNotEqualTo(word.getId());
    }

    @Test(expected = EntityExistsException.class)
    public void whenCategoryNameExistsThrow() {
        User user = userService.insert(new InternalUser("Ivan Ivanov", "ivan.ivanov@gmail.com", "login", "password", true));
        Category categoryOne = makeCategory("categoryOne", user);
        categoryService.copy(categoryOne, user, "categoryOne");
    }

    @Test
    public void moveWords() {
        User user = userService.insert(new InternalUser("Ivan Ivanov", "ivan.ivanov@gmail.com", "login", "password", true));

        Category categoryOne = makeCategory("categoryOne", user);
        Category categoryTwo = makeCategory("categoryTwo", user);
        
        assertThat(categoryOne.getWords().size()).isEqualTo(1);
        assertThat(categoryTwo.getWords().size()).isEqualTo(1);
        
        categoryService.moveWords(categoryOne.getWords(), categoryTwo);

        assertThat(categoryOne.getWords().size()).isEqualTo(0);
        assertThat(categoryTwo.getWords().size()).isEqualTo(2);
    }

    @Test
    public void copyWords() {
        User user = userService.insert(new InternalUser("Ivan Ivanov", "ivan.ivanov@gmail.com", "login", "password", true));

        Category categoryOne = makeCategory("categoryOne", user);
        Category categoryTwo = makeCategory("categoryTwo", user);

        assertThat(categoryOne.getWords().size()).isEqualTo(1);
        assertThat(categoryTwo.getWords().size()).isEqualTo(1);

        categoryService.copyWords(categoryOne.getWords(), categoryTwo);

        assertThat(categoryOne.getWords().size()).isEqualTo(1);
        assertThat(categoryTwo.getWords().size()).isEqualTo(2);
    }
    
    private Category makeCategory(String categoryName, User user) {
        Category category = categoryService.insert(
                new Category(categoryName, new ArrayList<>(), Language.ENGLISH, user)
        );

        wordService.insert(
                new Word("word_" + categoryName, "translation", category)
        );
        
        return category;
    }
}
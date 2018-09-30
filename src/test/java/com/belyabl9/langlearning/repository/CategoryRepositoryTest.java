package com.belyabl9.langlearning.repository;

import com.belyabl9.langlearning.TestConfiguration;
import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.InternalUser;
import com.belyabl9.langlearning.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void cleanUp() {
        entityManager.clear();
    }
    
    @Test
    public void findAllByUser() throws Exception {
        User user = new InternalUser("Ivan Ivanov", "ivan.ivanov@gmail.com", "login", "password", true);
        user = entityManager.persistAndFlush(user);

        Category moodsCategory = new Category("moods", Collections.emptyList(), user);
        Category feelingsCategory = new Category("feelings", Collections.emptyList(), user);
        entityManager.persistAndFlush(moodsCategory);
        entityManager.persistAndFlush(feelingsCategory);

        List<Category> categories = categoryRepository.findAllByUser(user);
        assertThat(categories.size()).isEqualTo(2);
        assertThat(categories).contains(moodsCategory, feelingsCategory);
    }

    @Test
    public void findAllByUserIsNull() throws Exception {
        User user = new InternalUser("Ivan Ivanov", "ivan.ivanov@gmail.com", "login", "password", true);
        user = entityManager.persistAndFlush(user);
        
        Category builtInCategoryOne = new Category("builtInCategoryOne");
        Category builtInCategoryTwo = new Category("builtInCategoryTwo");
        Category userCategory = new Category("userCategory", Collections.emptyList(), user);
        entityManager.persistAndFlush(builtInCategoryOne);
        entityManager.persistAndFlush(builtInCategoryTwo);
        entityManager.persistAndFlush(userCategory);

        List<Category> categories = categoryRepository.findAllByUserIsNull();
        assertThat(categories.size()).isEqualTo(2);
        assertThat(categories).contains(builtInCategoryOne, builtInCategoryTwo);
    }

}
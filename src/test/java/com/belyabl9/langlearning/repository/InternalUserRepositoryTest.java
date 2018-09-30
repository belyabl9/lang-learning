package com.belyabl9.langlearning.repository;

import com.belyabl9.langlearning.TestConfiguration;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class InternalUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InternalUserRepository userRepository;

    @Before
    public void cleanUp() {
        entityManager.clear();
    }
    
    @Test
    public void findByLogin() throws Exception {
        User foundUser = userRepository.findByLogin("login");
        assertThat(foundUser).isNull();
        
        User user = new InternalUser("Ivan Ivanov", "ivan.ivanov@gmail.com", "login", "password", true);
        entityManager.persistAndFlush(user);

        foundUser = userRepository.findByLogin("login");
        assertThat(foundUser).isNotNull();
    }

}
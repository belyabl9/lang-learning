package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.TestConfiguration;
import com.belyabl9.langlearning.domain.InternalUser;
import com.belyabl9.langlearning.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void insert() throws Exception {
        User user = new InternalUser("Ivan Ivanov", "ivan.ivanov@gmail.com", "login", "password", true);
        user = userService.insert(user);
        
        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo("Ivan Ivanov");
        assertThat(user.getEmail()).isEqualTo("ivan.ivanov@gmail.com");
        assertThat(user.getLogin()).isEqualTo("login");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.isEnabled()).isEqualTo(true);
    }

}
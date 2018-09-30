package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.TestConfiguration;
import com.belyabl9.langlearning.domain.InternalUser;
import com.belyabl9.langlearning.domain.User;
import org.junit.Before;
import org.junit.BeforeClass;
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
public class InternalUserServiceTest {

    @Autowired
    private UserService userService;
    
    @Autowired
    private InternalUserService internalUserService;

    private User user;
    
    @Before
    public void setUp() {
        user = userService.insert(new InternalUser("Ivan Ivanov", "ivan.ivanov@gmail.com", "login", "password", true));
    }
    
    @Test
    public void findById() throws Exception {
        User foundUser = internalUserService.findById(user.getId());
        assertThat(foundUser.getName()).isEqualTo("Ivan Ivanov");
        assertThat(foundUser.getLogin()).isEqualTo("login");
    }

    @Test
    public void findByLogin() throws Exception {
        User foundUser = internalUserService.findByLogin("login");
        assertThat(foundUser.getName()).isEqualTo("Ivan Ivanov");
        assertThat(foundUser.getLogin()).isEqualTo("login");
    }

    @Test
    public void remove() throws Exception {
        internalUserService.remove(user.getId());
        assertThat(internalUserService.findById(user.getId())).isNull();
    }

}
package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class MeriamWebsterDictParserServiceTest {
    
    @Autowired
    private MeriamWebsterDictParserService service;
    
    @Test
    public void findAll() throws Exception {
        Set<String> examples = service.findUsageExamples("joyful");
        assertThat(examples).isNotEmpty();
    }

}
package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.service.impl.ThesarusSynonymService;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ThesarusSynonymServiceTest {
    
    @Test
    public void findAll() throws Exception {
        ThesarusSynonymService synonymService = new ThesarusSynonymService();
        Set<String> words = synonymService.findAll("pleased");
        assertThat(words).contains("happy", "satisfied");
    }

}
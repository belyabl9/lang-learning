package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.Language;
import com.belyabl9.langlearning.service.LanguageService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {
    
    @Override
    public List<Language> findLanguages() {
        return Arrays.asList(Language.values());
    }

}

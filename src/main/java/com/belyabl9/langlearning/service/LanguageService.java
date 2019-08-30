package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.domain.Language;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LanguageService {

    /**
     * Returns a list of all available languages
     * @return a list of all available languages
     */
    List<Language> findLanguages();
    
}

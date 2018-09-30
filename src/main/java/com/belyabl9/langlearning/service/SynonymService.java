package com.belyabl9.langlearning.service;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Service
public interface SynonymService {

    /**
     * Returns a set of synonyms for a given word
     */
    @NotNull
    Set<String> findAll(@NotNull String word);
}

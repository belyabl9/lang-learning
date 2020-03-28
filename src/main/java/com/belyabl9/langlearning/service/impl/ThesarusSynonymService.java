package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.service.SynonymService;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

// TODO
@Service
public class ThesarusSynonymService implements SynonymService {
    
    @Override
    public Set<String> findAll(@NonNull String word) {
        return Collections.emptySet();
    }
}

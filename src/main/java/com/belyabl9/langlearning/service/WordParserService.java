package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.domain.Word;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.List;

@Service
public interface WordParserService {

    /**
     * Parses words from input stream
     */
    @NotNull
    List<Word> parse(@NotNull InputStream inputStream);
}

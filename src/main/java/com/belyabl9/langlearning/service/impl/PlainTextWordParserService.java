package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.Word;
import com.belyabl9.langlearning.service.WordParserService;
import com.google.common.base.Splitter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// TODO: make file format configurable
@Service
@Qualifier("plain-text")
public class PlainTextWordParserService implements WordParserService {

    private static final Splitter SPLITTER = Splitter.on(";").trimResults();

    /**
     * Expects an input stream of a text file with ";"-separated word and translation.
     *
     * Example:
     * angry ; злой
     * pleased ; довольный
     */
    @Override
    public List<Word> parse(@NonNull InputStream inputStream) {
        List<Word> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                List<String> fields = SPLITTER.splitToList(line);
                Word word = new Word();
                word.setOriginal(fields.get(0));
                word.setTranslation(fields.get(1));
                words.add(word);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return words;
    }
    
}

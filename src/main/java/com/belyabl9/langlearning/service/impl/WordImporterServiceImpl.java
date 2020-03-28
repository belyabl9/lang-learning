package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.Word;
import com.belyabl9.langlearning.exception.EntityExistsException;
import com.belyabl9.langlearning.service.WordParserService;
import com.belyabl9.langlearning.service.WordImporterService;
import com.belyabl9.langlearning.service.WordService;
import com.belyabl9.langlearning.service.impl.importer.ImporterSettings;
import com.belyabl9.langlearning.service.impl.importer.ImporterStatus;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class WordImporterServiceImpl implements WordImporterService {
    
    private final WordParserService wordParserService;

    private final WordService wordService;

    @Autowired
    public WordImporterServiceImpl(WordParserService wordParserService,
                                   WordService wordService) {
        this.wordParserService = wordParserService;
        this.wordService = wordService;
    }

    @Override
    public ImporterStatus importCategoryWords(@NonNull Category category,
                                              @NonNull InputStream inputStream) {
        List<Word> words = wordParserService.parse(inputStream);
        words.forEach(word -> word.setCategory(category));

        List<Long> importedIds = new ArrayList<>();
        for (Word word : words) {
            word = wordService.insert(word);
            importedIds.add(word.getId());
        }
        return new ImporterStatus("Imported " + importedIds.size() + " items.");
    }

    @Override
    public ImporterStatus importCategoryWords(@NonNull Category category,
                                              @NonNull InputStream inputStream,
                                              @NonNull ImporterSettings importerSettings) {
        List<Word> words = wordParserService.parse(inputStream);
        words.forEach(word -> word.setCategory(category));
        
        List<Long> importedIds = new ArrayList<>();
        for (Word word : words) {
            try {
                word = wordService.insert(word);
                importedIds.add(word.getId());
            } catch (EntityExistsException ex) {
                switch (importerSettings.getActionOnDuplicate()) {
                    case REPLACE:
                        Word foundWord = wordService.findByOriginal(category, word.getOriginal());
                        wordService.remove(foundWord);
                        wordService.insert(word);
                        break;
                    case SKIP:
                        break;
                    case ERROR_ROLLBACK:
                        for (Long importedId : importedIds) {
                            wordService.remove(importedId);
                        }
                        return new ImporterStatus("Duplicate items are not allowed. Imported items have been rolled back.");
                    case ERROR:
                        return new ImporterStatus("Duplicate items are not allowed. Imported " + importedIds.size() + " items.");
                }
            }
        }
        return new ImporterStatus("Imported " + importedIds.size() + " items.");
    }
}

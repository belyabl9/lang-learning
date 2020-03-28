package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.service.impl.importer.ImporterSettings;
import com.belyabl9.langlearning.service.impl.importer.ImporterStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.InputStream;

@Service
public interface WordImporterService {
    
    /**
     * Imports words to a category from an input stream.
     * Allows to configure importer, e.g. specifying a strategy for dealing with duplicates
     * @return a status of a performed import 
     */
    @NotNull
    ImporterStatus importCategoryWords(@NotNull Category category, @NotNull InputStream inputStream, @NotNull ImporterSettings importerSettings);

    /**
     * Imports words to a category from an input stream.
     * @return a status of a performed import 
     */
    @NotNull
    ImporterStatus importCategoryWords(@NotNull Category category, @NotNull InputStream inputStream);
}

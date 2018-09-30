package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.Word;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Service
public interface WordService {

    /**
     * Returns a word with a specified id or null
     * @param id id of a word
     * @return a word with a specified id or null
     */
    @Nullable
    Word findById(long id);

    @Nullable
    Word findByOriginal(@NotNull Category category, @NotNull String original);

    /**
     * Returns a list of words which belong to a specified category
     * @param category a category object
     * @return a list of words which belong to a specified category
     */
    @NotNull
    List<Word> findAllByCategory(@NotNull Category category);

    /**
     * Checks whether the word already exists in the category 
     * @param original word
     * @param category category
     * @return true if the word already exists in the category, otherwise false
     */
    boolean exists(@NotNull String original, @NotNull Category category);

    /**
     * Inserts a word by the specified object and returns a created object
     * @param word a word object to be inserted into DB
     * @return inserted Word object
     */
    @NotNull
    Word insert(@NotNull Word word);

    /**
     * Updates the word by the specified object and returns an updated object
     * @param word a word object to be updated
     * @return updated Word object
     */
    @NotNull
    Word update(@NotNull Word word);

    /**
     * Removes a word by the specified id
     * @param id id of a word
     */
    void remove(long id);

    /**
     * Removes a word by the specified Word object (it must have id parameter set)
     * @param word Word object with id parameter
     */
    void remove(@NotNull Word word);

    /**
     * Increases a priority for words which were not answered correctly and decreases for correctly answered ones.
     * @param correctIds a set of word ids for correctly answered words 
     * @param wrongIds a set of word ids for words which were not answered correctly 
     */
    void updatePriority(@NotNull Set<Long> correctIds, @NotNull Set<Long> wrongIds);
}

package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.domain.Word;
import com.belyabl9.langlearning.exception.LangNotSelectedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.belyabl9.langlearning.exception.EntityExistsException;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public interface CategoryService {

    @Nullable
    Category findById(long id);

    /**
     * Checks whether a category with the specified name exists for a specified user or not.
     * If user is not specified, built-in categories are searched.
     * 
     * @param name category name
     * @param user User object or null if this is a check for a built-in category
     * @return true if a category exists
     */
    boolean exists(@NotNull String name, @Nullable User user);

    /**
     * Returns categories created by Administrator and shared through all users
     */
    @NotNull
    List<Category> findBuiltInCategories();

    /**
     * Returns categories created by the specified user
     * @throws LangNotSelectedException
     */
    @NotNull
    List<Category> findUserCategories(@NotNull User user);

    /**
     * @throws EntityExistsException
     * @throws LangNotSelectedException
     */
    @NotNull
    Category insert(@NotNull Category category);

    /**
     * @throws EntityExistsException
     * @throws LangNotSelectedException
     */
    @NotNull
    Category update(@NotNull Category category);
    
    void remove(long id);

    void remove(@NotNull Category category);

    /**
     * Copies a category to the specified user
     * 
     * @throws EntityExistsException if there is a category with the same name
     */
    @Transactional
    @NotNull
    Category copy(@NotNull Category category, @NotNull User user);

    /**
     * Copies a category with the specified name to the specified user
     *
     * @throws EntityExistsException if there is a category with the same name
     */
    @Transactional
    @NotNull
    Category copy(@NotNull Category category, @NotNull User user, @NotNull String categoryName);

    /**
     * Moves specific words from one category to another.
     *
     * @param words a list of words which must belong to the same category
     * @param category a category where words will be moved to
     * 
     * @throws EntityExistsException if the same word already exists
     */
    @Transactional
    void moveWords(@NotNull List<Word> words, @NotNull Category category);

    /**
     * Copies specific words from one category to another.
     *
     * @param words a list of words which must belong to the same category
     * @param category a category where words will be moved to
     *
     * @throws EntityExistsException if the same word already exists
     */
    @Transactional
    void copyWords(@NotNull List<Word> words, @NotNull Category category);

    /**
     * Makes a category available for sharing via a link
     * @return link to the shared category
     */
    @NotNull
    String share(@NotNull Category category);

    /**
     * Stop sharing a category via a link
     */
    void unshare(@NotNull Category category);

    /**
     * Finds a category by a shared reference
     * @param reference link to a shared category
     * @return category or null
     */
    @Nullable
    Category findByReference(@NotNull String reference);

}

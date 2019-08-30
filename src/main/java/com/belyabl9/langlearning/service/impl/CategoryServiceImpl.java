package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.domain.Word;
import com.belyabl9.langlearning.exception.EntityExistsException;
import com.belyabl9.langlearning.exception.LangNotSelectedException;
import com.belyabl9.langlearning.repository.CategoryRepository;
import com.belyabl9.langlearning.repository.UserRepository;
import com.belyabl9.langlearning.service.CategoryService;
import com.belyabl9.langlearning.service.WordService;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final WordService wordService;
    
    @Autowired
    public CategoryServiceImpl(UserRepository userRepository,
                               CategoryRepository categoryRepository,
                               WordService wordService) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.wordService = wordService;
    }

    @Override
    public Category findById(long id) {
        return categoryRepository.findOne(id);
    }

    @Override
    public boolean exists(@NonNull String name, @Nullable User user) {
        Category category = categoryRepository.findByNameAndUser(name, user);
        return category != null;
    }

    @Override
    public List<Category> findBuiltInCategories() {
        return categoryRepository.findAllByUserIsNull();
    }

    @Override
    public List<Category> findUserCategories(@NonNull User user) {
        if (user.getLearningLang() == null) {
            throw new LangNotSelectedException("The user has not selected a language to learn.");
        }
        return categoryRepository.findAllByUserAndLang(user, user.getLearningLang());
    }

    @Override
    public Category insert(@NonNull Category category) {
        if (exists(category.getName(), category.getUser())) {
            throw new EntityExistsException();
        }
        if (category.getLang() == null) {
            throw new LangNotSelectedException("The user has not selected a language to learn.");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category update(@NonNull Category category) {
        if (exists(category.getName(), category.getUser())) {
            throw new EntityExistsException();
        }
        if (category.getLang() == null) {
            throw new LangNotSelectedException("The user has not selected a language to learn.");
        }
        return categoryRepository.save(category);
    }

    @Override
    public void remove(long id) {
        remove(findById(id));
    }

    @Override
    public void remove(@NonNull Category category) {
        category.getUser().getCategories().remove(category);
        userRepository.saveAndFlush(category.getUser());
    }

    @Override
    public String share(@NonNull Category category) {
        if (category.getSharedReference() != null) {
            return category.getSharedReference();
        }
        String reference = ReferenceUtils.encode(category);
        category.setSharedReference(reference);
        categoryRepository.save(category);
        return reference;
    }

    @Override
    public void unshare(@NonNull Category category) {
        category.setSharedReference(null);
        categoryRepository.save(category);
    }

    @Override
    public Category findByReference(@NonNull String reference) {
        return categoryRepository.findBySharedReference(reference);
    }

    @Transactional
    @Override
    public Category copy(@NonNull Category category, @NonNull User user) {
        return copy(category, user, category.getName());
    }

    @Transactional
    @Override
    public Category copy(@NonNull Category category, @NonNull User user, @NonNull String categoryName) {
        if (exists(categoryName, user)) {
            throw new EntityExistsException();
        }
        Category clonedCategory = new Category(categoryName, new ArrayList<>(), user);
        for (Word word : category.getWords()) {
            clonedCategory.getWords().add(copyWord(word, clonedCategory));
        }
        categoryRepository.save(clonedCategory);
        return clonedCategory;
    }

    @Transactional
    @Override
    public void moveWords(@NonNull List<Word> words, @NonNull Category category) {
        for (Word word : ImmutableList.copyOf(words)) {
            if (word.getCategory().getId().equals(category.getId())) {
                throw new IllegalArgumentException("Can not move words which are already related to this category.");
            }
            category.getWords().add(copyWord(word, category));
            wordService.remove(word.getId());
        }
    }

    @Transactional
    @Override
    public void copyWords(@NonNull List<Word> words, @NonNull Category category) {
        for (Word word : words) {
            if (word.getCategory().getId().equals(category.getId())) {
                throw new IllegalArgumentException("Can not copy words which are already related to this category.");
            }
            category.getWords().add(copyWord(word, category));
        }
        categoryRepository.save(category);
    }

    private Word copyWord(@NonNull Word word, @NonNull Category category) {
        return new Word(word.getOriginal(),
                        word.getTranslation(),
                        category,
                        0,
                        new HashSet<>(word.getSynonyms()),
                        new ArrayList<>(word.getSentenceExamples())
        );
    }

}

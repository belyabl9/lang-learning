package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.Word;
import com.belyabl9.langlearning.exception.EntityExistsException;
import com.belyabl9.langlearning.repository.CategoryRepository;
import com.belyabl9.langlearning.repository.WordRepository;
import com.belyabl9.langlearning.service.SynonymService;
import com.belyabl9.langlearning.service.WordService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class WordServiceImpl implements WordService {
    private final CategoryRepository categoryRepository;
    private final WordRepository wordRepository;
    private final SynonymService synonymService;

    @Autowired
    public WordServiceImpl(CategoryRepository categoryRepository,
                           WordRepository wordRepository,
                           SynonymService synonymService) {
        this.categoryRepository = categoryRepository;
        this.wordRepository = wordRepository;
        this.synonymService = synonymService;
    }

    @Override
    public Word findById(long id) {
        return wordRepository.findOne(id);
    }

    @Override
    public Word findByOriginal(@NonNull Category category, @NonNull String original) {
        return wordRepository.findByOriginalAndCategory(original, category);
    }

    @Override
    public List<Word> findAllByCategory(@NonNull Category category) {
        return wordRepository.findAllByCategoryOrderByPriorityDesc(category);
    }

    @Override
    public boolean exists(@NonNull String original, @NonNull Category category) {
        Word word = wordRepository.findByOriginalAndCategory(original, category);
        return word != null;
    }

    @Override
    public Word insert(@NonNull Word word) {
        if (exists(word.getOriginal(), word.getCategory())) {
            throw new EntityExistsException();
        }

        Set<String> synonyms = synonymService.findAll(word.getOriginal());
        if (!synonyms.isEmpty()) {
            word.setSynonyms(synonyms);
        }
        word = wordRepository.saveAndFlush(word);
        word.getCategory().addWord(word);
        return word;
    }

    @Override
    public Word update(@NonNull Word word) {
        return wordRepository.saveAndFlush(word);
    }

    @Override
    public void remove(long id) {
        remove(findById(id));
    }

    @Override
    public void remove(@NonNull Word word) {
        word.getCategory().getWords().remove(word);
        categoryRepository.saveAndFlush(word.getCategory());
    }

    @Transactional
    @Override
    public void updatePriority(@NonNull Set<Long> correctWordIds, @NonNull Set<Long> wrongWordIds) {
        if (!wrongWordIds.isEmpty()) {
            wordRepository.incrementPriority(wrongWordIds);
        }
        if (!correctWordIds.isEmpty()) {
            wordRepository.decrementPriority(correctWordIds);
        }
    }

}

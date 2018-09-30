package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.repository.CategoryRepository;
import com.belyabl9.langlearning.repository.WordRepository;
import com.belyabl9.langlearning.service.impl.WordServiceImpl;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class WordServiceTest {

    private WordService wordService;

    @Mock
    private CategoryRepository categoryRepository;
    
    @Mock
    private WordRepository wordRepository;

    @Mock
    private SynonymService synonymService;

    @Before
    public void setUp() {
        initMocks(this);
        wordService = new WordServiceImpl(categoryRepository, wordRepository, synonymService);
    }
    
    @Test
    public void findAllByCategory() throws Exception {
    }

    @Test
    public void insert() throws Exception {
    }

    @Test
    public void remove() throws Exception {
    }

    @Test
    public void updatePriority() throws Exception {
        Set<Long> incrementIds = ImmutableSet.of(1l);
        Set<Long> decrementIds = ImmutableSet.of(2l);
        doNothing().when(wordRepository).incrementPriority(incrementIds);
        doNothing().when(wordRepository).decrementPriority(decrementIds);

        wordService.updatePriority(decrementIds, incrementIds);

        verify(wordRepository, times(1)).incrementPriority(incrementIds);
        verify(wordRepository, times(1)).decrementPriority(decrementIds);
        verifyNoMoreInteractions(wordRepository);
    }

}
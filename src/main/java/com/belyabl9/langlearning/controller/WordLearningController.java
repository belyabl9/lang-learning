package com.belyabl9.langlearning.controller;

import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.TranslationQuiz;
import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.domain.Word;
import com.belyabl9.langlearning.domain.WordCard;
import com.belyabl9.langlearning.domain.WordLearningSetup;
import com.belyabl9.langlearning.service.AuthService;
import com.belyabl9.langlearning.service.CategoryService;
import com.belyabl9.langlearning.service.WordLearningService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
class WordLearningController {
    private static final Logger LOG = LoggerFactory.getLogger(WordLearningController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WordLearningService wordLearningService;
    
    @Autowired
    private AuthService authService;
    
    @RequestMapping(value="/learning/quiz/setup", method=GET)
    public String learningQuizSetup(Model model, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        List<Category> categories = categoryService.findUserCategories(user);
        categories = categories.stream()
                .filter(category -> !wordLearningService.collectQuestions(category.getId()).isEmpty())
                .collect(Collectors.toList());

        model.addAttribute("categories", categories);
        model.addAttribute("wordLearningSetup", new WordLearningSetup());
        return "learningQuizSetup";
    }

    @RequestMapping(value="/learning/cards/setup", method=GET)
    public String learningCardsSetup(Model model, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        List<Category> categories = categoryService.findUserCategories(user);

        model.addAttribute("categories", categories);
        model.addAttribute("wordLearningSetup", new WordLearningSetup());
        return "learningCardsSetup";
    }

    @RequestMapping(value="/learning/quiz/start", method=POST)
    public String learningQuizStart(@RequestParam("categoryIds[]") Set<Long> categoryIds,
                                    Model model,
                                    Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);

        for (Long categoryId : categoryIds) {
            Category category = categoryService.findById(categoryId);
            if (category == null) {
                throw new RuntimeException("A category with the specified id does not exist.");
            }
            if (category.isBuiltIn()) {
                throw new RuntimeException("Only user categories can be used.");
            }
            if (category.getUser().getId() != user.getId()) {
                throw new RuntimeException("A category with the specified id does not belong to a current user.");
            }
        }

        List<TranslationQuiz> words = wordLearningService.collectQuestions(categoryIds);
        if (words.isEmpty()) {
            LOG.error("Can not find words for learning.");
            throw new RuntimeException("Can not find words for learning.");
        }

        String questionsJson;
        try {
            questionsJson = new ObjectMapper().writeValueAsString(words);
        } catch (JsonProcessingException e) {
            LOG.error("Can not serialize questions to JSON.", e);
            throw new RuntimeException(e);
        }
        model.addAttribute("questionsJson", questionsJson);

        return "learningQuiz";
    }

    @RequestMapping(value="/learning/cards/start", method=POST)
    public String learningCardsStart(@RequestParam("categoryIds[]") Set<Long> categoryIds,
                                    Model model,
                                    Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);

        List<Word> words = new ArrayList<>();
        for (Long categoryId : categoryIds) {
            Category category = categoryService.findById(categoryId);
            if (category == null) {
                throw new RuntimeException("A category with the specified id does not exist.");
            }
            if (category.isBuiltIn()) {
                throw new RuntimeException("Only user categories can be used.");
            }
            if (category.getUser().getId() != user.getId()) {
                throw new RuntimeException("A category with the specified id does not belong to a current user.");
            }
            words.addAll(category.getWords());
        }
        
        List<WordCard> wordCards = words.stream().map(word -> new WordCard(
                word.getId(),
                word.getOriginal(),
                word.getTranslation(),
                word.getCategory().getName(),
                word.getAssociationImg() != null ? word.getAssociationImg().getUrl() : null,
                word.getSentenceExamples()
        )).collect(Collectors.toList());

        String wordsJson = null;
        try {
            wordsJson = new ObjectMapper().writeValueAsString(wordCards);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
        model.addAttribute("words", wordsJson);

        return "learningCards";
    }
    
}

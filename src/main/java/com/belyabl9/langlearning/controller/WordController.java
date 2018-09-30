package com.belyabl9.langlearning.controller;

import com.belyabl9.langlearning.ajax.*;
import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.Image;
import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.domain.Word;
import com.belyabl9.langlearning.dto.WordDto;
import com.belyabl9.langlearning.exception.EntityExistsException;
import com.belyabl9.langlearning.service.AuthService;
import com.belyabl9.langlearning.service.CategoryService;
import com.belyabl9.langlearning.service.ImageService;
import com.belyabl9.langlearning.service.WordService;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
class WordController {
    private static final Logger LOG = LoggerFactory.getLogger(WordController.class);
    
    @Autowired
    private WordService wordService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ImageService imageService;

    @Autowired
    private AuthService authService;
    
    @RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.GET)
    public String showCategoryWords(@PathVariable long categoryId,
                                    Model model,
                                    Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        Category category = categoryService.findById(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("Category with a specified id does not exist >>> " + categoryId);
        }

        if (!user.isAdmin() && !category.isBuiltIn() && category.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("A category with a specified id does not belong to the current user >>> " + categoryId);
        }

        Collection<Category> categories = new ArrayList<>(user.getCategories());
        categories = categories.stream().filter(c -> c.getId() != categoryId).collect(Collectors.toList());
        if (user.isAdmin()) {
            categories.addAll(categoryService.findBuiltInCategories());
        }

        model.addAttribute("category", category);
        model.addAttribute("user", user);
        model.addAttribute("categories", categories);
        return "category";
    }

    @RequestMapping(value = "/categories/ref/{reference}", method = RequestMethod.GET)
    public String showCategoryWordsByRef(@PathVariable String reference,
                                    Model model,
                                    Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        Category category = categoryService.findByReference(reference);
        if (category == null) {
            throw new IllegalArgumentException("Category with a specified shared reference does not exist >>> " + reference);
        }

        model.addAttribute("category", category);
        model.addAttribute("user", user);
        model.addAttribute("shared", true);
        return "category";
    }

    @ResponseBody
    @RequestMapping(value = "/word/find")
    public AjaxFindWordResponse findWord(@RequestBody AjaxFindWordRequest findWordRequest, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        Word word = wordService.findById(findWordRequest.getId());
        if (word == null) {
            return new AjaxFindWordResponse(false, "A word with a specified id does not exist >>> " + findWordRequest.getId());
        }
        if (!user.isAdmin() && word.getCategory().getUser().getId() != user.getId()) {
            return new AjaxFindWordResponse(false, "The word must belong to the current user.");
        }
        return new AjaxFindWordResponse(
                new WordDto(
                        word.getId(),
                        word.getOriginal(),
                        word.getTranslation(),
                        word.getSentenceExamples(),
                        word.getAssociationImg() != null ? word.getAssociationImg().getUrl() : null
                )
        );
    }

    @ResponseBody
    @RequestMapping(value = "/word/add", method = RequestMethod.POST)
    public AjaxResponseBody addNewWord(@RequestBody AjaxAddWordRequest addWordRequest,
                                       Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        Category category = categoryService.findById(addWordRequest.getCategoryId());
        if (!user.isAdmin() && category.getUser().getId() != user.getId()) {
            return AjaxResponseBody.failure("The category of the word must belong to the the current user.");
        }
        Word word = new Word(addWordRequest.getOriginal(),
                             addWordRequest.getTranslation(),
                             category,
                     0,
                             Collections.emptySet(),
                             addWordRequest.getUsageExamples()
        );
        
        try {
            word = wordService.insert(word);
            String associationImgBase64 = addWordRequest.getAssociationImgBase64();
            if (!Strings.isNullOrEmpty(associationImgBase64)) {
                Image image = imageService.insert(new Image(Base64.getDecoder().decode(associationImgBase64), word));
                word.setAssociationImg(image);
            }
        } catch (EntityExistsException e) {
            return AjaxResponseBody.failure("The word already exists.");
        } catch (Exception e) {
            LOG.error("The word can not be added.", e);
            return AjaxResponseBody.failure("The word can not be added.");
        }
        return AjaxResponseBody.SUCCESS;
    }

    // TODO looks like too much logic for controller. move to service ?
    @ResponseBody
    @RequestMapping(value = "/word/update", method = RequestMethod.POST)
    public AjaxResponseBody updateWord(@RequestBody AjaxUpdateWordRequest updateWordRequest,
                                       Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        Word word = wordService.findById(updateWordRequest.getId());
        if (word == null) {
            return AjaxResponseBody.failure("The word with the specified id does not exist >> " + updateWordRequest.getId());
        }

        if (!user.isAdmin() && word.getCategory().getUser().getId() != user.getId()) {
            return AjaxResponseBody.failure("The word must belong to the the category of the current user.");
        }

        boolean wordOriginalChanged = !word.getOriginal().equals(updateWordRequest.getOriginal());
        boolean wordTranslationChanged = !word.getTranslation().equals(updateWordRequest.getTranslation());

        Image prevAssociationImg = word.getAssociationImg();
        if (prevAssociationImg != null) {
            imageService.remove(prevAssociationImg);
        }

        String associationImgBase64 = updateWordRequest.getAssociationImgBase64();
        if (!Strings.isNullOrEmpty(associationImgBase64)) {
            word.setAssociationImg(
                    imageService.insert(
                            new Image(Base64.getDecoder().decode(associationImgBase64), word)
                    )
            );
        } else {
            word.setAssociationImg(null);
        }

        
        word.setOriginal(updateWordRequest.getOriginal());
        word.setTranslation(updateWordRequest.getTranslation());
        word.setSentenceExamples(updateWordRequest.getUsageExamples());
        
        if (wordOriginalChanged) {
            if (wordService.exists(word.getOriginal(), word.getCategory())) {
                return AjaxResponseBody.failure("The word already exists.");
            }
        }
        
        // reset priority if the word or translation have been changed
        if (wordOriginalChanged || wordTranslationChanged) {
            word.setPriority(0);
        }

        try {
            wordService.update(word);
        } catch (Exception e) {
            LOG.error("The word can not be updated.", e);
            return AjaxResponseBody.failure("The word can not be updated.");
        }
        return AjaxResponseBody.SUCCESS;
    }
    
    @ResponseBody
    @RequestMapping(value = "/word/delete")
    public AjaxResponseBody delWord(@RequestBody AjaxDelWordRequest delWordRequest,
                                    Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        Word word = wordService.findById(delWordRequest.getId());
        if (word == null) {
            return AjaxResponseBody.failure("A word with a specified id does not exist >>> " + delWordRequest.getId());
        }
        if (!user.isAdmin() && word.getCategory().getUser().getId() != user.getId()) {
            return AjaxResponseBody.failure("Can not delete a word that does not belong to the current user.");
        }
        try {
            wordService.remove(delWordRequest.getId());
        } catch (Exception e) {
            LOG.error("The word can not be deleted.", e);
            return AjaxResponseBody.failure("The word can not be deleted.");
        }
        return AjaxResponseBody.success();
    }

    @ResponseBody
    @RequestMapping(value = "/word/addSentenceExample")
    public AjaxResponseBody addSentenceExample(@RequestBody AjaxAddSentenceExampleRequest request, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        Word word = wordService.findById(request.getWordId());
        if (word == null) {
            return AjaxResponseBody.failure("The word with a specified id does not exist >>" + request.getWordId());
        }
        if (!user.isAdmin() && word.getCategory().getUser().getId() != user.getId()) {
            return AjaxResponseBody.failure("The word must belong to the the category of the current user.");
        }
        List<String> sentenceExamples = new ArrayList<>(word.getSentenceExamples());
        sentenceExamples.add(request.getSentence());
        word.setSentenceExamples(sentenceExamples);
        try {
            wordService.update(word);
        } catch (Exception e) {
            LOG.error("Can not add a sentence example for a word.", e);
            return AjaxResponseBody.failure("Can not add a sentence example for a word.");
        }
        return AjaxResponseBody.success();
    }

    @ResponseBody
    @RequestMapping(value = "/word/usageExamples")
    public AjaxResponseBody getWordUsageExamples(@RequestBody AjaxGetUsageExamplesRequest request, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        Word word = wordService.findById(request.getWordId());
        if (word == null) {
            return AjaxResponseBody.failure("The word with a specified id does not exist >>" + request.getWordId());
        }
        if (!user.isAdmin() && word.getCategory().getUser().getId() != user.getId()) {
            return AjaxResponseBody.failure("The word must belong to the the category of the current user.");
        }
        return AjaxUsageExamplesResponse.create(word.getSentenceExamples());
    }

    @ResponseBody
    @RequestMapping(value = "/word/updatePriority")
    public AjaxResponseBody updatePriority(@RequestBody AjaxUpdateWordsPriorityRequest request, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        if (!user.isAdmin()) {
            boolean idsValidated = Stream.concat(request.getCorrectIds().stream(), request.getWrongIds().stream())
                    .noneMatch(id -> {
                        Word word = wordService.findById(id);
                        return word.getCategory().getUser().getId() != user.getId();
                    });
            if (!idsValidated) {
                return AjaxResponseBody.failure("All specified words must belong to categories of the current user.");
            }
        }
        try {
            wordService.updatePriority(request.getCorrectIds(), request.getWrongIds());
        } catch (Exception e) {
            LOG.error("Can not update a priority of words.", e);
            return AjaxResponseBody.failure("Can not update a priority of words.");
        }
        return AjaxResponseBody.success();
    }

    @ResponseBody
    @RequestMapping(value = "/words/copy", method = RequestMethod.POST)
    public AjaxResponseBody copyWords(@RequestBody AjaxCopyWordsRequest copyWordsRequest, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);

        List<Long> wordsIds = copyWordsRequest.getWordsIds();
        List<Word> words = new ArrayList<>();
        for (Long wordId : wordsIds) {
            Word word = wordService.findById(wordId);
            if (!user.isAdmin() && word.getCategory().isBuiltIn()) {
                return AjaxResponseBody.failure("Only administrator can copy words from built-in suggested categories.");
            }
            if (!user.isAdmin() && !word.getCategory().isBuiltIn() && word.getCategory().getUser().getId() != user.getId()) {
                return AjaxResponseBody.failure("The words must belong to a category of the current user.");
            }
            words.add(word);
        }
        Category category = categoryService.findById(copyWordsRequest.getCategoryId());
        if (category.isBuiltIn() && !user.isAdmin()) {
            return AjaxResponseBody.failure("Only administrator can copy words into built-in suggested categories.");
        }
        if (!user.isAdmin() && !category.isBuiltIn() && category.getUser().getId() != user.getId()) {
            return AjaxResponseBody.failure("The word's category must belong to the current user.");
        }

        try {
            categoryService.copyWords(words, category);
        } catch (Exception e) {
            LOG.error("Can not copy the words.", e);
            return AjaxResponseBody.failure("Can not copy the words.");
        }
        return AjaxResponseBody.success();
    }

    @ResponseBody
    @RequestMapping(value = "/words/move", method = RequestMethod.POST)
    public AjaxResponseBody moveWords(@RequestBody AjaxCopyWordsRequest copyWordsRequest, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);

        List<Long> wordsIds = copyWordsRequest.getWordsIds();
        List<Word> words = new ArrayList<>();
        for (Long wordId : wordsIds) {
            Word word = wordService.findById(wordId);
            if (!user.isAdmin() && word.getCategory().isBuiltIn()) {
                return AjaxResponseBody.failure("Only administrator can move words from built-in suggested categories.");
            }
            if (!user.isAdmin() && !word.getCategory().isBuiltIn() && word.getCategory().getUser().getId() != user.getId()) {
                return AjaxResponseBody.failure("The words must belong to a category of the current user.");
            }
            words.add(word);
        }
        Category category = categoryService.findById(copyWordsRequest.getCategoryId());
        if (category.isBuiltIn() && !user.isAdmin()) {
            return AjaxResponseBody.failure("Only administrator can move words into built-in suggested categories.");
        }
        if (!user.isAdmin() && !category.isBuiltIn() && category.getUser().getId() != user.getId()) {
            return AjaxResponseBody.failure("The word's category must belong to the current user.");
        }
        if (!user.isAdmin() && category.getUser().getId() != user.getId()) {
            return AjaxResponseBody.failure("The word's category must belong to the current user.");
        }

        try {
            categoryService.moveWords(words, category);
        } catch (Exception e) {
            LOG.error("Can not move the words.", e);
            return AjaxResponseBody.failure("Can not move the words.");
        }
        return AjaxResponseBody.SUCCESS;
    }

}
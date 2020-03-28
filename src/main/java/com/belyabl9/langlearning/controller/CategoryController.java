package com.belyabl9.langlearning.controller;

import com.belyabl9.langlearning.ajax.*;
import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.dto.CategoryDto;
import com.belyabl9.langlearning.exception.EntityExistsException;
import com.belyabl9.langlearning.exception.LangNotSelectedException;
import com.belyabl9.langlearning.service.*;
import com.belyabl9.langlearning.service.impl.importer.ActionOnDuplicate;
import com.belyabl9.langlearning.service.impl.importer.ImporterSettings;
import com.belyabl9.langlearning.service.impl.importer.ImporterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
class CategoryController {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WordImporterService wordImporterService;
    
    @Autowired
    private AuthService authService;
    
    @ResponseBody
    @RequestMapping(value = "/category/find")
    public AjaxFindCategoryResponse findCategory(@RequestBody AjaxFindCategoryByIdRequest request, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        Category category = categoryService.findById(request.getId());
        if (category == null) {
            return new AjaxFindCategoryResponse(false, "Can not find a category with the specified id >> " + request.getId());
        }
        if (!user.isAdmin() && !category.isBuiltIn() && category.getUser().getId() != user.getId()) {
            return new AjaxFindCategoryResponse(false, "The category must belong to the current user.");
        }
        return new AjaxFindCategoryResponse(new CategoryDto(category.getId(), category.getName()));
    }
    
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public String findAll(Model model, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        List<Category> categories = categoryService.findUserCategories(user);
        List<Category> builtInCategories = categoryService.findBuiltInCategories();
        model.addAttribute("user", user);
        model.addAttribute("userCategories", categories);
        model.addAttribute("builtInCategories", builtInCategories);
        return "categories";
    }

    @ResponseBody
    @RequestMapping(value = "/category/add", method = RequestMethod.POST)
    public AjaxResponseBody addCategory(@RequestBody AjaxAddCategoryRequest addCategoryRequest, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        try {
            categoryService.insert(
                    new Category(addCategoryRequest.getCategoryName(),
                                 Collections.emptyList(),
                                 user.getLearningLang(),
                                 addCategoryRequest.getUserId() != null ? user : null
                    )
            );
        } catch (EntityExistsException e) {
            return AjaxResponseBody.failure("The category with the specified name already exists.");
        } catch (LangNotSelectedException e) {
            LOG.error("Can not add a category. The user has not selected a language to learn.", e);
            return AjaxResponseBody.failure("Can not add a category. The user has not selected a language to learn.");
        } catch (Exception e) {
            LOG.error("Can not add a category. Please try again", e);
            return AjaxResponseBody.failure("Can not add a category. Please try again");
        }
        return AjaxResponseBody.success();
    }

    @ResponseBody
    @RequestMapping(value = "/category/update", method = RequestMethod.POST)
    public AjaxResponseBody updateCategory(@RequestBody AjaxUpdateCategoryRequest updateCategoryRequest, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        Category category = categoryService.findById(updateCategoryRequest.getId());
        if (!user.isAdmin() && category.isBuiltIn()) {
            return AjaxResponseBody.failure("Only administrator is allowed to update built-in suggested categories.");
        }
        if (!user.isAdmin() && !category.isBuiltIn() && category.getUser().getId() != user.getId()) {
            return AjaxResponseBody.failure("The category being updated must belong to the current user.");
        }
        category.setName(updateCategoryRequest.getCategoryName());

        try {
            categoryService.update(category);
        } catch (EntityExistsException e) {
            return AjaxResponseBody.failure("The category with the specified name already exists.");
        } catch (Exception e) {
            LOG.error("Can not update the category. Please try again", e);
            return AjaxResponseBody.failure("Can not update the category. Please try again");
        }
        return AjaxResponseBody.success();
    }

    @ResponseBody
    @RequestMapping(value = "/category/clone", method = RequestMethod.POST)
    public AjaxResponseBody cloneCategory(@RequestBody AjaxCloneCategoryRequest cloneCategoryRequest, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        Category category = categoryService.findById(cloneCategoryRequest.getId());
        if (!user.isAdmin() && !category.isBuiltIn() && !category.isShared() && category.getUser().getId() != user.getId()) {
            return AjaxResponseBody.failure("The category being cloned is not available for the current user.");
        }
        try {
            categoryService.copy(category, user, cloneCategoryRequest.getCategoryName());
        } catch (Exception e) {
            LOG.error("Can not clone the category.", e);
            return AjaxResponseBody.failure("Can not clone the category.");
        }
        return AjaxResponseBody.SUCCESS;
    }

    @ResponseBody
    @RequestMapping(value = "/category/delete")
    public AjaxResponseBody delCategory(@RequestBody AjaxDelCategoryRequest delCategoryRequest, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        Category category = categoryService.findById(delCategoryRequest.getId());
        if (!user.isAdmin() && category.isBuiltIn()) {
            return AjaxResponseBody.failure("Only administrator is allowed to delete built-in categories.");
        }
        if (!user.isAdmin() && !category.isBuiltIn() && category.getUser().getId() != user.getId()) {
            return AjaxResponseBody.failure("The category must belong to the current user.");
        }
        try {
            categoryService.remove(delCategoryRequest.getId());
        } catch (Exception e) {
            LOG.error("Can not delete the category.", e);
            return AjaxResponseBody.failure("Can not delete the category.");
        }
        return AjaxResponseBody.SUCCESS;
    }

    @ResponseBody
    @PostMapping("/categories/import")
    public AjaxResponseBody importWordsFromFile(@RequestParam("categoryName") String categoryName,
                                                @RequestParam("categoryWordsFile") MultipartFile categoryWordsFile,
                                                Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        
        Category category = categoryService.insert(
                new Category(categoryName, new ArrayList<>(), user.getLearningLang(), user)
        );
        try {
            ImporterStatus importerStatus = wordImporterService.importCategoryWords(category, categoryWordsFile.getInputStream());
            return new AjaxResponseBody(true, importerStatus.getMessage());
        } catch (IOException e) {
            LOG.error("Can not import a category from file.", e);
            throw new RuntimeException(e);
        }
    }
    
    @ResponseBody
    @PostMapping("/categories/{categoryId}/words/import")
    public AjaxResponseBody importWordsFromFile(@PathVariable long categoryId,
                                      @RequestParam("wordImportFile") MultipartFile file,
                                      @RequestParam("duplicateAction") ActionOnDuplicate duplicateAction,
                                      Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        Category category = categoryService.findById(categoryId);
        if (!user.isAdmin() && !category.isBuiltIn() && category.getUser().getId() != user.getId()) {
            throw new RuntimeException("The category must belong to the current user.");
        }
        try {
            ImporterStatus importerStatus = wordImporterService.importCategoryWords(category, file.getInputStream(), new ImporterSettings(duplicateAction));
            return new AjaxResponseBody(true, importerStatus.getMessage());
        } catch (IOException e) {
            LOG.error("Can not import words from file.", e);
            throw new RuntimeException(e);
        }
    }

    @ResponseBody
    @PostMapping("/categories/export")
    public byte[] exportCategory(@RequestParam("categoryId") Long categoryId, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);

        Category category = categoryService.findById(categoryId);
        if (category == null) {
            throw new RuntimeException("The category with the specified id is not available.");
        }
        if (!user.isAdmin() && !category.getUser().getId().equals(user.getId()) ) {
            throw new RuntimeException("The category must belong to the current user.");
        }

        try {
            File file = Files.createTempFile(null, null).toFile();
            String categoryContent = categoryService.export(category);
            Files.write(Paths.get(file.getPath()), categoryContent.getBytes());
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Could export a category.", e);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/categories/share", method = RequestMethod.POST)
    public AjaxReferenceResponse shareCategory(@RequestParam("categoryId") Long categoryId, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        try {
            Category category = categoryService.findById(categoryId);
            if (!category.getUser().getId().equals(user.getId())) {
                throw new IllegalAccessException("The category must belong to the current user.");
            }
            return new AjaxReferenceResponse(categoryService.share(category));
        } catch (Exception e) {
            LOG.error("Internal error: Can not share the category", e);
            return new AjaxReferenceResponse(false, "Internal error: Can not share the category.");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/categories/unshare", method = RequestMethod.POST)
    public AjaxResponseBody unshareCategory(@RequestParam("categoryId") Long categoryId, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        try {
            Category category = categoryService.findById(categoryId);
            if (!category.getUser().getId().equals(user.getId())) {
                throw new IllegalAccessException("The category must belong to the current user.");
            }
            categoryService.unshare(category);
            return AjaxResponseBody.success();
        } catch (Exception e) {
            LOG.error("Internal error: Can not unshare the category", e);
            return new AjaxResponseBody(false, "Internal error: Can not unshare the category.");
        }
    }
    
}
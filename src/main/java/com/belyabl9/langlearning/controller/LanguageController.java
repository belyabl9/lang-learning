package com.belyabl9.langlearning.controller;

import com.belyabl9.langlearning.domain.Language;
import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.service.AuthService;
import com.belyabl9.langlearning.service.LanguageService;
import com.belyabl9.langlearning.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
class LanguageController {
    private static final Logger LOG = LoggerFactory.getLogger(LanguageController.class);

    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private LanguageService languageService;
    
    @RequestMapping(value = "/language", method = RequestMethod.GET)
    public String showLanguages(Model model, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        
        model.addAttribute("languages", languageService.findLanguages());
        model.addAttribute("lang", user.getLearningLang() != null ? user.getLearningLang().getCode() : null);
        
        return "language";
    }

    @RequestMapping(value = "/language", method = RequestMethod.POST)
    public String selectLang(@RequestParam("language") String language, Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        user.setLearningLang(Language.fromCode(language));
        userService.update(user);
        return "/intro";
    }
}
package com.belyabl9.langlearning.controller;

import com.belyabl9.langlearning.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
class UserControllerAdvice {
    
    @Autowired
    private AuthService authService;
    
    @ModelAttribute
    public void addUserModel(Model model, Principal principal) {
        if (principal == null) {
            return;
        }
        model.addAttribute("user", authService.extractUserFromAuthInfo(principal, true));
    }
    
}

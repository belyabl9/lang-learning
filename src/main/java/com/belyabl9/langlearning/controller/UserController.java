package com.belyabl9.langlearning.controller;

import com.belyabl9.langlearning.ajax.AjaxResponseBody;
import com.belyabl9.langlearning.domain.ExternalUser;
import com.belyabl9.langlearning.domain.InternalUser;
import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.service.AuthService;
import com.belyabl9.langlearning.service.EmailService;
import com.belyabl9.langlearning.service.InternalUserService;
import com.belyabl9.langlearning.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@Slf4j
class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private InternalUserService internalUserService;

    @Autowired
    private AuthService authService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String addUser(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("login") String login,
            @RequestParam("password") String password,
            Model model
    ) {
        if (internalUserService.findByLogin(login) != null) {
            model.addAttribute("message", "A user with the specified login already exists.");
            return "registration";
        }
        User user = new InternalUser(name, email, login, passwordEncoder.encode(password), true);
        try {
            userService.insert(user);
            return "redirect:/login";
        } catch (Exception e) {
            log.error("Can not add a user: " + e.getMessage());
            model.addAttribute("message", "Can not create a user.");
            return "registration";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/user/password/change", method = RequestMethod.POST)
    public AjaxResponseBody changePassword(@RequestParam("new_password") String newPassword, 
                                           @RequestParam("confirm_password") String confirmPassword,
                                           Principal principal) {
        User user = authService.extractUserFromAuthInfo(principal);
        if (user instanceof ExternalUser) {
            throw new UnsupportedOperationException("It's not possible to change password for a user from an external system.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        try {
            userService.update(user);
            return AjaxResponseBody.SUCCESS;
        } catch (Exception e) {
            return new AjaxResponseBody(false, "Can not update the password.");
        }
    }
    
    @RequestMapping(value = "/user/{userId}/profile", method = RequestMethod.GET)
    public String showProfile(@PathVariable long userId,
                              Model model,
                              Principal principal) throws IllegalAccessException {
        User user = authService.extractUserFromAuthInfo(principal);
        if (!user.getId().equals(userId)) {
            throw new IllegalAccessException("Can not open a profile for another user.");
        }
        return "profile";
    }
    
}
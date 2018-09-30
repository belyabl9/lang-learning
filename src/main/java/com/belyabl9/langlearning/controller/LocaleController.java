package com.belyabl9.langlearning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class LocaleController {

    @RequestMapping(value="/locale/", method=GET)
    public String changeLocale() {
        return "intro";
    }
    
}

package com.belyabl9.langlearning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
class IntroController {

    @RequestMapping(value="/", method=GET)
    public String intro() {
        return "intro";
    }
    
}
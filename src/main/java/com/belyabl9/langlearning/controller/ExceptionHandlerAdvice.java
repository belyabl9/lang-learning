package com.belyabl9.langlearning.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    protected String handleException(Exception ex, Model model) {
        model.addAttribute("reason", ex.getMessage());
        return "error";
    }
}
package com.belyabl9.langlearning.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            for (HttpStatus httpStatus : HttpStatus.values()) {
                if (statusCode == httpStatus.value()) {
                    model.addAttribute("reason", httpStatus.getReasonPhrase());
                }
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
package com.belyabl9.langlearning.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    
    CustomAuthFailureHandler(String failureUrl) {
        super(failureUrl);
    }
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        saveException(request, exception);
        String errorCode;
        if (exception instanceof BadCredentialsException) {
            errorCode = "bad_credentials";
        } else {
            errorCode = "can_not_auth";
        }
        getRedirectStrategy().sendRedirect(request, response, "/login?error=" + errorCode);
    }
    
}

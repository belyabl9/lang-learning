package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.domain.User;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@Service
public interface AuthService {

    /**
     * Extracts user information. It can be an internal user or from external system like Google or Facebook
     */
    User extractUserFromAuthInfo(@NotNull Principal principal);

    /**
     * The same as {@link #extractUserFromAuthInfo}, but adds possibility of triggering creation of an internal user
     * to map it with a user from external system (Google/Facebook/etc.)
     * @param createInternalIfNotExists true if an internal user has to be created from extracted information
     */
    User extractUserFromAuthInfo(@NotNull Principal principal, boolean createInternalIfNotExists);
    
}

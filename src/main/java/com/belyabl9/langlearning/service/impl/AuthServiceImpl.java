package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.ExternalUser;
import com.belyabl9.langlearning.domain.SocialProvider;
import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.service.AuthService;
import com.belyabl9.langlearning.service.ExternalUserService;
import com.belyabl9.langlearning.service.InternalUserService;
import com.belyabl9.langlearning.service.UserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    private final static String GOOGLE_ID_FIELD_NAME = "sub";

    @Autowired
    private UserService userService;
    
    @Autowired
    private InternalUserService internalUserService;

    @Autowired
    private ExternalUserService externalUserService;

    @Override
    public User extractUserFromAuthInfo(@NonNull Principal principal) {
        return extractUserFromAuthInfo(principal, false);
    }

    @Override
    public User extractUserFromAuthInfo(@NonNull Principal principal, boolean createInternalIfNotExists) {
        if (principal instanceof OAuth2Authentication) {
            return extractExternalUser(principal, createInternalIfNotExists);
        }
        String username = principal.getName();
        return internalUserService.findByLogin(username);
    }

    private boolean isGoogle(@NonNull Map<String, String> details) {
        return details.containsKey(GOOGLE_ID_FIELD_NAME);
    }

    @SuppressWarnings("unchecked")
    private User extractExternalUser(@NonNull Principal principal, boolean createInternalIfNotExists) {
        OAuth2Authentication googleAuth = (OAuth2Authentication) principal;
        Map<String, String> details = (Map<String, String>) googleAuth.getUserAuthentication().getDetails();

        SocialProvider socialProvider;
        String extIdStr;
        if (isGoogle(details)) {
            socialProvider = SocialProvider.GOOGLE;
            extIdStr = details.get("sub");
        } else {
            socialProvider = SocialProvider.FACEBOOK;
            extIdStr = (String) googleAuth.getUserAuthentication().getPrincipal();
        }

        User externalUser = externalUserService.findByExternalIdAndProvider(extIdStr, socialProvider);
        if (createInternalIfNotExists && externalUser == null) {
            externalUser = userService.insert(new ExternalUser(extIdStr, socialProvider, details.get("name"), details.get("email")));
        }
        return externalUser;
    }

}

package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.domain.ExternalUser;
import com.belyabl9.langlearning.domain.SocialProvider;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Service
public interface ExternalUserService {

    @Nullable
    ExternalUser findByExternalIdAndProvider(@NotNull String externalId,
                                             @NotNull SocialProvider socialProvider);

    /**
     * Removes a user by id 
     * @param id user id
     */
    void remove(long id);
}

package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.domain.User;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public interface UserService {
    
    /**
     * Inserts a user into DB and returns inserted user object
     * @param user user object
     * @return inserted user object
     */
    @NotNull
    User insert(@NotNull User user);

    @NotNull
    User update(@NotNull User user);

}

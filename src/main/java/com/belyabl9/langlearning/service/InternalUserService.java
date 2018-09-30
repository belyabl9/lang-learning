package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.domain.User;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Service
public interface InternalUserService {

    /**
     * Returns a user by id
     * @param id user id
     * @return user object or null
     */
    @Nullable
    User findById(long id);

    /**
     * Returns a user by login
     * @param login user login
     * @return user object or null
     */
    @Nullable
    User findByLogin(@NotNull String login);

    /**
     * Removes a user by id 
     * @param id user id
     */
    void remove(long id);
}

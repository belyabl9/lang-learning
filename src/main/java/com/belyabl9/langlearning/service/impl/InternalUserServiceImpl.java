package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.repository.InternalUserRepository;
import com.belyabl9.langlearning.service.InternalUserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InternalUserServiceImpl implements InternalUserService {

    @Autowired
    private InternalUserRepository repository;


    @Override
    public User findById(long id) {
        return repository.findOne(id);
    }

    @Override
    public User findByLogin(@NonNull String login) {
        return repository.findByLogin(login);
    }

    @Override
    public void remove(long id) {
        repository.delete(id);
    }
}

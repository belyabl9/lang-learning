package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.InternalUser;
import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.domain.UserRole;
import com.belyabl9.langlearning.repository.UserRepository;
import com.belyabl9.langlearning.service.EmailService;
import com.belyabl9.langlearning.service.InternalUserService;
import com.belyabl9.langlearning.service.UserService;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private InternalUserService internalUserService;
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageSource messageSource;
    
    @PostConstruct
    private void init() {
        // temporary
        if (internalUserService.findByLogin("admin") == null) {
            addAdminUser();
        }
    }
    
    @Override
    public User insert(User user) {
        User createdUser = userRepository.saveAndFlush(user);
        try {
            emailService.sendSimpleMessage(
                    user.getEmail(),
                    messageSource.getMessage("user_created_mail_subj", null, LocaleContextHolder.getLocale()),
                    messageSource.getMessage("user_created_mail", new Object[]{ user.getName() }, LocaleContextHolder.getLocale())
            );
        } catch (Exception e) {
            log.error("Can not send an email.", e);
        }
        return createdUser;
    }

    @Override
    public User update(User user) {
        return userRepository.saveAndFlush(user);
    }
    
    // workaround for development, should be inserted manually to DB after startup
    private void addAdminUser() {
        User user = new InternalUser("admin", "test@gmail.com", "admin", "$2a$10$3wmA5vNtCi7liYg.UskCW.HLZ/PosbDogjrxlenLKR3VV5V/v601u", true);
        user.setUserRoles(ImmutableSet.of(new UserRole(user, "ADMIN")));
        insert(user);
    }
}

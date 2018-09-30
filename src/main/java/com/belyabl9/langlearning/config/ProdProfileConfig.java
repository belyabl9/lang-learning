package com.belyabl9.langlearning.config;

import com.belyabl9.langlearning.service.EmailService;
import com.belyabl9.langlearning.service.impl.email.EmailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdProfileConfig {

    @Bean
    public EmailService emailService() {
        return new EmailServiceImpl();
    }
    
}
package com.belyabl9.langlearning.config;

import com.belyabl9.langlearning.service.EmailService;
import com.belyabl9.langlearning.service.impl.email.DummyEmailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevProfileConfig {

    @Bean
    public EmailService emailService() {
        return new DummyEmailServiceImpl();
    }
    
}
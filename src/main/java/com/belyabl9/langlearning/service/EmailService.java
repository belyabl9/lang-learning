package com.belyabl9.langlearning.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}

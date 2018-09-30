package com.belyabl9.langlearning.service.impl.email;

import com.belyabl9.langlearning.service.EmailService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class DummyEmailServiceImpl implements EmailService {

    public void sendSimpleMessage(String to, String subject, String text) {}
}
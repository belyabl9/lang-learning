package com.belyabl9.langlearning.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = "com.belyabl9.langlearning")
@EnableWebMvc
@EnableJpaRepositories(basePackages = "com.belyabl9.langlearning.repository")
public class MainConfiguration {

}

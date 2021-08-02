package com.myproject.myweb.config;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Configuration
public class ThymeleafLayoutConfig {

    @Bean
    public LayoutDialect layoutDialect(){
        return new LayoutDialect();
    }
}

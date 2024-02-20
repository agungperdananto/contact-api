package com.github.contact.contact;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.contact.contact.resolver.UserArgumentResolver;

@Configuration
public class WebConfiguration implements WebMvcConfigurer{

    @Autowired
    private UserArgumentResolver userArgumentResolver;

    @Override
    public void addArgumentResolvers(@Nullable List<HandlerMethodArgumentResolver> resolvers){
        Objects.requireNonNull(resolvers);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(userArgumentResolver);
    }
}

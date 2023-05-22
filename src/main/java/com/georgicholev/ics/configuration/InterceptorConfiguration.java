package com.example.ics.configuration;

import com.example.ics.interceptions.interceptors.PostRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final PostRequestInterceptor postRequestInterceptor;

    @Autowired
    public InterceptorConfiguration(PostRequestInterceptor postRequestInterceptor) {
        this.postRequestInterceptor = postRequestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(postRequestInterceptor);
    }
}

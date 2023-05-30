package com.example.ics.configuration;

import com.example.ics.interceptions.interceptors.PostRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final PostRequestInterceptor postRequestInterceptor;

    public WebConfiguration(PostRequestInterceptor postRequestInterceptor) {
        this.postRequestInterceptor = postRequestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(postRequestInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("**")
                .allowedOrigins("http://localhost:4200/")
                .allowedMethods("GET", "POST")
                .allowedHeaders("Content-Type");
    }
}

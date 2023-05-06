package com.example.ics.configuration;

import com.example.ics.interceptions.rateLimiters.PostRequestThrottle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LimiterConfiguration {

    @Bean
    public PostRequestThrottle postRequestThrottle() {
        return new PostRequestThrottle();
    }
}

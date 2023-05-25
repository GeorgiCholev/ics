package com.example.ics.configuration;

import com.example.ics.interceptions.rate_limiters.LimiterRequirements;
import com.example.ics.interceptions.rate_limiters.PostRequestThrottle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class LimiterConfiguration {

    private static final LimiterRequirements[] POST_REQUEST_LIMITATIONS =
            new LimiterRequirements[] {new LimiterRequirements(5, Duration.ofMinutes(1))};

    @Bean
    public PostRequestThrottle postRequestThrottle() {
        return new PostRequestThrottle(POST_REQUEST_LIMITATIONS);
    }
}

package com.example.ics.interceptions.rateLimiters;

import java.time.Duration;

public class PostRequestThrottle extends BasicLimiter {

    private static final LimiterRequirements[] POST_REQUEST_LIMITATIONS =
            new LimiterRequirements[] {new LimiterRequirements(5, Duration.ofMinutes(1))};

    public PostRequestThrottle() {
        super(POST_REQUEST_LIMITATIONS);
    }

    @Override
    public boolean tryConsume() {
        return super.tryConsume();
    }
}

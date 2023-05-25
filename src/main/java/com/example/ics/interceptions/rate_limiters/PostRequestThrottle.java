package com.example.ics.interceptions.rate_limiters;


public class PostRequestThrottle extends BasicLimiter {

    public PostRequestThrottle(LimiterRequirements[] limitations) {
        super(limitations);
    }

    @Override
    public boolean tryConsume() {
        return super.tryConsume();
    }
}

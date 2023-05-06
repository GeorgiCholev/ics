package com.example.ics.interceptions.rateLimiters;

import java.time.Duration;

public record LimiterRequirements(int maxRequest, Duration period) {


}

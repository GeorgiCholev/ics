package com.example.ics.interceptions.rate_limiters;

import java.time.Duration;

public record LimiterRequirements(int maxRequest, Duration period) {


}

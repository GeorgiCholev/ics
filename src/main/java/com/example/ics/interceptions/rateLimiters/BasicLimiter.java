package com.example.ics.interceptions.rateLimiters;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucketBuilder;


public abstract class BasicLimiter {

    private final Bucket bucket;

    protected BasicLimiter(LimiterRequirements[] limitations) {
        LocalBucketBuilder builder = Bucket.builder();

        addLimitations(limitations, builder);

        this.bucket = builder.build();
    }

    protected boolean tryConsume() {
        return bucket.tryConsume(1);
    }

    private LocalBucketBuilder addLimitations(LimiterRequirements[] limitations, LocalBucketBuilder builder) {
        for (LimiterRequirements limitation : limitations) {
            builder = addLimitation(limitation, builder);
        }
        return builder;
    }

    private LocalBucketBuilder addLimitation(LimiterRequirements limitation, LocalBucketBuilder builder) {
        int capacity = limitation.maxRequest();
        Refill refill = Refill.intervally(capacity, limitation.period());
        Bandwidth bandwidth = Bandwidth.classic(capacity, refill);
        return builder.addLimit(bandwidth);
    }

}

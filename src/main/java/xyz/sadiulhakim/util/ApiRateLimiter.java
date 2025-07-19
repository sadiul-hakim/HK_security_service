package xyz.sadiulhakim.util;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ApiRateLimiter {

    private final RateLimiterRegistry rateLimiterRegistry;

    public ApiRateLimiter(RateLimiterRegistry rateLimiterRegistry) {
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    public boolean canLoad(String userId, int callNumber, Duration timeoutDuration) {

        // Refresh period should match the timeout because after the timeoutDuration ends we want it to refresh.
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(callNumber)
                .limitRefreshPeriod(timeoutDuration)
                .timeoutDuration(Duration.ofMillis(1))
                .build();

        try {
            RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter(userId, config);
            return rateLimiter.acquirePermission();

        } catch (RequestNotPermitted exception) {
            return false;
        }
    }
}

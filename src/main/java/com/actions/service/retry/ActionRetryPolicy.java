package com.actions.service.retry;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Defines retry delays for action failures.
 */
public final class ActionRetryPolicy {

    private static final List<Duration> TRANSIENT_DELAYS = List.of(
            Duration.ofSeconds(1),
            Duration.ofSeconds(5),
            Duration.ofSeconds(15));

    private static final List<Duration> RATE_LIMIT_DELAYS = List.of(
            Duration.ofSeconds(5),
            Duration.ofSeconds(15),
            Duration.ofSeconds(30));

    /**
     * Returns the delay after a failed attempt, or empty when the action should
     * not be retried.
     *
     * @param failedAttempt one-based failed attempt number
     * @param category failure classification
     * @return the next retry delay, if another attempt is allowed
     */
    public Optional<Duration> nextDelay(
            int failedAttempt,
            FailureCategory category) {
        if (failedAttempt <= 0) {
            throw new IllegalArgumentException(
                    "failedAttempt must be greater than zero");
        }

        Objects.requireNonNull(category, "category must not be null");

        List<Duration> schedule = switch (category) {
            case TRANSIENT -> TRANSIENT_DELAYS;
            case RATE_LIMIT -> RATE_LIMIT_DELAYS;
            case VALIDATION, AUTHENTICATION -> List.of();
        };

        if (failedAttempt > schedule.size()) {
            return Optional.empty();
        }

        return Optional.of(schedule.get(failedAttempt - 1));
    }
}

package com.actions.service.retry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class ActionRetryPolicyTest {

    private final ActionRetryPolicy policy = new ActionRetryPolicy();

    @Test
    void nextDelay_transientFailure_returnsRequiredSchedule() {
        assertThat(policy.nextDelay(1, FailureCategory.TRANSIENT))
                .contains(Duration.ofSeconds(1));
        assertThat(policy.nextDelay(3, FailureCategory.TRANSIENT))
                .contains(Duration.ofSeconds(15));
        assertThat(policy.nextDelay(4, FailureCategory.TRANSIENT)).isEmpty();
    }

    @Test
    void nextDelay_rateLimitFailure_returnsRequiredSchedule() {
        assertThat(policy.nextDelay(1, FailureCategory.RATE_LIMIT))
                .contains(Duration.ofSeconds(5));
        assertThat(policy.nextDelay(3, FailureCategory.RATE_LIMIT))
                .contains(Duration.ofSeconds(30));
        assertThat(policy.nextDelay(4, FailureCategory.RATE_LIMIT)).isEmpty();
    }

    @Test
    void nextDelay_nonRetryableFailure_returnsEmpty() {
        assertThat(policy.nextDelay(1, FailureCategory.VALIDATION)).isEmpty();
        assertThat(policy.nextDelay(1, FailureCategory.AUTHENTICATION)).isEmpty();
    }

    @Test
    void nextDelay_invalidAttempt_throwsIllegalArgumentException() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> policy.nextDelay(0, FailureCategory.TRANSIENT));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> policy.nextDelay(-1, FailureCategory.TRANSIENT));
    }

    @Test
    void nextDelay_nullCategory_throwsNullPointerException() {
        assertThatNullPointerException()
                .isThrownBy(() -> policy.nextDelay(1, null))
                .withMessageContaining("category");
    }
}

package com.actions.service.demo;

import com.actions.service.retry.ActionRetryPolicy;
import com.actions.service.retry.FailureCategory;
import com.actions.service.retry.FailureCategoryParser;
import java.time.Duration;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Runs the prepared runtime-exception scenario when the demo property is set.
 */
@Component
@ConditionalOnProperty(name = "demo.failure-category")
public final class RuntimeFailureDemo implements ApplicationRunner {

    private final FailureCategoryParser parser = new FailureCategoryParser();
    private final ActionRetryPolicy retryPolicy = new ActionRetryPolicy();

    @Override
    public void run(ApplicationArguments args) {
        String rawCategory = args.getOptionValues("demo.failure-category").get(0);
        FailureCategory category = parser.parse(rawCategory);
        Duration firstDelay = retryPolicy.nextDelay(1, category).orElseThrow();

        System.out.printf(
                "First retry for %s is %d seconds.%n",
                category,
                firstDelay.toSeconds());
    }
}

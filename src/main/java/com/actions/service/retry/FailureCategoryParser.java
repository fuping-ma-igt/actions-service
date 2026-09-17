package com.actions.service.retry;

import java.util.Locale;
import java.util.Objects;

/**
 * Converts external failure-category values into the domain enum.
 */
public final class FailureCategoryParser {

    /**
     * Parses a failure category supplied by an external system.
     *
     * @param rawCategory external category value
     * @return the matching domain category
     */
    public FailureCategory parse(String rawCategory) {
        Objects.requireNonNull(rawCategory, "rawCategory must not be null");

        return FailureCategory.valueOf(
                rawCategory.trim().toUpperCase(Locale.ROOT));
    }
}

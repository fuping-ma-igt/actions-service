package com.actions.service.retry;

/**
 * Classifies failures so the service can decide whether and when to retry.
 */
public enum FailureCategory {
    TRANSIENT,
    RATE_LIMIT,
    VALIDATION,
    AUTHENTICATION
}

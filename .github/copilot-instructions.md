# Actions Service Copilot Instructions

## Project context

- This is a Java 17, Spring Boot 3.3, Maven project.
- Run tests from Windows with `./mvnw.cmd test`.
- Put retry-domain code in `com.actions.service.retry`.
- Keep domain policy classes independent of Spring. Do not add Spring annotations to
  a class that only contains business rules.

## Retry policy contract

When implementing the retry policy for failed actions, use this exact public API:

- Create `ActionRetryPolicy` with
  `Optional<Duration> nextDelay(int failedAttempt, FailureCategory category)`.
- Create `FailureCategory` with exactly these values: `TRANSIENT`, `RATE_LIMIT`,
  `VALIDATION`, and `AUTHENTICATION`.
- `failedAttempt` is one-based. Throw `IllegalArgumentException` when it is zero or
  negative.
- `VALIDATION` and `AUTHENTICATION` failures are never retried.
- For `TRANSIENT`, return delays of 1, 5, and 15 seconds after failed attempts 1,
  2, and 3 respectively.
- For `RATE_LIMIT`, return delays of 5, 15, and 30 seconds after failed attempts 1,
  2, and 3 respectively.
- Return `Optional.empty()` after failed attempt 3.
- Reject a null category with a useful message.
- Express retry schedules as named, immutable constants. Do not hide these business
  values in control flow as magic numbers.

## Shared engineering conventions

Follow these shared instructions when creating or changing Java source and tests:

- [Java Coding Standard](https://github.com/fuping-ma-igt/ai-tools/blob/main/instructions/java-coding-standard.instructions.md)
- [Java Unit Tests](https://github.com/fuping-ma-igt/ai-tools/blob/main/instructions/java-unit-tests.instructions.md)

The shared Java unit-test instructions define framework and naming conventions.
For this feature, also test the first and last scheduled attempts, the exhausted
boundary, both non-retryable categories, zero and negative attempts, and a null
category. Test the pure domain policy directly without starting a Spring context.

## Definition of done

- Run `./mvnw.cmd test` and report the result.
- Do not add dependencies unless the existing build cannot express the requirement.

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

## Java conventions

- Use 4 spaces, never tabs, and aim for lines no longer than 80 characters.
- Use braces for every control-statement body.
- Do not reassign method parameters.
- Use one top-level type per file and no wildcard imports.
- Add useful Javadoc to public types and public methods. Document constraints and
  behavior instead of restating names.
- Prefer immutable data and modern Java APIs. Use `Objects.requireNonNull` for the
  category validation.

## Unit-test conventions

- Use JUnit 5 and AssertJ for new tests.
- Name a test class `[ClassUnderTest]Test`, ending in singular `Test`.
- Name test methods `[method]_[state]_[expectedBehaviour]`.
- Use one scenario per test method so a failure identifies the broken behavior.
- Test the first and last scheduled attempts, the exhausted boundary, both
  non-retryable categories, zero/negative attempts, and a null category.
- Test pure domain classes directly. Do not use `@SpringBootTest` or start a Spring
  context for these unit tests.
- Do not rewrite an existing, passing test solely to change its style.

## Definition of done

- Run `./mvnw.cmd test` and report the result.
- Do not add dependencies unless the existing build cannot express the requirement.

# Runtime-exception demo prompts

Use one Copilot chat session so the investigation and approved plan remain in
context. Keep the repository on the prepared starter state until Agent begins the
implementation.

## Ask

> Do not change any files. Run the prepared runtime demo and trace the exception
> to its root cause. Explain why the current tests pass even though the application
> fails for the production-style value `rate-limit`. Reference the relevant Java
> files and distinguish confirmed findings from assumptions.

## Plan

> Plan a safe fix for the runtime failure. External failure-category values use
> lower-case kebab-case, while the Java enum uses upper-case snake-case. Preserve
> the public retry-policy API and reject unsupported values with a useful message.
> Include affected files, regression cases, verification commands, assumptions and
> risks. Do not edit files yet.

## Agent

> Implement the approved plan. Limit changes to failure-category parsing and its
> tests. Do not weaken or remove existing assertions. Run the full test suite and
> the prepared runtime demo. Stop and ask before changing the public retry-policy
> API or unrelated code. At the end, report files changed, test results,
> assumptions and anything unverified.

## Human review

Inspect the final diff, the new boundary and invalid-input tests, the full Maven
test result and the successful runtime-demo output before accepting the change.

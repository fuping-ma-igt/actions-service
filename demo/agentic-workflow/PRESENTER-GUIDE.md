# Ask, Plan and Agent: Java runtime-exception demo

## Purpose

The demo shows the three built-in GitHub Copilot roles working on one real Java
failure. The service compiles and its unit tests pass, but application startup
fails when an external system supplies a category in kebab-case.

## Prepared failure

Run from the repository root:

```powershell
.\demo\agentic-workflow\run-runtime-demo.ps1
```

Expected starter result:

```text
java.lang.IllegalArgumentException:
No enum constant com.actions.service.retry.FailureCategory.RATE-LIMIT
```

The defect is in `FailureCategoryParser`. It converts input to upper case but does
not translate the external hyphen separator to the enum's underscore separator.
The existing parser tests cover lower-case input and whitespace, but not kebab-case.

## Twelve-minute live sequence

1. Ask, 3 minutes: run the failure, trace the stack and explain the coverage gap.
2. Plan, 3 minutes: agree on normalization, error behavior and regression cases.
3. Agent, 4 minutes: implement the approved plan and run both verification paths.
4. Human review, 2 minutes: inspect the diff, tests, runtime output and assumptions.

Use the exact prompts in `demo/agentic-workflow/PROMPTS.md`.

## Expected repair

A suitable repair trims the value, converts hyphens to underscores and applies
locale-independent upper casing before calling `FailureCategory.valueOf`. It also
adds focused tests for `rate-limit`, invalid values and any other cases agreed in
the plan. The public `ActionRetryPolicy` API should remain unchanged.

After the repair, the runtime demo should finish with:

```text
First retry for RATE_LIMIT is 5 seconds.
```

## Rehearsal and recovery

- Use the same VS Code, Copilot extension and model as the audience.
- Confirm Ask, Plan and Agent appear in the agent selector.
- Warm the Maven dependency cache before the session.
- Start from a clean Git checkpoint and keep a local copy of the repaired diff.
- If Copilot behaves unexpectedly, use the result to discuss scope and evidence.
- Never let the agent change tests only to make the failure disappear.

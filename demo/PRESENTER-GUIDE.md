# GitHub Copilot custom-instructions demo

## What the audience should learn

The same short request can produce generic, guessed code or code that follows the
team's exact business contract. Custom instructions supply durable repository
context without making every developer repeat it in every prompt.

The most visible comparison is not formatting. It is whether the generated retry
policy implements the correct failure categories, schedules, API, boundaries, and
test strategy.

## Why the existing link-only instructions were changed

The previous `.github/copilot-instructions.md` contained only web links to two
shared standards. A link is not a dependable substitute for instruction content:
the model may not retrieve it, access may require authentication, and the result
depends on network availability. This demo keeps the important rules short,
self-contained, and local. The shared documents can still remain the human source
of truth outside the demo.

## Preparation (before the presentation)

1. Use one IDE, one Copilot model, and the same Copilot mode for both runs.
2. Commit or stash unrelated changes. Create a tag or branch for the clean starting
   point so both runs start from identical production code.
3. Run `./mvnw.cmd test` once to warm the Maven cache.
4. Open Copilot settings and search for `instruction file`. Confirm that **Code
   Generation: Use Instruction Files** can be toggled.
5. Perform both runs privately once. Save the two diffs or screenshots as a fallback
   in case a live model response is slow or unusually lucky.
6. Use a new chat for each run. Chat history is otherwise an uncontrolled source of
   context.

## Live run 1: without instructions (about 3 minutes)

1. Reset to the clean starting point.
2. Clear **Code Generation: Use Instruction Files**.
3. Start a new Copilot chat.
4. Paste the prompt from `demo/PROMPT.md` exactly.
5. Accept the implementation without coaching it or answering requirement questions
   with details from the instruction file.
6. Show the generated API and tests, then run the contract verifier:

   ```powershell
   .\demo\verify-demo.cmd
   ```

Expected result: the code may be reasonable, but it will normally guess the retry
categories, delays, maximum attempts, validation behavior, package, and test naming.
The contract verifier should fail at compilation or behavior assertions.

If Copilot asks for the missing rules, that is also a valid baseline outcome: it
demonstrates that the prompt alone does not contain enough project knowledge.

## Live run 2: with instructions (about 4 minutes)

1. Reset to exactly the same clean starting point and start a new chat.
2. Select **Code Generation: Use Instruction Files**.
3. Paste the same prompt without adding any detail.
4. In the response's References list, show the audience that
   `.github/copilot-instructions.md` was included.
5. Accept the implementation and let Copilot run the tests.
6. Run `.\demo\verify-demo.cmd`.

Expected result: the contract verifier passes. Point out the exact API, the two
schedules, non-retryable categories, boundary handling, immutable constants,
Javadoc, focused JUnit 5 tests, and absence of `@SpringBootTest` in the new unit
test.

## Comparison scorecard

Score each item as pass/fail on screen. The contract verifier checks the first six.

| # | Observable result | Without instructions | With instructions |
|---|---|---:|---:|
| 1 | Exact `nextDelay` API and package | Usually fail | Pass |
| 2 | Four exact failure categories | Usually fail | Pass |
| 3 | Correct transient schedule | Usually fail | Pass |
| 4 | Correct rate-limit schedule | Usually fail | Pass |
| 5 | Non-retryable categories return empty | Inconsistent | Pass |
| 6 | Input and exhausted-boundary behavior | Inconsistent | Pass |
| 7 | Named immutable constants, no Spring stereotype | Inconsistent | Pass |
| 8 | Focused JUnit 5 tests with team naming | Inconsistent | Pass |

## Consistency safeguards

- Treat the verifier, not the prose quality of Copilot's answer, as the result.
- Never compare two runs with different prompts, models, modes, or starting files.
- Keep temperature-like variability out of the story: show a saved baseline diff if
  the no-instructions run happens to guess several rules.
- If the baseline somehow passes all contract checks, explain that it inferred or
  discovered repository context and use the saved baseline. Do not keep rerunning
  until a preferred answer appears.
- Do not open the files under `demo/contract` in Copilot's chat context. They are a
  presentation test harness, not implementation guidance.

## Suggested talk track (8-10 minutes total)

1. **Set-up:** "This prompt says what feature we want, but it does not repeat our
   team's domain contract."
2. **Baseline:** "This is plausible Java, yet plausible is not the same as correct
   for this repository."
3. **Reveal:** briefly show the instruction file and its project-specific rules.
4. **Second run:** show the same prompt and the instruction reference.
5. **Proof:** run the verifier and compare the scorecard.
6. **Close:** "Instructions are versioned team context. They improve consistency;
   tests still provide the enforcement."

## Optional follow-up demo

After the main comparison, deliberately change one schedule value in the generated
code and run the verifier again. This reinforces the boundary: instructions guide
generation, while executable tests prevent later regressions.

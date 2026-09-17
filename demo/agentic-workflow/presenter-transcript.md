# Presenter transcript: Ask, Plan and Agent

This transcript supports a 30-minute session. Slides 14–16 are backup slides and
do not appear in the main timing.

## Slide 1 — A safe agentic workflow (0:00–0:45)

Today we will use the three built-in GitHub Copilot roles in VS Code on one real
Java problem. We will start by defining what an AI agent does, then compare Ask,
Plan and Agent. The second half is a live investigation of a Spring Boot runtime
exception.

The goal is not maximum automation. The goal is a controlled workflow that gives
us evidence before we accept a change.

## Slide 2 — The missing connection (0:45–2:15)

We have already trained three useful capabilities. Prompting states the goal.
Instructions carry repository and team context. MCP adds tools and external
systems.

An agentic workflow connects those capabilities. The agent receives a goal and
context, chooses tools, observes the results and continues toward an outcome.
Today's focus is how we control that loop and judge its evidence.

## Slide 3 — What an AI agent does (2:15–4:20)

The model is one component of the agent. The agent repeatedly gathers context,
reasons about the next step, calls a tool and observes the result. It may continue,
ask for clarification or stop.

Tools can read source files, search the repository, edit Java, run Maven or call an
MCP server. Tool output becomes new context for the next decision.

The human stays responsible for four things: defining success, controlling
permissions, reviewing evidence and deciding whether to accept the result. A
completed tool call proves only that the tool ran. It does not prove that the
requirement is satisfied.

## Slide 4 — One word, two meanings (4:20–5:30)

There is a naming collision to clear up. An AI agent is the general system: a
model, context, tools and an iterative loop.

Agent is also the name of VS Code's implementation role. Ask and Plan are built-in
roles too, but they carry less authority for the current stage of work. I will use
the capitalized names when I mean those VS Code roles.

## Slide 5 — Three built-in roles (5:30–8:00)

Choose the role by the outcome you need.

Use Ask when you need understanding, analysis or options. It can inspect the
workspace, but its output is advice rather than an implementation.

Use Plan when you need an agreed approach before files change. Plan researches
with read-only tools, asks questions and produces implementation and verification
steps.

Use Agent when the outcome is bounded and ready to implement. Agent can edit files,
run commands and iterate, subject to the permissions we grant.

The short rule is: Ask to understand. Plan to agree. Agent to execute.

## Slide 6 — The controlled workflow (8:00–10:00)

The roles form a workflow with human checkpoints.

After Ask, confirm that the problem is correctly understood. After Plan, decide
whether the proposed approach, scope and verification are acceptable. During
Agent, watch the requested tool actions and any scope expansion. After Agent,
inspect the complete diff and the verification evidence.

Authority increases only after the previous checkpoint is satisfied. When the
problem remains unclear, stay in Ask. When the approach is uncertain, stay in
Plan.

## Slide 7 — Demo setup (10:00–11:30)

[Open the integrated terminal in the `actions-service` repository.]

This Spring Boot service compiles and eight tests pass. The demo supplies a
production-style failure category, `rate-limit`, during application startup.

[Run `.\demo\agentic-workflow\run-runtime-demo.ps1`.]

The application throws `IllegalArgumentException`. The stack points through
`RuntimeFailureDemo` into `FailureCategoryParser`. The important detail is that
our tests are green while the real input still fails.

The request we received is deliberately brief: “The application fails for
rate-limit. Fix it.” We will not let Agent edit immediately.

## Slide 8 — Ask: establish the cause (11:30–14:30)

[Select Ask and paste the Ask prompt from `demo/agentic-workflow/PROMPTS.md`.]

Ask must run the prepared demo, trace the stack and inspect the relevant Java and
test files without changing anything.

While it works, look for four pieces of evidence. Which call fails? How does the
parser transform the input? Which test case is missing? Which statements are
confirmed by code or command output, and which are assumptions?

The expected finding is that the parser trims the text and converts it to upper
case. That produces `RATE-LIMIT`. `Enum.valueOf` expects the Java enum name
`RATE_LIMIT`, so it throws. The parser tests cover lower-case text and whitespace,
but not the external hyphen separator.

[Ask the audience:] Do we now understand the failure well enough to plan a repair?

## Slide 9 — Plan: agree the repair (14:30–17:30)

[Switch to Plan and paste the Plan prompt.]

The prompt supplies the external contract: incoming values use lower-case
kebab-case, while the Java enum uses upper-case snake-case. It also protects the
public retry-policy API and asks for useful behavior when the value is unsupported.

Review the proposed plan before implementation. The scope should stay within
`FailureCategoryParser` and focused tests. Regression cases should cover
`rate-limit`, whitespace and invalid input. Verification should include both the
full Maven test suite and the runtime-demo command.

If Plan proposes unrelated refactoring, remove it. If it omits invalid-input
behavior or only runs one narrow test, correct it. Plans are negotiated, not
automatically accepted.

## Slide 10 — Agent: implement and verify (17:30–21:30)

[Start implementation from the approved plan, or switch to Agent and paste the
Agent prompt.]

Watch the working loop. Agent reads the relevant files, edits the parser and tests,
runs commands and observes the output. It may iterate if a test fails.

Approve tool calls according to their effect. A Maven test command is expected.
Changing the retry-policy API, deleting tests or touching unrelated files is not.

The likely implementation translates hyphens to underscores before applying
locale-independent upper casing. The expected regression test proves that
`rate-limit` maps to `RATE_LIMIT`. The full suite should pass, and the runtime demo
should print: `First retry for RATE_LIMIT is 5 seconds.`

## Slide 11 — Human review (21:30–23:30)

[Open the Source Control diff.]

Check the implementation before accepting it. The parser should normalize the
external separator before enum conversion. Tests should cover the production
value and the error behavior agreed in the plan. Existing assertions should remain
intact.

Check four forms of evidence: the diff stays in scope, the full Maven suite passes,
the runtime command now succeeds and unsupported values still fail clearly.

Finally, read the agent's assumptions and anything it could not verify. Agent can
propose that the task is complete. The human decides whether the evidence supports
acceptance.

## Slide 12 — What each role contributed (23:30–26:30)

Ask answered “What failed?” with a trace and repository evidence. Plan answered
“What should change?” with an agreed repair and test strategy. Agent answered “Can
I implement and verify it?” with a diff and command output. The human answered
“Should we accept it?” by comparing that evidence with the requirement.

[Ask the audience:] Which checkpoint reduced the most risk in this example? What
would have happened if we had started directly in Agent with only “Fix it”?

One testing risk deserves emphasis. Tests generated only from the implementation
can repeat the implementation's misunderstanding. The external contract and
acceptance cases should exist before Agent writes the fix.

## Slide 13 — Three habits for safe agentic work (26:30–30:00)

Remember three habits.

First, use the least-authoritative role that can complete the current step. Second,
define acceptance criteria before implementation begins. Third, review the diff and
verification evidence before accepting a change.

If you cannot explain how success will be verified, return to Ask or Plan.

Ask to understand. Plan to agree. Agent to execute. Human to decide.

[Take questions.]

## Sources

- [Build with agents in VS Code](https://code.visualstudio.com/docs/agents/overview)
- [Choose and use an agent harness](https://code.visualstudio.com/docs/agents/run/agent-harnesses)
- [Planning with agents in VS Code](https://code.visualstudio.com/docs/agents/planning)

# Development Cycle

This project should move through small, reviewable steps instead of jumping directly into features.

## 1. Product Definition

- Maintain the PRD in `docs/PRD.md`.
- Keep open questions visible.
- Convert agreed requirements into GitHub issues.
- Avoid building features that are outside the current milestone.

## 2. Planning

Each feature should have:

- User problem.
- Expected workflow.
- Acceptance criteria.
- Data needed.
- Risks or edge cases.

## 3. Branching

Use short-lived branches:

```text
docs/prd-v1
feature/rider-management
feature/ride-search
fix/fare-calculation
```

Do not commit directly to `main` once the project moves beyond prototypes.

## 4. Pull Requests

Every meaningful change should go through a PR with:

- Summary.
- Screenshots for UI changes.
- Test notes.
- Known limitations.

## 5. Testing

For the current static app:

- Run `node --check app.js`.
- Open the app in a browser.
- Load demo data.
- Create a ride.
- Add a rider.
- Assign a rider.
- Copy WhatsApp messages.
- Export CSV.

For future backend work:

- Add unit tests for fare calculation.
- Add integration tests for ride creation and assignment.
- Add end-to-end tests for dispatcher workflows.

## 6. Release Process

For each release:

1. Confirm PRD scope.
2. Merge reviewed PRs.
3. Tag the release.
4. Update README if setup changes.
5. Record known limitations.

## 7. Immediate Backlog

- Convert PRD requirements into GitHub issues.
- Create Android wireframes for passenger and bike owner flows.
- Decide Android stack and package structure.
- Define API contract for auth, riders, ride offers, ride requests, and trips.
- Create backend project skeleton.
- Create Android app skeleton.
- Define first launch routes around Bellary.
- Confirm legal/compliance assumptions for peer-to-peer bike pooling in Karnataka.

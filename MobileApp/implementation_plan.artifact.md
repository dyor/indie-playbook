# Implementation Plan: Authentication, Suggest Edits & Admin Review Flow

The "suggested edits" feature was not previously implemented in the codebase, which is why the option was not visible. Below is the proposed plan to enable authentication, allow users to submit suggested edits, and give admins the power to make direct edits or review and approve suggestions with a visual diff.

---

## User Review Required

> [!IMPORTANT]
> - **Authentication**: We will enable social sign-in (`AUTH_SOCIAL_LOGIN_ENABLED = true`) and configure admin identification via `AppConfiguration.ADMIN_EMAILS` (or Firestore user roles).
> - **Firestore Structure**:
>   - Collection `app_stories` for current stories.
>   - Collection `story_suggestions` for user-submitted edits.
> - **Diff Visualization**: When admins review suggestions, additions will be displayed in **bold**, and removals will be displayed with ~~strikethrough~~.

---

## Proposed Changes

### Phase 1: Authentication & Admin Status
- Enable social login in `AppConfiguration.kt`.
- Update `User.kt` domain model to include `isAdmin: Boolean`.
- Update `UserRepository.kt` to mark user as admin if their email matches `AppConfiguration.ADMIN_EMAILS` or an admin role.

### Phase 2: Data Models & Firestore Suggestion Repository
- Create `AppStorySuggestion` model (`id`, `storyId`, `userId`, `userEmail`, `suggestedFields`, `status`, `timestamp`).
- Add Firestore methods in `AppStoryRepository` / `AppStorySuggestionRepository`:
  - `submitSuggestion(suggestion: AppStorySuggestion)`
  - `getPendingSuggestions(): Flow<List<AppStorySuggestion>>`
  - `approveSuggestion(suggestion: AppStorySuggestion)`: Updates `app_stories` in Firestore & marks suggestion as approved.
  - `rejectSuggestion(suggestionId: String)`
  - `updateStoryDirectly(story: AppStory)`: Admin direct edit in Firestore.

### Phase 3: Word/Text Diff Utility
- Implement a diff calculator in `util/diff/TextDiff.kt` that compares original string vs suggested string.
- Produces `AnnotatedString` with `FontWeight.Bold` for additions and `TextDecoration.LineThrough` for deletions.

### Phase 4: User Suggest Edits Flow
- Add "Suggest Edits" button on `AppStoryDetailScreen`.
- Create `SuggestEditsScreen` (and `SuggestEditsViewModel`) allowing signed-in users to edit fields (Name, One Liner, Tech Stack, Origin Story, Growth Playbook, etc.) and submit to Firestore.
- Prompt unauthenticated/guest users to sign in first.

### Phase 5: Admin Capabilities (Direct Edit & Suggestion Approval)
- Add Admin controls on `AppStoryDetailScreen` and/or `AccountScreen` when `user.isAdmin == true`.
- **Direct Edit**: `AdminEditStoryScreen` lets admins update story fields immediately in Firestore.
- **Review Suggestions**: `AdminReviewSuggestionsScreen` displays:
  1. What the user suggested.
  2. Directly below, the calculated diff from original (bold additions, strikethrough deletions).
  3. "Approve" (applies changes to story & updates Room / Firestore) and "Reject" buttons.

---

## Verification Plan

### Automated Tests
- Unit tests for `TextDiff`: verify correct bold / strikethrough tagging for string insertions, deletions, and replacements.
- Repository unit tests for suggestion submission and approval mapping.

### Manual Verification
- Sign in as a regular user, submit a suggested edit for an App Story, and verify it lands in Firestore `story_suggestions`.
- Sign in as an admin, navigate to Admin Review screen, verify the diff display (bold for new text, strikethrough for deleted text), approve the edit, and verify the story updates in `app_stories` and in the app feed.

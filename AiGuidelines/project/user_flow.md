# Screens
1. **Onboarding Screen (Welcome)**
    - Purpose: Introduce the app's value proposition.
    - Key Elements: Headline about discovering top indie apps, brief description of tech stacks covered (KMP, Flutter, RN), "Get Started" button.
    - Expected Actions: Tap "Get Started" to proceed.

2. **Home Feed Screen**
    - Purpose: The primary discovery surface for app stories.
    - Key Elements: Tech stack filter chips (All, KMP, Flutter, React Native), scrollable list of app summary cards (App Icon, Name, One-liner, Tech Stack, Bookmark icon).
    - Expected Actions: Scroll feed, select a tech stack filter, toggle bookmark, tap a card to view details.

3. **App Story Detail Screen**
    - Purpose: Display the full case study for a selected indie app.
    - Key Elements: App header (Icon, Name, Tech Stack, Developer), "Origin Story" section, "Growth Playbook" section, floating or sticky bookmark button, Ad banner (placeholder for Phase 4).
    - Expected Actions: Read the story, toggle bookmark, navigate back to the feed.

4. **Saved (Bookmarks) Screen**
    - Purpose: Access previously saved stories.
    - Key Elements: List of bookmarked app summary cards, empty state graphic/message if nothing is saved.
    - Expected Actions: Tap a card to read the story, swipe or tap to un-bookmark.

# User Flow
- **Primary Use Case (First Session Discovery)**
  1. App Launch -> Onboarding Screen
  2. Onboarding Screen -> Tap "Get Started" -> Home Feed Screen
  3. Home Feed Screen -> Scroll and tap a specific app card (e.g., a KMP app) -> App Story Detail Screen
  4. App Story Detail Screen -> Read story -> Tap Bookmark icon -> Navigate Back -> Home Feed Screen

- **Alternative Path (Filtering)**
  1. Home Feed Screen -> Tap "Flutter" filter chip -> Feed updates to show only Flutter apps
  2. Tap an app card -> App Story Detail Screen

- **Alternative Path (Returning User - Reading Saved Items)**
  1. App Launch -> Home Feed Screen (Onboarding is skipped)
  2. Home Feed Screen -> Tap "Saved" in Bottom Nav -> Saved Screen
  3. Saved Screen -> Tap a saved story -> App Story Detail Screen
  4. App Story Detail Screen -> Un-bookmark -> Navigate Back -> Saved Screen (Item is removed)

# Navigation Structure
- **Primary Navigation**: Bottom Navigation Bar with two tabs:
  - **Discover** (Home Feed Screen)
  - **Saved** (Bookmarks Screen)
- **Secondary Navigation**:
  - Filter chips on the Home Feed.
  - Standard "Back" button navigation from the App Story Detail Screen to the previous screen.

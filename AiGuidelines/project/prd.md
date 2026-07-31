# Goal
Provide developers with actionable insights and inspiration by showcasing the stories, technical strategies, and marketing playbooks behind the top indie apps built with React Native, Flutter, and Kotlin Multiplatform.

# Target Audience
- Independent developers building commercial mobile apps.
- Software engineers interested in cross-platform development (React Native, Flutter, KMP).
- Indie hackers looking for proven marketing and growth strategies to increase visibility and downloads.
- Developers seeking inspiration from successful peers.

# Core Features
1. **Curated Indie App Feed**
    - Purpose: Showcase successful indie apps and their builder's stories.
    - Key Elements: Scrollable list of app profiles. Each profile includes a summary of the app's origin story (why it was built) and its growth journey (how it reached the top 10K).
    - Expected User Actions: Scroll through the feed, tap to read the full story.

2. **Categorized Tech Stack Filtering**
    - Purpose: Allow users to easily find apps built with specific frameworks they are interested in.
    - Key Elements: Filter chips or tabs for "React Native", "Flutter", and "KMP".
    - Expected User Actions: Tap a filter to view apps built exclusively with that technology.

3. **"Save for Later" Bookmarks**
    - Purpose: Enable users to save interesting technical playbooks or marketing strategies for future reference.
    - Key Elements: A bookmark icon on each story, and a dedicated "Saved" tab/screen to view bookmarked items.
    - Expected User Actions: Tap the bookmark icon to save/unsave, navigate to the "Saved" screen to read later.

# Monetization
- **Ads Supported**: The app will be free to use and monetized entirely through advertisements.
- No subscriptions or in-app purchases for the MVP.

# Competitors
- **Indie Hackers** — Strengths: Huge community, diverse content. Weaknesses: Not exclusively focused on mobile apps or specific tech stacks.
- **X/Twitter (Tech Twitter)** — Strengths: Real-time insights from builders. Weaknesses: Content is fragmented, hard to search, and often lacks deep, structured case studies.
- **Medium/Dev.to** — Strengths: Deep technical articles. Weaknesses: Broad focus; finding specific indie app growth stories requires significant filtering.

# Unique Selling Points
- **Laser-focused Niche**: Exclusively focuses on indie mobile apps built with React Native, Flutter, and KMP that have achieved top 10K status.
- **Structured Storytelling**: Provides a consistent narrative format for every app: the origin story combined with the specific growth playbook.
- **Actionable Inspiration**: Combines technical curiosity (which framework did they use?) with business strategy (how did they market it?).

# Technical Notes
- **Target Platforms**: iOS, Android, Desktop, Web (via Kotlin Multiplatform).
- **Architecture**: MVI pattern using Kotlin flows and ViewModels.
- **Storage**: Local persistence using Room 3 for the "Save for Later" feature and offline caching of the feed.
- **Monetization**: AdMob integration (deferred to Phase 4).
- **Backend (Future)**: The MVP can use static data or a simple mock API, but will eventually need a backend (Phase 2 integrations) to serve fresh stories and manage ads.

# Design Guidelines
- **Vibe**: Clean, modern, developer-centric, inspiring.
- **Tone**: Professional yet authentic and story-driven.
- **Accessibility**: High contrast for readability of long-form text, clear touch targets, support for dark mode.
- **Branding**: Needs to feel like a premium tech publication or curated newsletter.

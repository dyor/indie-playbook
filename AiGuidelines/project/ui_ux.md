# User Flow
- **Primary path**: 
  1. User launches the app and sees a clean, developer-focused onboarding screen emphasizing actionable growth playbooks.
  2. Lands on the Home Feed ("Discover"). The feed is pre-populated with high-quality cards showcasing top indie apps.
  3. User taps a filter chip (e.g., "KMP") to narrow down the tech stack.
  4. User taps an intriguing card to enter the "App Story Detail Screen" to read the origin and growth story.
  5. User taps the floating bookmark button to save the playbook for later reference.
- **Alternative paths**: 
  - User navigates to the "Saved" tab to review previously bookmarked playbooks.
- **Emotional journey notes**: The experience should evoke **curiosity** (discovering how others succeeded) and **motivation** (feeling empowered to apply the strategies). The interface must feel fast, native, and uncluttered—respecting the developer's time and attention.

# Screens
1. **Onboarding Screen (Welcome)**
    - Purpose: Hook the developer by promising actionable insights from top indie apps.
    - Key Elements: Bold typography, subtle code-like or blueprint background pattern, primary CTA.
    - Main Actions: Start discovering.
    - Delight Factor: *Slide-Up Reveal* for the text, with a subtle parallax effect on the background.

2. **Home Feed Screen (Discover)**
    - Purpose: Browse and filter the curated list of successful indie apps.
    - Key Elements: Sticky top bar with tech stack filter chips. Rich, magazine-style cards (App Icon, Title, Tech Stack Badge, Growth metric highlight).
    - Main Actions: Scroll, filter by tech stack, tap to read, quick-save bookmark icon on cards.
    - Delight Factor: *Springy Filter Chips* — when a filter is tapped, it gently bounces into the active state, and the list filters with a smooth *Staggered Fade-In* animation.

3. **App Story Detail Screen**
    - Purpose: Deep dive into the origin and growth playbook of a specific app.
    - Key Elements: Hero header with app icon and name. Clean, readable typography for the long-form content. Distinct sections for "The Origin" and "The Growth Playbook".
    - Main Actions: Read, bookmark.
    - Delight Factor: *Hero Image Shared Element Transition* — the app icon smoothly expands from the feed card into the detail header. A subtle reading progress bar fixed to the top edge.

4. **Saved Screen**
    - Purpose: Access bookmarked playbooks.
    - Key Elements: Grid or list of saved cards. A clever, developer-themed empty state (e.g., a "404 No Playbooks Found" illustration).
    - Main Actions: Open saved stories, remove bookmarks.
    - Delight Factor: *Swipe-to-Delete with Haptic Snap* — removing a bookmark feels tactile and satisfying.

# Design Directions

### Option 1 — "Dark Mode Developer IDE" (Recommended)
This style mimics the environment developers are most comfortable in: a sleek, high-contrast dark theme reminiscent of VS Code or Android Studio, but refined for content reading.
- **Color Palette**: 
  - Background: #1E1E1E (Deep Charcoal)
  - Surface/Cards: #252526 (Slightly lighter charcoal)
  - Primary Accent: #007ACC (IDE Blue) or #4EC9B0 (Mint Green for growth metrics)
  - Text: #D4D4D4 (Off-white), #858585 (Subdued gray)
- **Typography**: 
  - Headings: Inter or SF Pro (Clean, geometric sans-serif).
  - Body: A highly readable sans-serif, perhaps with a subtle monospaced font (like JetBrains Mono or Fira Code) used *sparingly* for tech stack tags or metrics to reinforce the developer vibe.
- **Iconography**: Minimal, geometric, line-based icons.
- **Animation Style**: Crisp and fast. *Snappy Drawer* transitions, avoiding sluggish, overly bouncy physics. 
- **Micro-interactions**: Hover/press states on cards should slightly lift the card with a crisp drop shadow and a subtle border highlight (like selecting a block of code).

### Option 2 — "Clean Tech Publication"
A bright, minimalist, "Medium-meets-Hacker-News" aesthetic focusing entirely on readability and content presentation.
- **Color Palette**:
  - Background: #FAFAFA (Off-white)
  - Surface/Cards: #FFFFFF (Pure White)
  - Primary Accent: #FF6B6B (Vibrant Coral) or #6C5CE7 (Tech Purple)
  - Text: #2D3436 (Dark Slate), #636E72 (Muted Gray)
- **Typography**: 
  - Headings: A strong serif (like Merriweather or Playfair Display) to give it a journalistic, authoritative feel.
  - Body: A clean sans-serif (like Roboto or system default) for maximum legibility.
- **Iconography**: Filled, slightly rounded icons for a friendly touch.
- **Animation Style**: Smooth, editorial flow. *Fade and Slide Up* when navigating to details.
- **Micro-interactions**: Liquid-like ripple effects on button presses. Bookmark icon performs a *Heartbeat Pop* when saved.

*(I will proceed with Option 1: "Dark Mode Developer IDE" as the primary design direction, as it perfectly targets the developer audience).*

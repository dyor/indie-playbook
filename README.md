# Indie Playbook

**Indie Playbook** is a cross-platform mobile application and resource hub designed for independent developers, software engineers, and indie hackers. It showcases the real origin stories, technical architectures, and growth playbooks behind the most successful indie apps built with modern cross-platform frameworks.

---

## 1. What This App Is About

Indie Playbook provides actionable insights and inspiration by analyzing how top indie creators built, launched, and scaled their mobile applications. Rather than vague advice, each featured app is presented as a concrete case study that bridges the gap between engineering decisions and business outcomes.

### Target Audience
- **Independent Developers & Indie Hackers**: Builders creating commercial mobile products seeking proven growth strategies, user acquisition tactics, and monetization models.
- **Cross-Platform Engineers**: Developers evaluating or building with modern frameworks—specifically **Kotlin Multiplatform (KMP)**, **Flutter**, and **React Native**.
- **Tech Enthusiasts**: Anyone curious about how solo founders and small teams build high-performing mobile businesses that reach the top charts.

### Key Content in Each Playbook
- **Origin Story**: The problem, spark of inspiration, and backstory behind why the app was created.
- **Tech Stack Breakdown**: Framework selection (KMP, Flutter, React Native), architecture, libraries, local persistence, and backend choices.
- **Growth & Marketing Playbook**: Practical steps taken to achieve the first 10,000+ downloads, App Store Optimization (ASO) strategies, viral mechanics, community launches, and paid user acquisition.
- **Business Milestones**: Verified or estimated download metrics, revenue ranges, monetization approaches, and developer details.

---

## 2. How This App Works

Indie Playbook delivers an intuitive, interactive experience designed for browsing, learning, and community contribution:

### 📱 Explore & Discover (Offline-First)
- **Curated Feed**: Scroll through featured app case studies with concise one-liners, framework tags, and milestone badges.
- **Offline-First Persistence**: The **entire catalog of stories and case studies is cached locally in Room 3**. The app reads directly from local storage so you can browse the feed, filter apps, and read full origin and growth stories even without an active internet connection.
- **Background Sync**: When connected, the app synchronizes in the background with Firebase Firestore to pull new additions and verified community edits without blocking the UI.
- **Tech Stack Filtering**: Filter apps by tech stack (All, Kotlin Multiplatform, Flutter, React Native) or store presence (Dual Store, App Store Only) to discover how your framework of choice is utilized in production.
- **In-Depth Case Studies**: Tap any app card to view the comprehensive profile, covering origin stories, technical details, reviews, and growth playbooks.

### 💡 Community-Driven Crowdsourcing & Suggestions
Indie Playbook is an active, living directory supported by the community:
- **Suggest Edits**: Spot an outdated revenue figure, a new milestone, or an updated tech stack detail? Mobile users can tap **"Suggest Edits"** directly on any app detail screen to submit corrections, notes, and updates.
- **Nominate New Indie Apps**: Found or built a standout cross-platform app? Users can submit nominations for brand-new indie apps to be added to the directory via the **"Suggest a New App"** flow.
- **Admin Review Queue**: All user contributions are submitted to Firebase Firestore under a pending moderation queue. Administrators can review, edit, approve, or reject suggestions, ensuring the playbook maintains high data quality and accuracy.

### 🔖 Offline Bookmarks
- Tap the bookmark icon on any story to save it. Bookmarks are tracked directly in the local Room database.
- Access your bookmarked playbooks instantly from the dedicated **Saved** tab, preserved across sessions and available offline.

### 📖 Methodology & Transparency
- A dedicated **Methodology** section outlines the vetting criteria, categorization rules, and data sources used to research and score featured apps.

---

## 3. Architecture & Built From Koko

Indie Playbook is built on top of **[Koko — The KMP Contest Starter Kit](https://github.com/KotlinFoundation/kmp-contest-starter-kit)** (official documentation: [kmp-contest-starter-kit-documentation](https://github.com/KotlinFoundation/kmp-contest-starter-kit-documentation)).

### What Koko Gives Us
The KMP Contest Starter Kit provides a production-grade foundation that allows developers to take an app idea and publish a polished, commercial product to **Google Play** and the **Apple App Store** (as well as Desktop and Web) using a single, unified Kotlin codebase:

- **Commercial Monetization Suite**:
  - **Subscriptions & In-App Purchases**: Seamless multiplatform billing via Adapty (default) or RevenueCat (switchable with a single Gradle property).
  - **One-Time Payments & Credit Packs**: Flexible in-app billing models for consumable/non-consumable credits.
  - **Advertisements**: Google AdMob integration supporting banner, interstitial, and rewarded ads.
  - **Dynamic Paywalls**: Configurable paywall UI supporting remote offerings, introductory trials, and A/B test setups.
- **Multiplatform Architecture**:
  - **Kotlin 2.4 + Compose Multiplatform 1.10**: 100% shared declarative UI and business logic across Android, iOS, JVM Desktop, and Web (WASM).
  - **Jetpack Navigation 3**: Declarative, type-safe navigation with automatic per-entry ViewModel lifecycle management.
  - **Local Storage (Room 3 & DataStore)**: Modern `androidx.room3` with SQLite driver abstraction (bundled SQLite on mobile/desktop, OPFS worker on web) and Jetpack DataStore Preferences.
  - **Networking & Cloud Services**: Ktor HTTP client, Firebase Authentication (Google & Apple Sign-In), Cloud Firestore, Firebase Cloud Messaging (push notifications), Crashlytics, and Remote Config.
  - **Design System**: Comprehensive library of accessible, reusable Compose components, typography, and themes.
- **Production DevOps & Tooling**:
  - **Automated CI/CD**: Pre-configured GitHub Actions workflows for linting, quality gates, and automated Play Store / App Store release publishing.
  - **Store Screenshot Generator**: Scaffolding that renders storefront screenshots across device resolutions directly from Compose `@Preview`s.
  - **Quality Gates**: Pre-configured Spotless + ktlint formatting, coroutine test harness, and Robolectric/Roborazzi screenshot tests.

---

## Repository Structure

```
├── MobileApp/         # Compose Multiplatform mobile app (Android, iOS, Web, Desktop)
│   ├── shared/        # Shared KMP library (domain models, repositories, UI screens, Room 3)
│   ├── androidApp/    # Android Application entry point (com.indieplaybook.app)
│   ├── iosApp/        # iOS Xcode wrapper and linkage packages
│   ├── desktopApp/    # JVM Desktop entry point
│   ├── webApp/        # Web (Wasm/JS) entry point
│   ├── designsystem/  # Shared design system components & themes
│   └── scripts/       # Automation scripts (generate screens, local models, store screenshots)
├── Web/               # Firebase Hosting landing page + Cloud Functions backend
├── Documentation/     # Docusaurus documentation site (git submodule: KotlinFoundation/kmp-contest-starter-kit-documentation)
├── AiGuidelines/      # AI-assisted development guidelines, PRD, and architecture patterns
├── skills/            # Agent skills (getting started, integrations, publishing, monetization)
├── .github/           # GitHub Actions CI/CD workflows (PR checks, release pipelines)
└── AGENTS.md          # Primary AI-agent context file (CLAUDE.md and GEMINI.md reference this)
```

---

## Tech Stack

| Component | Technology |
|---|---|
| **Language** | Kotlin 2.4.0 |
| **UI Framework** | Compose Multiplatform 1.10.3 |
| **Platforms** | Android, iOS, JVM Desktop, Web (WASM) |
| **Architecture** | MVI / MVVM with pure StateFlows and Repositories |
| **Dependency Injection** | Koin 4.2.1 |
| **Local Database** | Room 3.0.0-alpha06 (`androidx.room3`) with bundled SQLite |
| **Preferences** | Jetpack DataStore Preferences 1.3.0-alpha09 |
| **Networking** | Ktor Client 3.5.0 |
| **Backend & Cloud** | Firebase (Auth, Firestore, Cloud Messaging, Analytics, Crashlytics) |
| **Monetization** | Adapty / RevenueCat (Subscriptions & IAP), Google AdMob (Ads) |
| **Image Loading** | Coil 3.5.0 |
| **Permissions** | Calf 0.12.0 |
| **Logging** | Napier |

---

## Getting Started

### Prerequisites
- JDK 17 or higher (JDK 21 recommended)
- Android Studio with Android SDK configured
- Xcode (for iOS builds)
- Optional: [KDoctor](https://github.com/Kotlin/kdoctor) to verify environment setup

### Building & Running (from `MobileApp/`)

```bash
# Android debug build
./gradlew :androidApp:assembleDebug

# Run unit tests across shared code
./gradlew :shared:jvmTest :shared:testAndroidHostTest

# Format and check code style
./gradlew spotlessApply
./gradlew spotlessCheck

# Desktop application
./gradlew :desktopApp:run

# Web development server (Wasm)
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

### Store Screenshot Generation
Generate pixel-perfect App Store and Google Play screenshots directly from Compose previews:
```bash
./scripts/generate_store_screenshots.sh
```

---

## Contributing & Development

- Read [`AGENTS.md`](AGENTS.md) and [`AiGuidelines/`](AiGuidelines/) for in-depth technical patterns and architecture conventions.
- When adding new screens or database entities, use the provided scaffolding tools:
  - `./scripts/generate_screen.sh <ScreenName>`
  - `./scripts/make_local.sh <ModelName>`

---

## License

Released under the [MIT License](LICENSE) — Copyright (c) 2026 KotlinFoundation.

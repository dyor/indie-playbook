# Google Play Publication — Final Push Checklist

Track progress toward publishing **Indie Playbook** on the Google Play Store.

---

## 1. App Configuration & Legal URLs
- [x] **Privacy Policy URL**: Live and reachable at `https://koko-demo-71050.web.app/privacy-policy.html` (configured in `AppConfiguration.kt`).
- [x] **Terms & Conditions URL**: Live and reachable at `https://koko-demo-71050.web.app/terms-conditions.html` (configured in `AppConfiguration.kt`).
- [x] **Support Contact Email**: Configured in `AppConfiguration.kt` (`admin@dyor.com`).
- [x] **Version Verification**: Verified in `androidApp/build.gradle.kts` (`versionCode = 1`, `versionName = "1.0.0"`).

---

## 2. Visual & Store Graphic Assets
- [x] **App Launcher Icons**: Adaptive and mipmap icons configured in `androidApp/src/main/res/mipmap-*`.
- [x] **Store Screenshots Generated**: Staged at `distribution/store_screenshots/en/iphone_6_5/`:
  - `01-homefeed_HomeFeedStoreScreenshot_en.png` (Discover Feed, Filter Rows & Cards)
  - `02-storydetail_AppStoryDetailStoreScreenshot_en.png` (Story Detail, Metrics, Links & Badges)
  - `03-methodology_MethodologyStoreScreenshot_en.png` (Dataset Breakdown & Hierarchy)
  - `04-suggestedits_SuggestEditsStoreScreenshot_en.png` (Suggest a New App / Edits Flow)
- [x] **Store Listing Copy**: Staged at `distribution/android/playstore_metadata/listing.md` and release notes at `distribution/whatsnew/whatsnew-en-US`.

---

## 3. Release Signing Keystore & Build
- [x] **Release Keystore**: Configured at `distribution/android/keystore/keystore.jks`.
- [x] **Keystore Properties**: Configured at `distribution/android/keystore/keystore.properties`.
- [x] **Release AAB Built & Signed**: Successfully generated and signed via `./gradlew :androidApp:bundleRelease`:
  - Artifact path: `androidApp/build/outputs/bundle/release/androidApp-release.aab`
- [x] **Unit & UI Tests**: 51/51 unit & UI tests passing (`./gradlew :shared:jvmTest`).

---

## 4. Google Play Console Setup (Next Steps in Web Console)
1. **Create App**:
   - In [Google Play Console](https://play.google.com/console) → **Create App**.
   - App Name: `Indie Playbook`
   - Default language: `English (United States)`
   - Type: `App` | Price: `Free`
2. **Main Store Listing (Grow → Store presence → Main store listing)**:
   - Copy Title, Short Description, and Full Description from [`distribution/android/playstore_metadata/listing.md`](distribution/android/playstore_metadata/listing.md).
   - Upload screenshots from [`distribution/store_screenshots/en/iphone_6_5/`](distribution/store_screenshots/en/iphone_6_5/).
3. **App Content & Declarations (Policy → App content)**:
   - **Privacy Policy**: `https://koko-demo-71050.web.app/privacy-policy.html`
   - **Data Safety Form**: Account email (optional login), Crashlytics/diagnostics. No data shared with 3rd parties.
   - **Ads**: No (`Contains ads: No`).
   - **App Access**: All functionality is open (guest mode supported).
   - **Content Rating**: Complete standard IARC questionnaire (General audience / Everyone).
4. **Upload AAB to Internal Testing Track**:
   - Go to **Test and release → Testing → Internal testing** → **Create new release**.
   - Upload [`androidApp/build/outputs/bundle/release/androidApp-release.aab`](androidApp/build/outputs/bundle/release/androidApp-release.aab).
   - Add tester emails to verify on physical device.

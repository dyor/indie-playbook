# Pull Request & Release Instructions for Indie Playbook

This guide outlines internal release procedures and change verification for the standalone **Indie Playbook** repository.

> **Note:** This repository has diverged into a standalone application solely for Indie Playbook and is no longer used to submit pull requests or updates to the upstream `KotlinFoundation/kmp-contest-starter-kit`. This document is maintained for internal development, verification, and release preparation.

---

## What This Reference Solves

1. **Avoids Google Play "Broken Functionality" rejection out of the box:**
   - **The Bug:** By default, `local.properties.example` includes `ADMOB_APP_ID_ANDROID=`. When copied to `local.properties`, Gradle's `getRequiredProperty` evaluated `"" ?: defaultValue` to `""` instead of the fallback test ID. This generated an empty string resource (`<string name="admobAppId"/>`) in release builds.
   - When installed, `play-services-ads`'s automatic `MobileAdsInitProvider` ran at boot time and threw an unhandled `IllegalStateException: The Google Mobile Ads SDK was initialized without an application ID`. Google Play's automated review immediately flagged the app for **"Broken Functionality policy: App installs, but doesn't load"**.
   - **The Solution:**
     - Disable automatic `MobileAdsInitProvider` startup via `tools:node="remove"` in `androidApp/src/main/AndroidManifest.xml` (the starter kit already initializes AdMob manually via `AppInitializer` only when `IS_ADS_ENABLED` is true).
     - Fix `getRequiredProperty` in `androidApp/build.gradle.kts` and `shared/build.gradle.kts` to treat blank strings as null (`?.takeIf { it.isNotBlank() }`) so default values are never overridden by empty strings.

2. **Accelerates Google Play setup when ads are enabled:**
   - **The Problem:** The Google Play Console Data Safety questionnaire is notorious for confusing developers because "Advertising ID" is hidden under *Device or other IDs* in Step 3, not Step 2.
   - **The Solution:** Added a step-by-step walk-through to `skills/enable-ads/SKILL.md` detailing the exact answers needed for the Google Play Data Safety and Ads declarations.

---

## Step-by-Step Instructions for Changes and Release Verification

### Step 1: Create a Feature or Fix Branch

In your terminal, navigate to your repository root and create a clean branch off `main`:

```bash
git checkout main
git pull origin main
git checkout -b fix/admob-startup-crash-and-play-guide
```

### Step 2: Apply Targeted Changes

When modifying ad configuration or manifest permissions:

#### 1. `MobileApp/androidApp/src/main/AndroidManifest.xml`
Add `MobileAdsInitProvider` with `tools:node="remove"` inside `<application>`:

```xml
        <!-- Disable automatic AdMob SDK startup initialization.
             Initialization is handled manually via AppInitializer only when
             ads are enabled (FeatureFlagManager IS_ADS_ENABLED = true).
             This prevents startup crashes on launch when AdMob is not configured. -->
        <provider
            android:name="com.google.android.gms.ads.MobileAdsInitProvider"
            android:authorities="${applicationId}.mobileadsinitprovider"
            tools:node="remove" />

        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="@string/admobAppId"/>
```

#### 2. `MobileApp/androidApp/build.gradle.kts`
Update `getRequiredProperty` at the bottom of the file:

```kotlin
fun getRequiredProperty(
    key: String,
    defaultValue: String? = null,
    errorMessage: String = "Make sure you added `$key` in local.properties",
): String {
    val propertyValue: String? = gradleLocalProperties(rootDir, providers).getProperty(key)?.takeIf { it.isNotBlank() }
    if (propertyValue == null && defaultValue == null) {
        throw IllegalArgumentException(errorMessage)
    }
    return propertyValue ?: defaultValue ?: ""
}
```

#### 3. `MobileApp/shared/build.gradle.kts`
Update `getRequiredProperty` at the bottom of the file with the same check:

```kotlin
fun getRequiredProperty(
    key: String,
    defaultValue: String? = null,
    errorMessage: String = "Make sure you added `$key` in local.properties",
): String {
    val propertyValue: String? = gradleLocalProperties(rootDir, providers).getProperty(key)?.takeIf { it.isNotBlank() }
    if (propertyValue == null && defaultValue == null) {
        throw IllegalArgumentException(errorMessage)
    }
    return propertyValue ?: defaultValue ?: ""
}
```

#### 4. `skills/enable-ads/SKILL.md`
Update section `## 5. Store data-safety implications (User Action)`:

```markdown
## 5. Store data-safety implications (User Action)

AdMob collects the **advertising ID** and device/usage data. You must disclose this in store consoles:

### Google Play Console (Policy and programs → App content)

1. **Ads Declaration**:
   - Navigate to **App content → Ads**.
   - Select **"Yes, my app contains ads"** and save.

2. **Advertising ID Declaration**:
   - Navigate to **App content → Advertising ID**.
   - Select **"Yes"** (declaring your app uses the advertising ID for advertising/marketing and analytics).

3. **Data Safety Questionnaire**:
   - Navigate to **App content → Data safety**.
   - **Step 2 (Data collection and security)**:
     - *Does your app collect or share user data?* → Select **Yes**.
     - *Is all user data encrypted in transit?* → Select **Yes** (all AdMob/Firebase traffic uses HTTPS/TLS).
     - *Methods of account creation?* → Select your app's method (e.g. OAuth, or "My app does not allow users to create an account").
   - **Step 3 (Data types)**:
     - Scroll to **Device or other IDs**, expand the dropdown, and check **Device or other IDs** (AdMob collects the Advertising ID under this category).
   - **Step 4 (Data usage and handling)**:
     - Click **Device or other IDs** to configure:
       - *Collected or shared?* → Check both **Collected** and **Shared** (AdMob transfers identifiers to Google/ad partners).
       - *Processed ephemerally?* → Select **No**.
       - *Required or optional?* → Select **Data collection is required**.
       - *Purposes?* → Check **Advertising or marketing** (and **Analytics** if using Firebase Analytics).
   - **Step 5 (Preview)** → Review and **Save**.

### App Store Connect (App Privacy)

- Declare **Device ID** under Identifiers:
  - Used for: **Third-Party Advertising** and **Analytics**.
  - Linked to user / Used for tracking: Yes (if displaying personalized ads; prompt via **App Tracking Transparency** on iOS).

Skipping these declarations can cause app rejection or store policy warnings.
```

---

### Step 3: Verify the Diff and Tests

1. Check touched files:
   ```bash
   git status
   ```

2. Run quality gates to confirm everything passes:
   ```bash
   cd MobileApp
   ./gradlew :androidApp:assembleDebug
   ./gradlew :shared:jvmTest
   cd ..
   ```

### Step 4: Commit and Push to Repository

```bash
git add MobileApp/androidApp/src/main/AndroidManifest.xml \
        MobileApp/androidApp/build.gradle.kts \
        MobileApp/shared/build.gradle.kts \
        skills/enable-ads/SKILL.md

git commit -m "fix(ads): prevent startup crash when AdMob unconfigured and add Play Data Safety guide"

git push origin fix/admob-startup-crash-and-play-guide
```

### Step 5: Open Internal Pull Request

Open a pull request against the `main` branch of the `indie-playbook` repository with the title and summary below:

---

## Suggested PR Title and Description

### Title:
`fix(ads): prevent release startup crash when AdMob unconfigured & add Play Data Safety guide`

### Description:
```markdown
### Summary of Changes

1. **Fix Google Play "Broken Functionality" rejection out-of-the-box:**
   - In `local.properties.example`, `ADMOB_APP_ID_ANDROID=` is empty by default. Because `getRequiredProperty` evaluated `"" ?: defaultValue` to `""`, release builds generated an empty `<string name="admobAppId"/>`.
   - At launch, the AdMob SDK's automatic `MobileAdsInitProvider` threw an `IllegalStateException` on startup, causing Google Play automated review to reject fresh apps with:
     > *Broken Functionality policy: App installs, but doesn't load. Evidence: Invalid application ID.*
   - **Fix:**
     - Added `<provider android:name="com.google.android.gms.ads.MobileAdsInitProvider" tools:node="remove" />` to `AndroidManifest.xml`. AdMob is already initialized programmatically via `AppInitializer` only when `IS_ADS_ENABLED == true`, so removing this provider eliminates startup crashes when ads are not configured.
     - Updated `getRequiredProperty` in `androidApp/build.gradle.kts` and `shared/build.gradle.kts` with `?.takeIf { it.isNotBlank() }` so blank property values fall back to default values rather than outputting empty strings.

2. **Enhance `enable-ads` skill with Google Play Data Safety step-by-step:**
   - Added concrete click-by-click instructions in `skills/enable-ads/SKILL.md` for the Google Play Data Safety questionnaire (identifying where Advertising ID lives under *Device or other IDs*) and the App Content Ads declaration.

### Verification
- `./gradlew :androidApp:assembleDebug` passes.
- `./gradlew :shared:jvmTest` passes.
- Verified merged release manifest removes `MobileAdsInitProvider`.
```

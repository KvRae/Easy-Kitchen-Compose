# Easy-Kitchen-Compose

![Android SDK](https://img.shields.io/badge/Android%20SDK-21%2B-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-1.5.21-blue)
![Compose](https://img.shields.io/badge/Compose-1.0.0-9cf)
![Gradle](https://img.shields.io/badge/Gradle-7.0.2-yellow)
![Ktor](https://img.shields.io/badge/Ktor-1.6.2-orange)
![Room](https://img.shields.io/badge/Room-2.3.0-red)
![Koin](https://img.shields.io/badge/Koin-3.1.2-blueviolet)
![Coil](https://img.shields.io/badge/Coil-1.3.2-yellowgreen)
![Compose Navigation](https://img.shields.io/badge/Compose%20Navigation-2.4.0-ff69b4)

EasyKitchenCompose is a Jetpack Compose recipe app that makes discovering, saving, and cooking meals
delightful. It pairs a modern UI with clean architecture, remote data via Ktor, local persistence
via Room, and DI via Koin.

<div style="display: flex; flex-wrap: nowrap; gap: 12px; overflow-x: auto;">
  <img src="https://github.com/user-attachments/assets/98460667-3913-4f96-a47c-99a820f9409e" style="width: 16%;" />
  <img src="https://github.com/user-attachments/assets/88a931ec-05e6-4bb6-b8ed-3799b79f2264" style="width: 16%;" />
  <img src="https://github.com/user-attachments/assets/891b3e46-c2e3-4210-9ce9-3b7c05ab507b" style="width: 16%;" />
  <img src="https://github.com/user-attachments/assets/49209eb0-8f2d-41dd-8182-3bbd4ef5f5f0" style="width: 16%;" />
  <img src="https://github.com/user-attachments/assets/e6506860-31af-490d-b7aa-f072c74d1672" style="width: 16%;" />
  <img src="https://github.com/user-attachments/assets/5e3eb8fd-c5c5-4bd4-9e0f-e2940176065a" style="width: 16%;" />
</div>

## Distribution

Available on **Huawei AppGallery**.

<p align="center">
  <a href="https://appgallery.huawei.com/#/app/C116762135" target="_blank">
    <img 
      src="https://github.com/user-attachments/assets/50b61961-08eb-4911-92da-86b11a179fc0"
      alt="Download on Huawei AppGallery"
      width="30%"
    />
  </a>
</p>




## What you can do

- Browse and search meals by name, category, or area.
- Open rich meal details with ingredients, steps, and media.
- Save favorites locally for quick access.
- Filter content (by area/category/ingredients) and navigate to tailored lists.
- Chat assistant screen to help with cooking ideas.

## Architecture & tech

- Clean Architecture: presentation (Compose + ViewModel), domain (use cases, models, sealed
  exceptions), data (repositories, remote Ktor client, Room cache).
- Dependency Injection: Koin modules for data, domain, and presentation layers.
- Networking: Ktor client + serialization; image loading via Coil.
- Concurrency: Kotlin Coroutines/Flows.
- Navigation: Jetpack Compose Navigation; single-activity, multi-screen flow.
- Design system: Material 3-inspired theming with an orange-first palette and soft secondary
  accents.

## Project layout

```
app/
  src/main/java/com/kvrae/easykitchen/
    data/          # Remote data sources, DTOs, repositories
    domain/        # Use cases, models, sealed exceptions
    presentation/  # Compose UI, ViewModels, navigation
  src/main/res/    # Theming, drawables, strings
```

## Getting started

1) Prerequisites: Android Studio with Android SDK 21+, JDK matching your IDE toolchain, and an
   emulator/device.
2) Clone and set `local.properties` with your `sdk.dir`.
3) **Google Sign-In Setup**: Follow the instructions in `YOUR_SETUP_INFO.md` to configure Google
   authentication.
    - Get your SHA-1: Run `./get-sha1.sh`
    - Create OAuth Client in Google Cloud Console
    - Paste Client ID in `app/src/main/res/values/strings.xml` (line 32)
    - See `GOOGLE_SIGN_IN_SETUP.md` for detailed instructions
4) Sync the project in Android Studio.

Build and run from the IDE, or via CLI:

```bash
./gradlew clean assembleDebug
```

Run unit tests:

```bash
./gradlew test
```

## Google Sign-In Configuration

This app uses Google Sign-In for user authentication (mobile-side only, no backend required).

**Quick Setup**:

```bash
# 1. Get your SHA-1 fingerprint
./get-sha1.sh

# 2. Go to https://console.cloud.google.com/apis/credentials
# 3. Create OAuth 2.0 Client ID (Android)
# 4. Paste Client ID in app/src/main/res/values/strings.xml

# 5. Rebuild
./gradlew clean build
```

**Documentation**:

- **Quick Start**: `YOUR_SETUP_INFO.md` - Has your SHA-1 and quick instructions
- **Detailed Guide**: `GOOGLE_SIGN_IN_SETUP.md` - Complete step-by-step guide
- **Quick Reference**: `QUICK_SETUP_GOOGLE_AUTH.md` - 5-minute setup card

## Notes

- If you hit dependency cache issues, retry with `./gradlew --refresh-dependencies`.
- Ktor and Room components rely on proper Gradle sync; ensure network access on first setup.
- **Google Sign-In** requires proper OAuth configuration - see setup docs above.

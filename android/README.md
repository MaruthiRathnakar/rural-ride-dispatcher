# Rural Bike Pool Android Prototype

Static Android prototype built from `docs/WIREFRAMES.md`.

## Stack

- Kotlin
- Jetpack Compose
- Material 3
- Static mock data
- In-memory screen navigation

## Run

Open the `android/` folder in Android Studio, let Gradle sync, and run the `app` configuration.

CLI build:

```sh
gradle :app:assembleDebug
```

The debug APK is generated at:

```text
android/app/build/outputs/apk/debug/app-debug.apk
```

## Prototype Coverage

- Launch
- Phone login
- OTP
- Role selection
- Passenger search
- Ride results
- Ride details
- Request pending
- Confirmed trip
- Rating
- Rider home
- Rider verification
- Create ride offer
- Incoming request
- Active trip

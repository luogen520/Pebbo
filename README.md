# Pebbo

A small calculator for Android. Material 3, light + dark theme, one Activity, one layout. No ads, no analytics, no internet permission.

[![Get it on Orion Store](https://raw.githubusercontent.com/RookieEnough/Orion-Store/refs/heads/main/assets/orion-badge.png)](https://rookieenough.github.io/Orion-Data/redirect.html?id=pebbo)

## Features

- Four operations plus percent, sign toggle (±), backspace and clear
- Expression preview line above the big result
- Haptic feedback on every key press
- Auto-sizing display with a 12-digit input cap, division by zero shows Error instead of crashing
- Light and dark themes following the system setting
- State survives rotation and process death
- Minified release build, APK stays under 2 MB

## Download

Grab the latest signed APK from the [Releases](https://github.com/luogen520/Pebbo/releases) page. Requires Android 5.0 (API 21) or later. Also available on Orion Store — search for "Pebbo".

## Build

```
./gradlew assembleRelease
```

Output APK: `app/build/outputs/apk/release/app-release-unsigned.apk`

## Screenshots

| Light | Dark |
| --- | --- |
| ![light](fastlane/metadata/android/en-US/images/phoneScreenshots/1.png) | ![dark](fastlane/metadata/android/en-US/images/phoneScreenshots/2.png) |

## License

GPL-3.0-or-later. See [LICENSE](LICENSE).

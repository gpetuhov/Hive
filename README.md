# Hive
Hive - new way of communication. Beta-test link: https://play.google.com/apps/testing/com.gpetuhov.android.hive

## Platform
Android

## Requirements
* Android Studio 3.5 Canary 12
* Kotlin 1.3.30
* Android Gradle Plugin 3.5.0-alpha12
* Gradle wrapper 5.3.1
* AAPT 2

## Architecture
* Clean architecture with Domain, Presentation and UI layers
* MVP and MVVM for the Presentation layer:
    * MVP (Moxy library) for screen rotations
    * MVVM (Google ViewModel) for updating views on data changes
* Firebase Firestore for storing and sharing data
* Firebase Cloud Storage for storing media
* Firebase Cloud Functions (code in HiveCloud repo) for backend data handling
* Data Binding (in some views)
* Jetpack Navigation
* AndroidX
* AirBnb Epoxy for complex screens (with carousels)
* Dependency injection with Dagger 2
# Hive
Hive - new way of communication. Beta-test link: https://play.google.com/apps/testing/com.gpetuhov.android.hive

## Platform
Android

## Requirements
* Android Studio 3.6 Canary 1
* Kotlin 1.3.40-eap-32
* Android Gradle Plugin 3.6.0-alpha01
* Gradle wrapper 5.4.1
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

## Credits
Award Icons made by [Vectors Market](https://www.flaticon.com/authors/vectors-market) from [www.flaticon.com](https://www.flaticon.com/)

TextMaster award animation made by [Hyebin Park](https://lottiefiles.com/smoothy.co) from [LottieFiles](https://lottiefiles.com/)

Congratulation Animation made by [Jojo Lafrite](https://lottiefiles.com/jojolafrite) from [LottieFiles](https://lottiefiles.com/)

Gears Animation mady by [LottieFiles](https://lottiefiles.com/lottiefiles) from [LottieFiles](https://lottiefiles.com/)
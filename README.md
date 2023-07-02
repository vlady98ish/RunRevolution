# RunRevolution

RunRevolution is a mobile application designed to help runners track their routes and view their running history. The app is built using Kotlin and follows the MVVM architecture pattern. 

## Features

- Track your running route in real-time using GPS
- View your running history, including routes, distances, and times

## Technologies

- **Kotlin**: The project is fully written in Kotlin.
- **Room**: Used for data persistence.
- **Coroutines**: For handling asynchronous tasks.
- **MVVM Architecture**: The project follows the Model-View-ViewModel software architectural pattern.
- **Repository Pattern**: Used for abstracting the data layer.
- **Google Maps API**: For displaying and tracking running routes.
- **Dagger/Hilt or Koin**: Used for dependency injection (please replace with your specific DI library).
- **LiveData**: Used for data observation.
- **Android Jetpack**: A suite of libraries, tools, and guidance to help developers write high-quality apps easier. These libraries include Navigation, LiveData, ViewModel, etc.

## Getting Started

1. Clone the repository
```bash
git clone https://github.com/vlady98ish/RunRevolution.git
```


Open the project in Android Studio

Build and run the project on an emulator or physical device.

## App Architecture

The app is divided into three main layers:

- **Data Layer**: Handles all data operations such as database access, network requests, or any data source interactions.
- **Domain Layer**: Where business logic is handled. This is also where entities and repository interfaces are defined.
- **Presentation Layer**: Handles everything related to the UI of the app. This includes Activities, Fragments, ViewModels, and Adapters.

The app uses a `RunningService` for tracking a user's run in the background.

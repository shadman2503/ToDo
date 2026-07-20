# ToDo App

[![Download APK](https://img.shields.io/badge/DOWNLOAD-APK-green?style=for-the-badge&logo=android)](https://github.com/shadman2503/ToDo/releases/latest)

A modern, lightweight Android application built with Jetpack Compose designed to help you manage your daily tasks efficiently. This app demonstrates the implementation of the MVVM architecture, Room database for persistent storage, and Material 3 design principles.

## Features (v1.1.0 Updates)

- **Task Completion**: Mark tasks as done with a single tap. Completed tasks are visually distinguished with a strike-through title and a grayed-out effect.
- **Trash Bin**: Deleted tasks are moved to a Trash Bin. Items are automatically purged after 7 days, but can be restored or manually deleted permanently at any time.
- **Automatic List System**: Efficiently create lists in descriptions. Supports bullets (`-`, `*`, `•`) and numbered lists (`1.`) with automatic indentation, continuation on Enter, and bold formatting.
- **Improved UI Scanability**: Alternating background colors (Zebra striping) for list items and better contrast in both light and dark modes.
- **Automated Releases**: Integrated GitHub Actions for automatic APK building and versioning on every push and tag.
- **Task Management**: Easily add, edit, and soft-delete tasks.
- **Persistent Storage**: Uses Room Database (Schema v4) to ensure your tasks and their states are saved safely.
- **Theming**: Full support for Dark and Light modes with a seamless toggle.
- **Focus & Keyboard Management**: Intuitive focus clearing and automatic keyboard dismissal for a smoother workflow.

## Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Design System**: [Material 3](https://m3.material.io/)
- **Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Asynchronous Programming**: [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- **Data Observation**: LiveData
- **CI/CD**: [GitHub Actions](https://github.com/features/actions)

## Project Structure

```text
com.example.simple_todo_app
├── db/                # Database configuration, DAO, and Type Converters
├── utils/             # Utility classes (List logic, formatting)
├── MainActivity.kt    # Entry point and navigation handling
├── MainApplication.kt # Application class for database initialization
├── Todo.kt            # Room Entity representing a task
├── TodoListPage.kt    # Main task list UI
├── TrashBinPage.kt    # Trash management UI
├── TodoViewModel.kt   # Business logic and data stream handling
└── ui/theme/          # Material 3 theme definitions
```

## Getting Started

### Prerequisites

- Android Studio Ladybug | 2024.2.1 or newer
- JDK 17 or higher
- Android SDK 26+ (Android 8.0 Oreo)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/shadman2503/ToDo.git
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the app on an emulator or a physical device.

## Screenshots

### Main Interface & Completion
<p align="center">
  <img src="./screenshots/light_mode.png" width="300" alt="Light Mode" />
  <img src="./screenshots/dark_mode.png" width="300" alt="Dark Mode" />
</p>

### Task Management
<p align="center">
  <img src="./screenshots/add_task.png" width="250" alt="Add Task" />
  <img src="./screenshots/task_list.png" width="250" alt="Task List" />
  <img src="./screenshots/edit_task.png" width="250" alt="Update Task" />
</p>

## Contributing

Contributions are welcome! If you have suggestions for improvements or new features, feel free to open an issue or submit a pull request.

## Developer

**Shadman Shoumik**
- GitHub: [@shadman2503](https://github.com/shadman2503)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

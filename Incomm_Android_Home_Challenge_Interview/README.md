# Incomm Android Home Challenge

In this project, I implemented a user list feature using Clean Architecture combined with the MVVM (Model-View-ViewModel) design pattern. My goal was to build a solution that is easy to test, maintain, and scale, ensuring that the business logic is decoupled from the UI.

## Architecture Guidelines

I structured the app following Clean Architecture principles to separate responsibilities. The project is organized into three main layers:

### 1. Domain Layer (Innermost Layer)
This is where the business logic lives. It defines what the app does without caring about the UI or how data is fetched.
- **Entities**: These are pure Kotlin models, like the `User` data class.
- **Use Cases (Interactors)**: These encapsulate specific business rules.
- **Repository Interfaces**: Protocols that define what data operations are needed.
This layer has no dependencies on other layers.

### 2. Data Layer
This layer is responsible for retrieving and persisting data. It implements the protocols defined in the Domain layer.
- **Repositories**: The actual implementations of the repository interfaces.
- **Data Sources**: Objects that manage remote API calls (Retrofit) or local database access.
- **DTOs**: Data Transfer Objects used for parsing JSON responses (via Gson), which are then mapped to Domain Entities.
This layer depends only on the Domain layer.

### 3. Presentation Layer (MVVM)
This layer handles everything related to the UI and user interaction.
- **Views**: The `Activity` or `Fragment` that display data (e.g., `MainActivity`, `UserAdapter`).
- **ViewModels**: They prepare data for the View and handle user interactions by communicating with the Use Cases (or Repositories in simpler scenarios).
This layer also depends on the Domain layer.

---

## MVVM Pattern (Model-View-ViewModel)

I chose MVVM to separate the View from the business logic.

- **Model**: Represents the data (Domain Entities).
- **View**: The UI components (`MainActivity`, `RecyclerView`) that observe the ViewModel.
- **ViewModel**: Acts as the bridge. It transforms models into something the View can display and handles user input (using `LiveData` or `StateFlow`).

---

## Why I Chose This Architecture?

### 1. Separation of Concerns
Every part of the code has a clear responsibility. The UI code focuses on layout and display, while the logic code focuses on rules and data. This makes it much easier to read and navigate the codebase.

### 2. Testability
Since the business logic (Domain) serves as the core and has no UI dependencies, I can write unit tests for it easily. Similarly, ViewModels can be tested by mocking the dependencies, allowing me to verify the logic without running the app.

### 3. Scalability & Maintainability
New features can be added without breaking existing ones. The dependency rule (inner layers don't know about outer layers) prevents tight coupling, meaning changes in the database or API (Data Layer) won't break the UI or Business Logic.

## Project Structure

```
Incomm_Android_Home_Challenge_Interview/
├── domain/              # Business Logic (Entities, Interfaces)
├── data/                # Data Implementation (Repositories, DTOs, API)
├── ui/                  # UI and ViewModels (Activity, Adapter, ViewModel)
```

---

## Running Tests

This project includes Unit Tests (JUnit) and UI/Integration Tests (Espresso).

### Unit Tests
The unit tests cover:
- **Repositories**: Verifying data mapping and error handling.
- **ViewModels**: Verifying state management and LiveData updates.

To run unit tests via command line:
```bash
# If you have the wrapper
./gradlew testDebugUnitTest
# Or using system gradle
gradle testDebugUnitTest
```

### UI Tests (Espresso)
The UI tests verify the MainActivity launch and RecyclerView display.

To run UI tests via command line (requires connected device/emulator):
```bash
./gradlew connectedAndroidTest
```

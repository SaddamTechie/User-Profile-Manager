# User Profile Manager

## Overview
**User Profile Manager** is an Android application built with Jetpack Compose and Material Design 3. It allows users to create, view, edit, and delete personal profiles using a variety of UI components.

---

## Features and UI Components

### Home Screen
- **Welcome Message (`Text`)**: Displays "Welcome to Profile Manager!" centered on the screen.
- **Floating Action Button (FAB)**: A "+" button to add new profiles, visible only on the Home screen.
- **Bottom Navigation Bar**: Switches between Home and Profiles screens with "Home" and "Profiles" tabs.

### User Profile Form
- **Text Fields (`OutlinedTextField`)**: Inputs for Name, Email, Phone, and Age, with validation (e.g., Age as integer).
- **Gender Selection (`RadioButton`)**: Options for Male, Female, Other in a horizontal row.
- **Hobbies (`CheckBox`)**: Multiple-select options for Reading, Traveling, Coding.
- **Notifications (`Switch`)**: Toggle for enabling/disabling notifications.
- **Submit Button (`Button`)**: Material Design button to save the profile.

### Profile Display
- **Profile Card (`Card`)**: Displays profile details in a scrollable list using `LazyColumn`.
- **Profile Picture (`Surface`)**: Circular gray placeholder showing the user’s name initial.
- **Favorite Toggle (`IconToggleButton`)**: Toggles favorite status with heart icons.

### Edit & Delete Functionality
- **Options Dialog (`AlertDialog`)**: Triggered by an "Options" button; offers Edit (reopens form) and Delete (removes profile).
- **Confirmation (`Snackbar`)**: Shows "Profile saved" or "Profile deleted" after actions.


## Implementation Details

- **Structure**: Built using a single `Scaffold` with a `Box` for content layering. The `ProfileManagerApp` composable manages state and navigation.
- **State Management**: Uses `remember` and `mutableStateOf` for profiles, screen selection, and dialog states. Data is stored in memory (lost on app close).
- **Components**: 
  - **Home Screen**: `HomeScreen` and `HomeFab` composables.
  - **Navigation**: `BottomNavigationBar` with two items.
  - **Form**: `ProfileFormDialog` with input fields and controls.
  - **Display**: `ProfileListScreen` and `ProfileCard` for profile listing.
  - **Actions**: Dialog and Snackbar logic in `ProfileCard` and `ProfileManagerApp`.

- **Code Location**: All logic resides in `MainActivity.kt` under `com.example.profilemanager`.

---

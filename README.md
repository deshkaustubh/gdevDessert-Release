Dessert Release App - Starter Code
=================================
Starter code for the Android Basics with Compose: Dessert Release app.

Introduction
------------
The Dessert Release app is a simple app which displays a collection of Android dessert releases. 
This app is used to teach the concept of Preferences DataStore.

Pre-requisites
--------------

* Experience with Kotlin syntax.
* How to create and run a project in Android Studio.
* How to create composable functions

Getting Started
---------------

1. Install Android Studio, if you don't already have it.
2. Download the sample.
3. Import the sample into Android Studio.
4. Build and run the sample.

How to Run and Test the Dessert Release App with Preferences DataStore
---------------------------------------------------------------------

Follow these steps to set up, run, and test the Dessert Release app using Preferences DataStore:

### 1. Install Prerequisites
- Download and install [Android Studio](https://developer.android.com/studio) if you haven't already.
- Ensure you have the latest version of Android Studio and the required SDKs.

### 2. Download the Project
- Download or clone the repository containing the Dessert Release app starter code.

### 3. Import the Project
- Open Android Studio.
- Select **File > Open** and navigate to the folder where you downloaded the project.
- Select the project folder and click **OK** to open it in Android Studio.

### 4. Build the Project
- Let Android Studio sync the Gradle files. This may take a few minutes.
- If prompted, install any missing SDK components or plugins.
- Click the **Build** button or select **Build > Make Project** to compile the app.

### 5. Run the App
- Connect an Android device via USB or start an Android emulator.
- Click the **Run** button (green triangle) or select **Run > Run 'app'**.
- Choose your device or emulator and wait for the app to launch.

### 6. Test Preferences DataStore Functionality
- Interact with the app UI to change layout preferences (e.g., switch between linear and grid layouts).
- Close and reopen the app to verify that your layout preference is saved and restored using Preferences DataStore.

### 7. Troubleshooting
- If you encounter build errors, ensure all dependencies are downloaded and your Android Studio is up to date.
- Check the **Build** window for error messages and resolve any issues as indicated.
- If you see issues related to DataStore, verify that you are using the correct imports and DataStore types in your code.

### 8. Additional Resources
- [Preferences DataStore documentation](https://developer.android.com/topic/libraries/architecture/datastore)
- [Android Basics with Compose](https://developer.android.com/courses/android-basics-compose/course)

Implementing Preferences DataStore in Dessert Release App
-------------------------------------------------------

Follow these steps to implement Preferences DataStore for saving and restoring layout preferences:

### 1. Add DataStore Dependencies
- Open `app/build.gradle.kts`.
- Add the following dependencies:
  ```kotlin
  implementation("androidx.datastore:datastore-preferences:1.0.0")
  ```
- Sync your project.

### 2. Create UserPreferencesRepository
- Create a new file: `app/src/main/java/com/example/dessertrelease/data/local/UserPreferencesRepository.kt`.
- Implement a repository class to read and write preferences using DataStore:
  - Inject a `DataStore<Preferences>` instance.
  - Use `edit` to save preferences and `data.map` to observe them.

### 3. Set Up DataStore in Application Class
- In your `DessertReleaseApplication.kt`:
  - Add a `val Context.dataStore` property using `preferencesDataStore` delegate.
  - Initialize `UserPreferencesRepository` with the `dataStore` instance in `onCreate()`.

### 4. Update the ViewModel
- Inject `UserPreferencesRepository` into your ViewModel (e.g., via constructor or a ViewModelProvider.Factory).
- Expose a `Flow<Boolean>` or `LiveData<Boolean>` for the layout preference.
- Add a method to save the layout preference.

### 5. Update the UI Layer
- Collect the layout preference from the ViewModel using Compose's `collectAsState` or `observeAsState`.
- When the user changes the layout, call the ViewModel's save method.

### 6. Test the Implementation
- Run the app.
- Change the layout preference in the UI.
- Close and reopen the app to verify the preference is persisted.

### Example Code Snippets

**UserPreferencesRepository.kt**
```kotlin
class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
    }
    suspend fun saveLayoutPreferences(isLinearLayout: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LINEAR_LAYOUT] = isLinearLayout
        }
    }
    val isLinearLayout: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[IS_LINEAR_LAYOUT] ?: true }
}
```

**DessertReleaseApplication.kt**
```kotlin
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "layout_preferences")
class DessertReleaseApplication : Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository
    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}
```

**ViewModel Example**
```kotlin
class DessertViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    val isLinearLayout = userPreferencesRepository.isLinearLayout.asLiveData()
    fun saveLayoutPreference(isLinear: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreferences(isLinear)
        }
    }
}
```

**UI Example (Compose)**
```kotlin
val isLinearLayout by viewModel.isLinearLayout.observeAsState(true)
// Use isLinearLayout to control your layout
// On toggle, call viewModel.saveLayoutPreference(newValue)
```

---

These steps will help you implement Preferences DataStore in your app from scratch. Adjust package names and class names as needed for your project structure.

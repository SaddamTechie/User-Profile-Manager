package com.example.profilemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.profilemanager.ui.theme.ProfileManagerTheme
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfileManagerTheme {
                ProfileManagerApp()
            }
        }
    }
}

data class UserProfile(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val phone: String,
    val age: Int,
    val gender: String,
    val hobbies: List<String>,
    val notificationsEnabled: Boolean,
    var isFavorite: Boolean = false
)

@Composable
fun ProfileManagerApp() {
    var profiles by remember { mutableStateOf(listOf<UserProfile>()) }
    var selectedScreen by remember { mutableStateOf("home") }
    val snackbarHostState = remember { SnackbarHostState() }
    var showForm by remember { mutableStateOf(false) }
    var editingProfile by remember { mutableStateOf<UserProfile?>(null) }
    var showDeleteSnackbar by remember { mutableStateOf(false) }
    var showSaveSnackbar by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                selectedScreen = selectedScreen,
                onScreenSelected = { selectedScreen = it }
            )
        },
        floatingActionButton = {
            HomeFab(
                selectedScreen = selectedScreen,
                onFabClick = { showForm = true }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            MainContent(
                selectedScreen = selectedScreen,
                profiles = profiles,
                onEdit = { profile -> editingProfile = profile; showForm = true },
                onDelete = { profileId ->
                    profiles = profiles.filter { it.id != profileId }
                    showDeleteSnackbar = true
                },
                onToggleFavorite = { profileId ->
                    profiles = profiles.map {
                        if (it.id == profileId) it.copy(isFavorite = !it.isFavorite) else it
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (showForm) {
                ProfileFormDialog(
                    profile = editingProfile,
                    onDismiss = { showForm = false; editingProfile = null },
                    onSubmit = { profile ->
                        profiles = if (editingProfile != null) {
                            profiles.map { if (it.id == profile.id) profile else it }
                        } else {
                            profiles + profile
                        }
                        showForm = false
                        editingProfile = null
                        showSaveSnackbar = true
                    }
                )
            }

            // Handle Snackbars without LaunchedEffect
            if (showDeleteSnackbar) {
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar("Profile deleted")
                    showDeleteSnackbar = false
                }
            }
            if (showSaveSnackbar) {
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar("Profile saved")
                    showSaveSnackbar = false
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedScreen: String, onScreenSelected: (String) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedScreen == "home",
            onClick = { onScreenSelected("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedScreen == "profiles",
            onClick = { onScreenSelected("profiles") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profiles") },
            label = { Text("Profiles") }
        )
    }
}

@Composable
fun HomeFab(selectedScreen: String, onFabClick: () -> Unit) {
    if (selectedScreen == "home") {
        FloatingActionButton(onClick = onFabClick) {
            Text("+")
        }
    }
}

@Composable
fun MainContent(
    selectedScreen: String,
    profiles: List<UserProfile>,
    onEdit: (UserProfile) -> Unit,
    onDelete: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (selectedScreen) {
        "home" -> HomeScreen(modifier)
        "profiles" -> ProfileListScreen(
            profiles = profiles,
            onEdit = onEdit,
            onDelete = onDelete,
            onToggleFavorite = onToggleFavorite,
            modifier = modifier
        )
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Welcome to Profile Manager!",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun ProfileListScreen(
    profiles: List<UserProfile>,
    onEdit: (UserProfile) -> Unit,
    onDelete: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(profiles) { profile ->
            ProfileCard(
                profile = profile,
                onEdit = onEdit,
                onDelete = onDelete,
                onToggleFavorite = onToggleFavorite
            )
        }
    }
}

@Composable
fun ProfileCard(
    profile: UserProfile,
    onEdit: (UserProfile) -> Unit,
    onDelete: (String) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Gray,
                    modifier = Modifier.size(60.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = profile.name.first().toString(), color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Name: ${profile.name}")
                    Text(text = "Email: ${profile.email}")
                    Text(text = "Phone: ${profile.phone}")
                    Text(text = "Age: ${profile.age}")
                    Text(text = "Gender: ${profile.gender}")
                    Text(text = "Hobbies: ${profile.hobbies.joinToString()}")
                    Text(text = "Notifications: ${if (profile.notificationsEnabled) "On" else "Off"}")
                }
                IconToggleButton(
                    checked = profile.isFavorite,
                    onCheckedChange = { onToggleFavorite(profile.id) }
                ) {
                    Icon(
                        imageVector = if (profile.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                }
            }
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Options")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Profile Options") },
            text = { Text("Choose an action for ${profile.name}'s profile") },
            confirmButton = {
                TextButton(onClick = { onEdit(profile); showDialog = false }) {
                    Text("Edit")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDelete(profile.id); showDialog = false }) {
                    Text("Delete")
                }
            }
        )
    }
}

@Composable
fun ProfileFormDialog(
    profile: UserProfile?,
    onDismiss: () -> Unit,
    onSubmit: (UserProfile) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue(profile?.name ?: "")) }
    var email by remember { mutableStateOf(TextFieldValue(profile?.email ?: "")) }
    var phone by remember { mutableStateOf(TextFieldValue(profile?.phone ?: "")) }
    var age by remember { mutableStateOf(TextFieldValue(profile?.age?.toString() ?: "")) }
    var gender by remember { mutableStateOf(profile?.gender ?: "Male") }
    var hobbies by remember { mutableStateOf(profile?.hobbies ?: emptyList()) }
    var notifications by remember { mutableStateOf(profile?.notificationsEnabled ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (profile == null) "Add Profile" else "Edit Profile") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
                OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })

                Text("Gender", style = MaterialTheme.typography.bodyLarge)
                Row {
                    listOf("Male", "Female", "Other").forEach { option ->
                        Row(Modifier.padding(end = 8.dp)) {
                            RadioButton(
                                selected = gender == option,
                                onClick = { gender = option }
                            )
                            Text(option)
                        }
                    }
                }

                Text("Hobbies", style = MaterialTheme.typography.bodyLarge)
                listOf("Reading", "Traveling", "Coding").forEach { hobby ->
                    Row {
                        Checkbox(
                            checked = hobbies.contains(hobby),
                            onCheckedChange = { checked ->
                                hobbies = if (checked) hobbies + hobby else hobbies - hobby
                            }
                        )
                        Text(hobby)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Notifications")
                    Switch(
                        checked = notifications,
                        onCheckedChange = { notifications = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val ageInt = age.text.toIntOrNull() ?: return@Button
                onSubmit(
                    UserProfile(
                        id = profile?.id ?: UUID.randomUUID().toString(),
                        name = name.text,
                        email = email.text,
                        phone = phone.text,
                        age = ageInt,
                        gender = gender,
                        hobbies = hobbies,
                        notificationsEnabled = notifications
                    )
                )
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
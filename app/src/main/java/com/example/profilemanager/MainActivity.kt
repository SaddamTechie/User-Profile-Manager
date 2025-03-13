package com.example.profilemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProfileManagerApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class Profile(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val age: Int,
    val email: String
)

@Composable
fun ProfileManagerApp(modifier: Modifier = Modifier) {
    var profiles by remember { mutableStateOf(listOf<Profile>()) }
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Profile Manager",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )

        ProfileList(
            profiles = profiles,
            onDelete = { profileId ->
                profiles = profiles.filter { it.id != profileId }
            },
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Add Profile")
        }

        if (showAddDialog) {
            AddProfileDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { profile ->
                    profiles = profiles + profile
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun ProfileList(
    profiles: List<Profile>,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(profiles) { profile ->
            ProfileCard(profile = profile, onDelete = onDelete)
        }
    }
}

@Composable
fun ProfileCard(profile: Profile, onDelete: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Name: ${profile.name}")
                Text(text = "Age: ${profile.age}")
                Text(text = "Email: ${profile.email}")
            }
            Button(
                onClick = { onDelete(profile.id) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun AddProfileDialog(onDismiss: () -> Unit, onAdd: (Profile) -> Unit) {
    var name by remember { mutableStateOf(TextFieldValue()) }
    var age by remember { mutableStateOf(TextFieldValue()) }
    var email by remember { mutableStateOf(TextFieldValue()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val ageInt = age.text.toIntOrNull() ?: return@Button
                    if (name.text.isNotEmpty() && email.text.isNotEmpty()) {
                        onAdd(Profile(name = name.text, age = ageInt, email = email.text))
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
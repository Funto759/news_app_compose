package com.example.newsappfunto.ui.screens.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.newsappfunto.data.User
import com.example.newsappfunto.model.FirebaseAuthentificationViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController,scaffoldState: SnackbarHostState) {
    val signUpViewModel : FirebaseAuthentificationViewModel = hiltViewModel()
    val status by signUpViewModel.signUpStatus.collectAsStateWithLifecycle()
    val user by signUpViewModel.userStatus.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }
    var userState by remember { mutableStateOf<User?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(status) {
        when(status){
            is FirebaseAuthentificationViewModel.FirebaseViewState.UnAuthenticated ->{
                navController.navigate("LogInScreen")
            }
            is FirebaseAuthentificationViewModel.FirebaseViewState.Error -> {
                val error = (status as FirebaseAuthentificationViewModel.FirebaseViewState.Error).message
                println(error)
            }
            is FirebaseAuthentificationViewModel.FirebaseViewState.Loading -> {
                val loading = (status as FirebaseAuthentificationViewModel.FirebaseViewState.Loading).loading
                println(loading)
            }
            else -> {}
        }
    }

    LaunchedEffect(user) {
        when(user){
            is FirebaseAuthentificationViewModel.FirebaseViewState.User -> {
                val userDb = (user as FirebaseAuthentificationViewModel.FirebaseViewState.User).user
                userState = userDb
            }
            is FirebaseAuthentificationViewModel.FirebaseViewState.Error -> {
                val error = (user as FirebaseAuthentificationViewModel.FirebaseViewState.Error).message
                println(error)
            }
            else -> {}
        }
    }

    // Apply Material3 dark theme colors (could also be provided by the App's theme)
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Optional background layer card for accent (pink) – creates a layered effect
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(16.dp)
                        .offset(x = 8.dp, y = 8.dp), // offset to show behind main card
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) { /* Empty or decorative card behind main content */ }

                // Main Profile Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile picture
                        val imageModifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        // Use Coil or similar to load the image from URL
                        AsyncImage(
//                            model = user.photoUrl ?: "https://images.app.goo.gl/EUeLiKkonDJaVXU27",
                            model = "https://images.app.goo.gl/EUeLiKkonDJaVXU27",
                            contentDescription = "Profile Picture",
                            modifier = imageModifier,
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        // Name (OutlinedTextField for display only)
                        OutlinedTextField(
//                            value = user.displayName ?: "",
                            value = "${userState?.firstname.toString()} ${userState?.lastname.toString()}",
                            onValueChange = {},
                            readOnly = true,                // user cannot edit
                            label = { Text("Name") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            enabled = false  // visually disable (grays out) to emphasize read-only status
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Email (OutlinedTextField for display only)
                        OutlinedTextField(
//                            value = user.email ?: "",
                            value =  userState?.email.toString(),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Email") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            enabled = false
                        )
                        OutlinedTextField(
//                            value = user.displayName ?: "",
                            value = userState?.phoneNumber.toString(),
                            onValueChange = {},
                            readOnly = true,                // user cannot edit
                            label = { Text("Phone Number") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            enabled = false  // visually disable (grays out) to emphasize read-only status
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        // Sign Out button
                        Button(
                            onClick = { showBottomSheet = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Sign Out", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center){
                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showBottomSheet = false },
                            sheetState = sheetState,
                            tonalElevation = 8.dp,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        ) {
                            // Use a Column to stack the prompt and the buttons vertically.
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                // Prompt message in the center.
                                Text(
                                    text = "Are you sure you want to log out?",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                // First button: Delete Article.
                                Button(
                                    onClick = {
                                      signUpViewModel.signOut()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ExitToApp,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = "Log Out",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                // Second button: Cancel.
                                OutlinedButton(
                                    onClick = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.secondary
                                    )
                                ) {
                                    Text(
                                        text = "Cancel",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }

                    }
                }
            }

        }
    }


package com.example.compose_notes.ui.screens.logIn

import android.content.Intent
import android.os.Parcelable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.newsappfunto.R
import com.example.newsappfunto.model.FirebaseAuthentificationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginInput(
    val email: String = "",
    val password: String = "",
) : Parcelable

@Composable
fun LoginScreen(navController: NavController,scaffoldState: SnackbarHostState) {
    val scope = CoroutineScope(Dispatchers.IO)
    val viewModel: FirebaseAuthentificationViewModel = hiltViewModel()
//    var logInInput by rememberSaveable { mutableStateOf(LoginInput()) }
    val context = LocalContext.current

    val status by viewModel.signUpStatus.collectAsStateWithLifecycle()

    LaunchedEffect(status) {
        when(status){
            is FirebaseAuthentificationViewModel.FirebaseViewState.Authenticated -> {
                navController.navigate("CharactersScreen")
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




    // Launcher to pick an image from the gallery.
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri->
        if (uri != null){

            try {
                context.contentResolver.takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException){

            }
//            scope.launch {
//                val imagePath = saveImageToInternalStorage(context, uri)
//                logInInput = logInInput.copy(imageUri = imagePath ?: "")
//            }



        }

//        uri?.let {
//            // Save the selected image's URI in noteInput.
//            noteInput = noteInput.copy(imageUri = it.toString())
//        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo placeholder
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(painter = painterResource(R.drawable.news_broadcast_svgrepo_com), contentDescription = null)
                Spacer(Modifier.height(10.dp))

            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Welcome Back",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    var input by rememberSaveable { mutableStateOf(LoginInput()) }
                    var passwordVisible by rememberSaveable { mutableStateOf(false) }

                    OutlinedTextField(
                        value = input.email,
                        onValueChange = { input = input.copy(email = it) },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(
                               imageVector = Icons.Default.Email,
                                contentDescription = null
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            cursorColor = MaterialTheme.colorScheme.primary,
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = input.password,
                        onValueChange = { input = input.copy(password = it) },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                               imageVector = Icons.Default.Lock,
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            val icon = if (passwordVisible)
                                Icons.Default.Visibility
                            else Icons.Default.VisibilityOff
                            IconButton(onClick = {passwordVisible = !passwordVisible})  {
                                Icon(imageVector = icon, contentDescription = null)
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            cursorColor = MaterialTheme.colorScheme.primary,
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.login(input.email.removeWhitespace(), input.password.removeWhitespace()) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Login",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = { navController.navigate("SignUpScreen") },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Don't have an account? Sign Up")
                    }
                }
            }
        }
    }
}




@Composable
fun EditBox(
    value: String,
    modifier: Modifier,
    hint: String,
    onQuery: (String) -> Unit
) {
    var query by rememberSaveable { mutableStateOf(value) }
    TextField(
        value = query,
        onValueChange = {
            query = it
            onQuery(it)
        },
        placeholder = { Text(text = hint) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.TextFields,
                contentDescription = "Text Field Icon"
            )
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

fun String.removeWhitespace(): String =
    replace("\\s+".toRegex(), "")

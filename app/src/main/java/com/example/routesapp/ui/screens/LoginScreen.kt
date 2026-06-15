package com.example.routesapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.ui.viewmodels.AuthViewModel
import androidx.compose.runtime.collectAsState
import com.example.routesapp.data.SessionState

@Preview(showBackground = true)
@Composable
fun LoginScreen(onSuccess: () -> Unit,
                modifier: Modifier = Modifier,
                viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    ) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text("Username:",
            style = MaterialTheme.typography.bodySmall
        )
        OutlinedTextField(
            viewModel.usernameFieldState,
            modifier = Modifier.fillMaxWidth()
        )
        Text("Password:",
            style = MaterialTheme.typography.bodySmall
        )
        OutlinedSecureTextField(
            viewModel.passwordFieldState,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(viewModel.rememberMe.collectAsState().value,
                    { value -> viewModel.setRememberMe(value) }
                )
                Text("Remember me",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Button({ viewModel.attemptLogin() }) { Text("Log in") }
        }
        when (viewModel.loginSuccessful.collectAsState().value) {
            false -> {
                Text("Błędny login lub hasło",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error)
            }
            true -> {
                val token = (viewModel.sessionState.collectAsState().value as? SessionState.LoggedIn)?.token
                Text("Zalogowano pomyślnie. Token: $token",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                onSuccess()
            }
            null -> { }
        }
    }
}
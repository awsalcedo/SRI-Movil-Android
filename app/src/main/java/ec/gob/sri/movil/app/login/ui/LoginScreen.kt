package ec.gob.sri.movil.app.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.gob.sri.movil.app.login.components.SriButton
import ec.gob.sri.movil.app.login.components.SriLink
import ec.gob.sri.movil.app.login.components.SriTextField

@Composable
fun LoginScreen() {
    var userText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .clip(
                    RoundedCornerShape(
                        topStart = 15.dp,
                        topEnd = 15.dp
                    )
                )
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(
                    horizontal = 16.dp,
                    vertical = 24.dp
                )
                .consumeWindowInsets(WindowInsets.navigationBars),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            LoginHeaderSection(
                modifier = Modifier.fillMaxWidth()
            )
            LoginFormSection(
                userText = userText,
                onEmailTextChange = { userText = it },
                passwordText = passwordText,
                onPasswordTextChange = { passwordText = it },
                focusManager = focusManager,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun LoginHeaderSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        //Image(painter = painterResource(R.drawable.logo), contentDescription = "Logo SRI")
        Text(
            text = "SRI Movil",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun LoginFormSection(
    userText: String,
    onEmailTextChange: (String) -> Unit,
    passwordText: String,
    onPasswordTextChange: (String) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SriTextField(
            text = userText,
            onValueChange = onEmailTextChange,
            label = "Ruc / C.I. / Pasaporte",
            hint = "1712345678001 - 1712345678",
            isInputSecret = false,
            keyboardActions = KeyboardActions(onAny = {
                focusManager.moveFocus(FocusDirection.Next)
            }),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        SriTextField(
            text = passwordText,
            onValueChange = onPasswordTextChange,
            label = "Clave",
            hint = "Clave",
            isInputSecret = true,
            keyboardActions = KeyboardActions(onAny = {
                focusManager.clearFocus()
                //onLogin()
            }),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        SriButton(
            text = "Ingresar",
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        SriLink(
            text = "Cambiar de usuario",
            onClick = {},
            modifier = Modifier
                .align(
                    Alignment.CenterHorizontally
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginFormSectionPreview() {
    LoginFormSection(
        userText = "",
        onEmailTextChange = {},
        passwordText = "",
        focusManager = LocalFocusManager.current,
        onPasswordTextChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoginHeaderSectionPreview() {
    LoginHeaderSection()
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
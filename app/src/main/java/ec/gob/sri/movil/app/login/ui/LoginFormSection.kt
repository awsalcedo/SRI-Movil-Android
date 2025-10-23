package ec.gob.sri.movil.app.login.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.gob.sri.movil.app.login.components.SriButton
import ec.gob.sri.movil.app.login.components.SriLink
import ec.gob.sri.movil.app.login.components.SriTextField

@Composable
fun LoginFormSection(
    userText: String,
    onEmailTextChange: (String) -> Unit,
    adicionalText: String,
    onAdicionalTextChange: (String) -> Unit,
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
            label = "RUC / C.I. / Pasaporte",
            hint = "Ruc, Cédula o Pasaporte",
            isInputSecret = false,
            isLogin = true,
            keyboardActions = KeyboardActions(onAny = {
                focusManager.moveFocus(FocusDirection.Next)
            }),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(14.dp))
        SriTextField(
            text = adicionalText,
            onValueChange = onAdicionalTextChange,
            label = "C.I. adicional",
            hint = "En caso de requerir",
            isInputSecret = false,
            isLogin = true,
            keyboardActions = KeyboardActions(onAny = {
                focusManager.moveFocus(FocusDirection.Next)
            }),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(14.dp))
        SriTextField(
            text = passwordText,
            onValueChange = onPasswordTextChange,
            label = "Clave",
            hint = "",
            isInputSecret = true,
            isLogin = true,
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
        Spacer(modifier = Modifier.height(14.dp))
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
        adicionalText = "",
        onAdicionalTextChange = {},
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
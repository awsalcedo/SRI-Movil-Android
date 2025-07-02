package ec.gob.sri.movil.app.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.gob.sri.movil.app.R

@Composable
fun SriTextField(
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
    hint: String,
    isInputSecret: Boolean,
    keyboardActions: KeyboardActions = KeyboardActions(),
    modifier: Modifier = Modifier
) {
    //Para componentes autónomos donde nunca tenga un comportamiento diferente para el ícono de visibilidad,
    //Porque al final sólo queremos alternar la visibilidad cuando se presionamos en él, no es necesario usar un viewmodel
    var isPasswordVisible by remember { mutableStateOf(isInputSecret) }

    Column(
        modifier = modifier
    ) {
        /*Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )*/
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = label },
            visualTransformation = if (isPasswordVisible) {
                PasswordVisualTransformation(mask = '*')
            } else VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White.copy(alpha = 0.90f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White.copy(alpha = 0.90f),
                cursorColor = Color.White.copy(alpha = 0.90f)
            ),
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.90f)
                )
            },
            placeholder = {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.90f)
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = RoundedCornerShape(10.dp),
            trailingIcon = {
                if (isInputSecret) {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = Color.White.copy(alpha = 0.90f)
                        ),
                        onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }
                    ) {
                        when {
                            isPasswordVisible -> {
                                Icon(
                                    painter = painterResource(R.drawable.visibility_off),
                                    contentDescription = "Ocultar password"
                                )
                            }

                            !isPasswordVisible -> {
                                Icon(
                                    painter = painterResource(R.drawable.visibility),
                                    contentDescription = "Mostrar password"
                                )
                            }
                        }
                    }
                }

            },
            keyboardOptions = if (isInputSecret) {
                KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            } else {
                KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            },
            keyboardActions = keyboardActions,
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SriTextFieldPasswordPreview() {
    SriTextField("", {}, "Clave", "******", true)
}

@Preview(showBackground = true)
@Composable
fun SriTextFieldPreview() {
    SriTextField("", {}, "Ruc / C.I. / Pasaporte", "", false)
}
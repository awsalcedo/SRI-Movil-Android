package ec.gob.sri.movil.common.framework.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SriTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
    hint: String,
    isInputSecret: Boolean,
    isNumber: Boolean,
    isLogin: Boolean,
    keyboardActions: KeyboardActions = KeyboardActions(),
    // Soporte de error
    isError: Boolean = false,
    supportingText: String? = null,
    // Icono de limpiar
    showClearIcon: Boolean = false,
    onClear: (() -> Unit)? = null,
) {
    //Para componentes autónomos donde nunca tenga un comportamiento diferente para el ícono de visibilidad,
    //Porque al final sólo queremos alternar la visibilidad cuando se presionamos en él, no es necesario usar un viewmodel
    var isPasswordVisible by remember { mutableStateOf(isInputSecret) }

    Column(
        modifier = modifier
    ) {
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
            colors = if (isLogin) {
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.90f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White.copy(alpha = 0.90f),
                    cursorColor = Color.White.copy(alpha = 0.90f),
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                )
            } else {
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black.copy(alpha = 0.90f),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black.copy(alpha = 0.90f),
                    cursorColor = Color.Black.copy(alpha = 0.90f),
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                )
            },
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isLogin) Color.White.copy(alpha = 0.90f) else Color.Black.copy(alpha = 0.90f)
                )
            },
            placeholder = {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isLogin) Color.White.copy(alpha = 0.90f) else Color.Black.copy(alpha = 0.90f)
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = RoundedCornerShape(10.dp),
            trailingIcon = {
                when {
                    // Password toggle
                    isInputSecret -> {
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = Color.White.copy(alpha = 0.90f)
                            ),
                            onClick = {
                                isPasswordVisible = !isPasswordVisible
                            }
                        ) {
                            Icon(
                                imageVector = if (isPasswordVisible) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                },
                                contentDescription = if (isPasswordVisible) {
                                    "Ocultar contraseña"
                                } else {
                                    "Mostrar contraseña"
                                }
                            )
                        }
                    }
                    // Icono limpiar
                    showClearIcon && text.isNotBlank() -> {
                        IconButton(onClick = { onClear?.invoke() }) {
                            // si quieres usar tu propio ícono:
                            // Icon(painterResource(R.drawable.ic_clear), contentDescription = "Limpiar")
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar"
                            )
                        }
                    }
                }

            },
            keyboardOptions = when {
                isInputSecret -> {
                    KeyboardOptions(
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    )
                }

                isNumber -> {
                    KeyboardOptions(
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                }

                else -> {
                    KeyboardOptions(
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    )
                }
            },
            keyboardActions = keyboardActions,
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SriTextFieldPasswordPreview() {
    SriTextField(
        text = "",
        onValueChange = {},
        label = "Clave",
        hint = "******",
        isInputSecret = true,
        isNumber = true,
        isLogin = true,
        isError = false,
        supportingText = null,
        showClearIcon = false,
        onClear = {},
    )
}

@Preview(showBackground = true)
@Composable
fun SriTextFieldPreview() {
    SriTextField(
        text = "",
        onValueChange = {},
        label = "Ruc / C.I. / Pasaporte",
        hint = "",
        isInputSecret = false,
        isNumber = true,
        isLogin = false
    )
}

@Preview(showBackground = true)
@Composable
fun SriTextFieldErrorPreview() {
    SriTextField(
        text = "1234567890",
        onValueChange = {},
        label = "Ruc / C.I. / Pasaporte",
        hint = "",
        isInputSecret = false,
        isNumber = true,
        isLogin = false,
        isError = true,
        supportingText = "Error en el campo",
        showClearIcon = true,
        onClear = {}
    )
}
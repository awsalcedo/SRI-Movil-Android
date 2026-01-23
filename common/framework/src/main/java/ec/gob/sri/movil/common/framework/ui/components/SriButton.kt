package ec.gob.sri.movil.common.framework.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SriButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val finalEnabled = enabled && !isLoading

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = finalEnabled,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
            contentColor = Color.White,
            disabledContentColor = Color.White.copy(alpha = 0.85f)
        ),
        contentPadding = PaddingValues(12.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SriMarkButtonPreview() {
    SriButton(
        text = if (true) "Consultar" else "Consultando...",
        onClick = {},
        enabled = true,
        isLoading = false
    )
}

@Preview(showBackground = true)
@Composable
fun SriDisabledButtonPreview() {
    SriButton(
        text = if (false) "Ingresar" else "Consultando...",
        onClick = {},
        enabled = false,
        isLoading = false
    )
}
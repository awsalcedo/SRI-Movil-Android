package ec.gob.sri.movil.app.login.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoginHeaderSection(
    alignment: Alignment.Horizontal = Alignment.Start,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment
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
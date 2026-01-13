package ec.gob.sri.movil.app.estadotributario.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ec.gob.sri.movil.app.estadotributario.domain.models.EstadoTributarioDomain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadoTributarioDetalleScreen(estadoTributario: String) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle Estado Tributario") }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("detalle  estado tributario: $estadoTributario")
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun EstadoTributarioDetalleScreenPreview() {
    val estadoTributario = EstadoTributarioDomain(
        ruc = "1712245974001",
        razonSocial = "SALCEDO SILVA ALEX WLADIMIR",
        descripcion = "AL DIA EN SUS OBLIGACIONES",
        plazoVigenciaDoc = "0 meses",
        claseContribuyente = "Otro",
        obligacionesPendientes = emptyList()
    )
    MaterialTheme(colorScheme = lightColorScheme()) {
        EstadoTributarioDetalleScreen(estadoTributario = "AL DIA EN SUS OBLIGACIONES")
    }
}
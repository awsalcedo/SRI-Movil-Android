package ec.gob.sri.movil.feature.estadotributario.ui.detalle

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
fun EstadoTributarioDetalleScreen(
    estadoTributario: EstadoTributarioDomain
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle Estado Tributario") }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(estadoTributario.descripcion)
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun EstadoTributarioDetalleScreenPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        EstadoTributarioDetalleScreen(
            estadoTributario = EstadoTributarioDomain(
                ruc = "1234567890",
                razonSocial = "Empresa Ejemplo",
                descripcion = "Alex Salcedo",
                plazoVigenciaDoc = "2023-12-31",
                claseContribuyente = "Clase A",
                obligacionesPendientes = emptyList()
            )
        )
    }
}
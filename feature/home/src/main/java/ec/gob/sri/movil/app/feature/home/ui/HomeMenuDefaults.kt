package ec.gob.sri.movil.app.feature.home.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RequestPage
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

object HomeMenuDefaults {

    fun build(): List<HomeItemUi> = listOf(
        HomeItemUi(
            id = "estado_tributario",
            title = "Estado tributario",
            subtitle = "Consulta tu estado",
            enabled = true,
            icon = { HomeIcon(Icons.Default.RequestPage) }
        ),
        HomeItemUi(
            id = "deudas",
            title = "Deudas",
            subtitle = "Firmes y en gestión",
            enabled = true,
            icon = { HomeIcon(Icons.Default.ReceiptLong) }
        ),
        HomeItemUi(
            id = "comprobantes",
            title = "Comprobantes electrónicos",
            subtitle = "Consulta y descarga",
            enabled = true,
            icon = { HomeIcon(Icons.Default.Description) }
        ),
        HomeItemUi(
            id = "valores_pagar",
            title = "Valores a pagar",
            subtitle = "Obligaciones pendientes",
            enabled = true,
            icon = { HomeIcon(Icons.Default.Payments) }
        ),
        HomeItemUi(
            id = "validez",
            title = "Validación documentos físicos",
            subtitle = "Escanea y valida",
            enabled = true,
            icon = { HomeIcon(Icons.Default.QrCodeScanner) }
        ),
        HomeItemUi(
            id = "cita_previa",
            title = "Cita previa",
            subtitle = "Turnos en línea",
            enabled = true,
            icon = { HomeIcon(Icons.Default.RequestPage) }
        ),
    )
}

@Composable
private fun HomeIcon(image: androidx.compose.ui.graphics.vector.ImageVector) {
    Icon(imageVector = image, contentDescription = null)
}
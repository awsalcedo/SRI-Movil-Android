package ec.gob.sri.movil.app.feature.home.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.FileDownloadDone
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.RequestPage
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

object HomeMenuDefaults {

    fun build(): List<HomeItemUi> = listOf(
        HomeItemUi(
            id = "comprobantes",
            title = "Comprobantes electrónicos",
            subtitle = "Consulta y descarga",
            enabled = true,
            icon = { HomeIcon(Icons.Default.Description) }
        ),
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
            icon = { HomeIcon(Icons.Default.Money) }
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
            subtitle = "Verifica si el documento físico es válido para su emisión y/o recepción",
            enabled = true,
            icon = { HomeIcon(Icons.Default.QrCodeScanner) }
        ),
        HomeItemUi(
            id = "renta",
            title = "Impuesto a la Renta Causado",
            subtitle = "Conozca un detalle histórico de los valores de Impuesto a la Renta Causado, ISD y otros regímenes",
            enabled = true,
            icon = { HomeIcon(Icons.Default.Money) }
        ),
        HomeItemUi(
            id = "certificados",
            title = "Certificados",
            subtitle = "Validación de certificados y declaraciones emitidos en línea",
            enabled = true,
            icon = { HomeIcon(Icons.Default.FileDownloadDone) }
        ),
        HomeItemUi(
            id = "tramites",
            title = "Seguimiento de trámites",
            subtitle = "Información sobre proceso de trámites",
            enabled = true,
            icon = { HomeIcon(Icons.Default.Archive) }
        ),
        HomeItemUi(
            id = "qr",
            title = "Validación códigos QR",
            subtitle = "Escanea y valida",
            enabled = true,
            icon = { HomeIcon(Icons.Default.Archive) }
        ),
        HomeItemUi(
            id = "cita_previa",
            title = "Cita previa",
            subtitle = "Turnos en línea",
            enabled = true,
            icon = { HomeIcon(Icons.Default.RequestPage) }
        ),
        HomeItemUi(
            id = "calculadoras",
            title = "Calculadoras",
            subtitle = "Escanea y valida",
            enabled = true,
            icon = { HomeIcon(Icons.Default.Calculate) }
        ),
        HomeItemUi(
            id = "denuncias",
            title = "Denuncias",
            subtitle = "Denuncias Adiminstrativas y Tributarias",
            enabled = true,
            icon = { HomeIcon(Icons.Default.RequestPage) }
        ),
        HomeItemUi(
            id = "contactos",
            title = "Contáctenos",
            subtitle = "Su opinión nos interesa",
            enabled = true,
            icon = { HomeIcon(Icons.Default.Contacts) }
        ),
        HomeItemUi(
            id = "simar",
            title = "SIMAR",
            subtitle = "Comprobar la legalidad de las precintas fiscales del SRI",
            enabled = true,
            icon = { HomeIcon(Icons.Default.Water) }
        ),
        HomeItemUi(
            id = "facturador",
            title = "Facturador SRI",
            subtitle = "Fácil, seguro y sin costo",
            enabled = true,
            icon = { HomeIcon(Icons.Default.DevicesOther) }
        ),
        HomeItemUi(
            id = "configuracion",
            title = "Configuración",
            subtitle = "Preferencias",
            enabled = true,
            icon = { HomeIcon(Icons.Default.Settings) }
        )
    )
}

@Composable
private fun HomeIcon(image: androidx.compose.ui.graphics.vector.ImageVector) {
    Icon(
        imageVector = image,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primaryContainer
    )
}
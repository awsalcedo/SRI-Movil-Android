package ec.gob.sri.movil.common.framework.ui.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import ec.gob.sri.movil.common.framework.ui.components.SriBottomNavItem

object SriTopLevelNav {
    val items: List<SriBottomNavItem> = listOf(
        SriBottomNavItem("home", "Home", Icons.Default.Home, "Home"),
        SriBottomNavItem("noticias", "Noticias", Icons.AutoMirrored.Filled.Article, "Noticias"),
        SriBottomNavItem("agencias", "Agencias", Icons.Default.LocationOn, "Agencias"),
        SriBottomNavItem("login", "Login", Icons.Default.AccountCircle, "Login"),
    )
}
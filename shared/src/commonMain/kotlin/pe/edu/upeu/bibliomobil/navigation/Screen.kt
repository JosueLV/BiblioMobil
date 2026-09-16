package pe.edu.upeu.bibliomobil.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val titulo: String, val icono: ImageVector) {
    data object Inicio : Screen("Inicio", Icons.Default.LocalLibrary)
    data object Libros : Screen("Libros", Icons.Default.MenuBook)
    data object Lectores : Screen("Lectores", Icons.Default.Person)
    data object Prestamos : Screen("Préstamos", Icons.Default.Bookmark)
}

val DESTINOS: List<Screen> = listOf(
    Screen.Inicio,
    Screen.Libros,
    Screen.Lectores,
    Screen.Prestamos
)

private fun Screen.aClave(): String = when (this) {
    Screen.Inicio -> "inicio"
    Screen.Libros -> "libros"
    Screen.Lectores -> "lectores"
    Screen.Prestamos -> "prestamos"
}

private fun claveAScreen(clave: String): Screen = when (clave) {
    "libros" -> Screen.Libros
    "lectores" -> Screen.Lectores
    "prestamos" -> Screen.Prestamos
    else -> Screen.Inicio
}

val ScreenSaver: Saver<Screen, String> = Saver(
    save = { it.aClave() },
    restore = { claveAScreen(it) }
)
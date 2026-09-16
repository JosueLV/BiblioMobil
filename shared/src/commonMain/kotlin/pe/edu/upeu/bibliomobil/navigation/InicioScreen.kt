package pe.edu.upeu.bibliomobil.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

private data class AccesoRapido(
    val titulo: String,
    val icono: ImageVector,
    val destino: Screen
)

private val ACCESOS_RAPIDOS = listOf(
    AccesoRapido("Registrar libros", Icons.Default.MenuBook, Screen.Libros),
    AccesoRapido("Registrar lectores", Icons.Default.Person, Screen.Lectores),
    AccesoRapido("Revisar préstamos", Icons.Default.Bookmark, Screen.Prestamos)
)

@Composable
fun InicioScreen(onNavegar: (Screen) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.LocalLibrary,
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Text("BiblioMobil", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Tu biblioteca, siempre a la mano",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text("Qué puedes hacer", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(ACCESOS_RAPIDOS) { acceso ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavegar(acceso.destino) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(imageVector = acceso.icono, contentDescription = null)
                        Text(acceso.titulo, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.components.MensajeExito
import pe.edu.upeu.bibliomobil.presentation.components.ValidatedTextField

@Composable
fun LibroScreen(
    viewModel: LibroViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ValidatedTextField(
                    value = uiState.formulario.titulo,
                    onValueChange = viewModel::onTituloChange,
                    label = "Título",
                    error = uiState.formulario.errorTitulo
                )
                ValidatedTextField(
                    value = uiState.formulario.autor,
                    onValueChange = viewModel::onAutorChange,
                    label = "Autor",
                    error = uiState.formulario.errorAutor
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ValidatedTextField(
                        value = uiState.formulario.anio,
                        onValueChange = viewModel::onAnioChange,
                        label = "Año",
                        error = uiState.formulario.errorAnio,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                    ValidatedTextField(
                        value = uiState.formulario.ejemplares,
                        onValueChange = viewModel::onEjemplaresChange,
                        label = "Ejemplares",
                        error = uiState.formulario.errorEjemplares,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }
                Button(
                    onClick = viewModel::registrar,
                    enabled = !uiState.registrando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (uiState.registrando) "Registrando…" else "Registrar")
                }
                uiState.mensajeExito?.let { MensajeExito(it) }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val conteo = when (val fase = uiState.fase) {
            is FaseLibros.ConLibros -> if (fase.libros.size == 1) "1 libro" else "${fase.libros.size} libros"
            else -> null
        }
        conteo?.let { Text(it, style = MaterialTheme.typography.titleMedium) }

        Spacer(modifier = Modifier.height(8.dp))

        when (val fase = uiState.fase) {
            is FaseLibros.Cargando -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Text("Cargando…")
                }
            }
            is FaseLibros.SinLibros -> EstadoVacio(
                icono = Icons.Default.MenuBook,
                titulo = "Sin libros",
                descripcion = "Aún no hay libros registrados en el catálogo"
            )
            is FaseLibros.ConLibros -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(fase.libros) { libro -> LibroItem(libro) }
            }
            is FaseLibros.Error -> EstadoVacio(
                icono = Icons.Default.MenuBook,
                titulo = fase.mensaje,
                descripcion = "Inténtalo nuevamente",
                esError = true,
                accion = { TextButton(onClick = viewModel::cargarLibros) { Text("Reintentar") } }
            )
        }
    }
}

@Composable
private fun LibroItem(libro: LibroUi) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(libro.titulo, style = MaterialTheme.typography.titleMedium)
            Text(libro.autor, style = MaterialTheme.typography.bodyMedium)
            Text(libro.lineaSecundaria, style = MaterialTheme.typography.bodySmall)
            if (libro.requiereReposicion) {
                AssistChip(onClick = {}, label = { Text("Pocos ejemplares") })
            }
        }
    }
}
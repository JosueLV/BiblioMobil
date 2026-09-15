package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
fun LectorScreen(
    viewModel: LectorViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ValidatedTextField(
                    value = uiState.formulario.nombre,
                    onValueChange = viewModel::onNombreChange,
                    label = "Nombre",
                    error = uiState.formulario.errorNombre
                )
                ValidatedTextField(
                    value = uiState.formulario.correo,
                    onValueChange = viewModel::onCorreoChange,
                    label = "Correo",
                    error = uiState.formulario.errorCorreo,
                    keyboardType = KeyboardType.Email
                )
                ValidatedTextField(
                    value = uiState.formulario.telefono,
                    onValueChange = viewModel::onTelefonoChange,
                    label = "Teléfono (opcional)",
                    error = uiState.formulario.errorTelefono,
                    keyboardType = KeyboardType.Phone
                )
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
            is FaseLectores.ConLectores ->
                if (fase.lectores.size == 1) "1 lector" else "${fase.lectores.size} lectores"
            else -> null
        }
        conteo?.let { Text(it, style = MaterialTheme.typography.titleMedium) }

        Spacer(modifier = Modifier.height(8.dp))

        when (val fase = uiState.fase) {
            is FaseLectores.Cargando -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Text("Cargando…")
                }
            }
            is FaseLectores.SinLectores -> EstadoVacio(
                icono = Icons.Default.Person,
                titulo = "Sin lectores",
                descripcion = "Aún no hay lectores registrados en la cartera"
            )
            is FaseLectores.ConLectores -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(fase.lectores) { lector -> LectorItem(lector) }
            }
            is FaseLectores.Error -> EstadoVacio(
                icono = Icons.Default.Person,
                titulo = fase.mensaje,
                descripcion = "Inténtalo nuevamente",
                esError = true,
                accion = { TextButton(onClick = viewModel::cargarLectores) { Text("Reintentar") } }
            )
        }
    }
}

@Composable
private fun LectorItem(lector: LectorUi) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(lector.nombre, style = MaterialTheme.typography.titleMedium)
            Text(lector.correo, style = MaterialTheme.typography.bodyMedium)
            Text(lector.telefonoMostrado, style = MaterialTheme.typography.bodySmall)
        }
    }
}
package pe.edu.upeu.bibliomobil

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import pe.edu.upeu.bibliomobil.navigation.DESTINOS
import pe.edu.upeu.bibliomobil.navigation.Screen
import pe.edu.upeu.bibliomobil.navigation.ScreenSaver
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.inicio.InicioScreen
import pe.edu.upeu.bibliomobil.presentation.lector.LectorScreen
import pe.edu.upeu.bibliomobil.presentation.libro.LibroScreen
import pe.edu.upeu.bibliomobil.theme.BiblioMobilTheme

@Composable
fun App() {
    KoinContext {
        var modoOscuro by rememberSaveable { mutableStateOf(false) }

        BiblioMobilTheme(darkTheme = modoOscuro) {
            var pantallaActual: Screen by rememberSaveable(stateSaver = ScreenSaver) {
                mutableStateOf(Screen.Inicio)
            }
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Icon(imageVector = Icons.Default.LocalLibrary, contentDescription = null)
                            Text("BiblioMobil", style = MaterialTheme.typography.titleLarge)
                        }
                        HorizontalDivider()
                        DESTINOS.forEach { destino ->
                            NavigationDrawerItem(
                                label = { Text(destino.titulo) },
                                icon = { Icon(destino.icono, contentDescription = null) },
                                selected = destino == pantallaActual,
                                onClick = {
                                    pantallaActual = destino
                                    scope.launch { drawerState.close() }
                                },
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        HorizontalDivider()
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Modo oscuro")
                            Switch(checked = modoOscuro, onCheckedChange = { modoOscuro = it })
                        }
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(pantallaActual.titulo) },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.LocalLibrary, contentDescription = "Menú")
                                }
                            }
                        )
                    }
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        when (pantallaActual) {
                            Screen.Inicio -> InicioScreen(onNavegar = { pantallaActual = it })
                            Screen.Libros -> LibroScreen()
                            Screen.Lectores -> LectorScreen()
                            Screen.Prestamos -> EstadoVacio(
                                icono = Icons.Default.Bookmark,
                                titulo = "Préstamos en construcción",
                                descripcion = "Este módulo estará disponible en una próxima versión"
                            )
                        }
                    }
                }
            }
        }
    }
}
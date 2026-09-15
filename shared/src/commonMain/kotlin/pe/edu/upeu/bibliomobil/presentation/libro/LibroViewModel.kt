package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.LibroInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase

class LibroViewModel(
    private val registrarLibro: RegistrarLibroUseCase,
    private val listarLibros: ListarLibrosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibroUiState())
    val uiState: StateFlow<LibroUiState> = _uiState.asStateFlow()

    init {
        cargarLibros()
    }

    fun cargarLibros() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = FaseLibros.Cargando) }
            listarLibros()
                .onSuccess { libros ->
                    _uiState.update {
                        it.copy(
                            fase = if (libros.isEmpty()) {
                                FaseLibros.SinLibros
                            } else {
                                FaseLibros.ConLibros(libros.map { libro -> libro.aUi() })
                            }
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(fase = FaseLibros.Error("No se pudo cargar el catálogo"))
                    }
                }
        }
    }

    fun onTituloChange(valor: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(titulo = valor, errorTitulo = null))
        }
    }

    fun onAutorChange(valor: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(autor = valor, errorAutor = null))
        }
    }

    fun onAnioChange(valor: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(anio = valor, errorAnio = null))
        }
    }

    fun onEjemplaresChange(valor: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(ejemplares = valor, errorEjemplares = null))
        }
    }

    fun registrar() {
        val estadoActual = _uiState.value
        if (estadoActual.registrando) return // evita doble registro con doble toque

        viewModelScope.launch {
            _uiState.update { it.copy(registrando = true) }
            val formulario = estadoActual.formulario

            registrarLibro(
                titulo = formulario.titulo,
                autor = formulario.autor,
                anio = formulario.anio,
                ejemplares = formulario.ejemplares
            ).onSuccess { libro ->
                _uiState.update {
                    it.copy(
                        registrando = false,
                        formulario = FormularioLibro(),
                        mensajeExito = "Libro \"${libro.titulo}\" registrado correctamente"
                    )
                }
                cargarLibros()
            }.onFailure { error ->
                if (error is LibroInvalidoException) {
                    _uiState.update {
                        it.copy(
                            registrando = false,
                            formulario = formulario.copy(
                                errorTitulo = error.errores.titulo,
                                errorAutor = error.errores.autor,
                                errorAnio = error.errores.anio,
                                errorEjemplares = error.errores.ejemplares
                            )
                        )
                    }
                } else {
                    _uiState.update { it.copy(registrando = false) }
                }
            }
        }
    }

    fun limpiarMensajeExito() {
        _uiState.update { it.copy(mensajeExito = null) }
    }
}
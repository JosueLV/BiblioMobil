package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LectorInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase

class LectorViewModel(
    private val registrarLector: RegistrarLectorUseCase,
    private val listarLectores: ListarLectoresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LectorUiState())
    val uiState: StateFlow<LectorUiState> = _uiState.asStateFlow()

    init {
        cargarLectores()
    }

    fun cargarLectores() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = FaseLectores.Cargando) }
            listarLectores()
                .onSuccess { lectores ->
                    _uiState.update {
                        it.copy(
                            fase = if (lectores.isEmpty()) {
                                FaseLectores.SinLectores
                            } else {
                                FaseLectores.ConLectores(lectores.map { lector -> lector.aUi() })
                            }
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(fase = FaseLectores.Error("No se pudo cargar la cartera de lectores"))
                    }
                }
        }
    }

    fun onNombreChange(valor: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(nombre = valor, errorNombre = null))
        }
    }

    fun onCorreoChange(valor: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(correo = valor, errorCorreo = null))
        }
    }

    fun onTelefonoChange(valor: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(telefono = valor, errorTelefono = null))
        }
    }

    fun registrar() {
        val estadoActual = _uiState.value
        if (estadoActual.registrando) return

        viewModelScope.launch {
            _uiState.update { it.copy(registrando = true) }
            val formulario = estadoActual.formulario

            registrarLector(
                nombre = formulario.nombre,
                correo = formulario.correo,
                telefono = formulario.telefono
            ).onSuccess { lector ->
                _uiState.update {
                    it.copy(
                        registrando = false,
                        formulario = FormularioLector(),
                        mensajeExito = "Lector \"${lector.nombre}\" registrado correctamente"
                    )
                }
                cargarLectores()
            }.onFailure { error ->
                if (error is LectorInvalidoException) {
                    _uiState.update {
                        it.copy(
                            registrando = false,
                            formulario = formulario.copy(
                                errorNombre = error.errores.nombre,
                                errorCorreo = error.errores.correo,
                                errorTelefono = error.errores.telefono
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
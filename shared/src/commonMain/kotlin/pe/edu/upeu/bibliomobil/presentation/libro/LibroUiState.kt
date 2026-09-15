package pe.edu.upeu.bibliomobil.presentation.libro

sealed interface FaseLibros {
    data object Cargando : FaseLibros
    data object SinLibros : FaseLibros
    data class ConLibros(val libros: List<LibroUi>) : FaseLibros
    data class Error(val mensaje: String) : FaseLibros
}

data class LibroUiState(
    val fase: FaseLibros = FaseLibros.Cargando,
    val formulario: FormularioLibro = FormularioLibro(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
)
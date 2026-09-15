package pe.edu.upeu.bibliomobil.presentation.lector

sealed interface FaseLectores {
    data object Cargando : FaseLectores
    data object SinLectores : FaseLectores
    data class ConLectores(val lectores: List<LectorUi>) : FaseLectores
    data class Error(val mensaje: String) : FaseLectores
}

data class LectorUiState(
    val fase: FaseLectores = FaseLectores.Cargando,
    val formulario: FormularioLector = FormularioLector(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
)
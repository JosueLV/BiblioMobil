package pe.edu.upeu.bibliomobil.domain.model

data class Lector(
    val id: Long,
    val nombre: String,
    val correo: String,
    val telefono: String?
) {
    init {
        require(nombre.isNotBlank()) { "El nombre es obligatorio" }
        require(correo.isNotBlank()) { "El correo es obligatorio" }
        require(FORMATO_CORREO.matches(correo)) { "El correo no tiene un formato válido" }
        require(telefono == null || telefono.isNotBlank()) {
            "El teléfono no puede estar vacío si se proporciona"
        }
    }

    companion object {
        val FORMATO_CORREO = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

data class ErroresDeLector(
    val nombre: String? = null,
    val correo: String? = null,
    val telefono: String? = null
) {
    val tieneErrores: Boolean
        get() = nombre != null || correo != null || telefono != null
}

class LectorInvalidoException(val errores: ErroresDeLector) : Exception()

class RegistrarLectorUseCase(private val repository: LectorRepository) {

    suspend operator fun invoke(
        nombre: String,
        correo: String,
        telefono: String
    ): Result<Lector> = resultadoDe {
        val nombreRecortado = nombre.trim()
        val correoRecortado = correo.trim()
        val telefonoNormalizado: String? = telefono.trim().ifBlank { null }

        var errorNombre: String? = null
        var errorCorreo: String? = null
        var errorTelefono: String? = null

        if (nombreRecortado.isBlank()) {
            errorNombre = "El nombre es obligatorio"
        }

        if (correoRecortado.isBlank()) {
            errorCorreo = "El correo es obligatorio"
        } else if (!Lector.FORMATO_CORREO.matches(correoRecortado)) {
            errorCorreo = "El correo no tiene un formato válido"
        }

        if (telefonoNormalizado != null && telefonoNormalizado.length !in 6..9) {
            errorTelefono = "El teléfono debe tener entre 6 y 9 dígitos"
        }

        val errores = ErroresDeLector(errorNombre, errorCorreo, errorTelefono)
        if (errores.tieneErrores) {
            throw LectorInvalidoException(errores)
        }

        val lector = Lector(
            id = 0L,
            nombre = nombreRecortado,
            correo = correoRecortado,
            telefono = telefonoNormalizado
        )
        repository.registrar(lector)
    }
}
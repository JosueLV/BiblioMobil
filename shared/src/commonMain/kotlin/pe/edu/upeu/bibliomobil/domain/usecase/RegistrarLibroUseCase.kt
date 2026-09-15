package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

data class ErroresDeLibro(
    val titulo: String? = null,
    val autor: String? = null,
    val anio: String? = null,
    val ejemplares: String? = null
) {
    val tieneErrores: Boolean
        get() = titulo != null || autor != null || anio != null || ejemplares != null
}

class LibroInvalidoException(val errores: ErroresDeLibro) : Exception()

class RegistrarLibroUseCase(private val repository: LibroRepository) {

    suspend operator fun invoke(
        titulo: String,
        autor: String,
        anio: String,
        ejemplares: String
    ): Result<Libro> = resultadoDe {
        val tituloRecortado = titulo.trim()
        val autorRecortado = autor.trim()

        var errorTitulo: String? = null
        var errorAutor: String? = null
        var errorAnio: String? = null
        var errorEjemplares: String? = null

        if (tituloRecortado.isBlank()) {
            errorTitulo = "El título es obligatorio"
        }
        if (autorRecortado.isBlank()) {
            errorAutor = "El autor es obligatorio"
        }

        val anioInt: Int? = when {
            anio.isBlank() -> {
                errorAnio = "El año es obligatorio"
                null
            }
            anio.toIntOrNull() == null -> {
                errorAnio = "El año debe ser un número entero"
                null
            }
            anio.toInt() !in Libro.ANIO_MINIMO..Libro.ANIO_MAXIMO -> {
                errorAnio = "El año debe estar entre ${Libro.ANIO_MINIMO} y ${Libro.ANIO_MAXIMO}"
                null
            }
            else -> anio.toInt()
        }

        val ejemplaresInt: Int? = when {
            ejemplares.isBlank() -> {
                errorEjemplares = "Los ejemplares son obligatorios"
                null
            }
            ejemplares.toIntOrNull() == null -> {
                errorEjemplares = "Los ejemplares deben ser un número entero"
                null
            }
            ejemplares.toInt() < 0 -> {
                errorEjemplares = "Los ejemplares no pueden ser negativos"
                null
            }
            else -> ejemplares.toInt()
        }

        val errores = ErroresDeLibro(errorTitulo, errorAutor, errorAnio, errorEjemplares)
        if (errores.tieneErrores) {
            throw LibroInvalidoException(errores)
        }

        val libro = Libro(
            id = 0L,
            titulo = tituloRecortado,
            autor = autorRecortado,
            anio = anioInt!!,
            ejemplares = ejemplaresInt!!
        )
        repository.registrar(libro)
    }
}
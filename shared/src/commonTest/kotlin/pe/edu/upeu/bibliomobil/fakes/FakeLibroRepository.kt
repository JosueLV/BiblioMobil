package pe.edu.upeu.bibliomobil.fakes

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class FakeLibroRepository : LibroRepository {
    val libros = mutableListOf<Libro>()
    private var siguienteId = 1L
    var debeFallar = false

    override suspend fun registrar(libro: Libro): Libro {
        if (debeFallar) throw RuntimeException("Fallo simulado al registrar libro")
        val libroConId = libro.copy(id = siguienteId)
        siguienteId += 1
        libros.add(libroConId)
        return libroConId
    }

    override suspend fun listar(): List<Libro> {
        if (debeFallar) throw RuntimeException("Fallo simulado al listar libros")
        return libros.toList()
    }
}
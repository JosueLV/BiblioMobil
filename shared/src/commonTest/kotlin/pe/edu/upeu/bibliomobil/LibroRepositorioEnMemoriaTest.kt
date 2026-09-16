package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.data.repository.LibroRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.domain.model.Libro
import kotlin.test.Test
import kotlin.test.assertEquals

class LibroRepositorioEnMemoriaTest {

    private fun libro(titulo: String) =
        Libro(id = 0, titulo = titulo, autor = "Autor", anio = 2000, ejemplares = 5)

    @Test
    fun asigna_ids_correlativos() = runTest {
        val repo = LibroRepositorioEnMemoria()
        val primero = repo.registrar(libro("Primero"))
        val segundo = repo.registrar(libro("Segundo"))
        assertEquals(1L, primero.id)
        assertEquals(2L, segundo.id)
    }

    @Test
    fun lista_en_orden_de_registro() = runTest {
        val repo = LibroRepositorioEnMemoria()
        repo.registrar(libro("Primero"))
        repo.registrar(libro("Segundo"))
        val lista = repo.listar()
        assertEquals(listOf("Primero", "Segundo"), lista.map { it.titulo })
    }
}
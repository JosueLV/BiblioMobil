package pe.edu.upeu.bibliomobil

import pe.edu.upeu.bibliomobil.domain.model.Libro
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LibroTest {

    @Test
    fun rechaza_titulo_vacio() {
        assertFailsWith<IllegalArgumentException> {
            Libro(id = 1, titulo = "", autor = "Autor", anio = 2000, ejemplares = 5)
        }
    }

    @Test
    fun rechaza_anio_fuera_de_rango() {
        assertFailsWith<IllegalArgumentException> {
            Libro(id = 1, titulo = "Título", autor = "Autor", anio = 1000, ejemplares = 5)
        }
    }

    @Test
    fun requiereReposicion_cierto_con_dos_ejemplares() {
        val libro = Libro(id = 1, titulo = "Título", autor = "Autor", anio = 2000, ejemplares = 2)
        assertTrue(libro.requiereReposicion)
    }

    @Test
    fun requiereReposicion_falso_con_tres_ejemplares() {
        val libro = Libro(id = 1, titulo = "Título", autor = "Autor", anio = 2000, ejemplares = 3)
        assertFalse(libro.requiereReposicion)
    }
}
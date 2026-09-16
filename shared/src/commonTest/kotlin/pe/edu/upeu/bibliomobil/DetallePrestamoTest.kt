package pe.edu.upeu.bibliomobil

import pe.edu.upeu.bibliomobil.domain.model.DetallePrestamo
import pe.edu.upeu.bibliomobil.domain.model.Libro
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DetallePrestamoTest {

    private val libro = Libro(id = 1, titulo = "Título", autor = "Autor", anio = 2000, ejemplares = 5)

    @Test
    fun rechaza_cero_dias() {
        assertFailsWith<IllegalArgumentException> {
            DetallePrestamo(libro = libro, dias = 0)
        }
    }

    @Test
    fun rechaza_dieciseis_dias() {
        assertFailsWith<IllegalArgumentException> {
            DetallePrestamo(libro = libro, dias = 16)
        }
    }

    @Test
    fun multaPorRetraso_de_cuatro_dias_es_seis() {
        val detalle = DetallePrestamo(libro = libro, dias = 7)
        assertEquals(6.0, detalle.multaPorRetraso(4))
    }
}
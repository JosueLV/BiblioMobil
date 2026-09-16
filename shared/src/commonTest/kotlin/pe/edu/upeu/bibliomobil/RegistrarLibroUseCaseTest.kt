package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.domain.usecase.LibroInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import pe.edu.upeu.bibliomobil.fakes.FakeLibroRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RegistrarLibroUseCaseTest {

    @Test
    fun acepta_un_libro_valido() = runTest {
        val repo = FakeLibroRepository()
        val useCase = RegistrarLibroUseCase(repo)

        val resultado = useCase("Cien años de soledad", "García Márquez", "1967", "5")

        assertTrue(resultado.isSuccess)
        assertEquals(1L, resultado.getOrNull()?.id)
    }

    @Test
    fun titulo_vacio_devuelve_mensaje_del_anexo() = runTest {
        val useCase = RegistrarLibroUseCase(FakeLibroRepository())
        val resultado = useCase("", "Autor", "2000", "5")
        val error = resultado.exceptionOrNull() as LibroInvalidoException
        assertEquals("El título es obligatorio", error.errores.titulo)
    }

    @Test
    fun autor_vacio_devuelve_mensaje_del_anexo() = runTest {
        val useCase = RegistrarLibroUseCase(FakeLibroRepository())
        val resultado = useCase("Título", "", "2000", "5")
        val error = resultado.exceptionOrNull() as LibroInvalidoException
        assertEquals("El autor es obligatorio", error.errores.autor)
    }

    @Test
    fun anio_no_numerico_devuelve_mensaje_del_anexo() = runTest {
        val useCase = RegistrarLibroUseCase(FakeLibroRepository())
        val resultado = useCase("Título", "Autor", "abc", "5")
        val error = resultado.exceptionOrNull() as LibroInvalidoException
        assertEquals("El año debe ser un número entero", error.errores.anio)
    }

    @Test
    fun ejemplares_negativos_devuelve_mensaje_del_anexo() = runTest {
        val useCase = RegistrarLibroUseCase(FakeLibroRepository())
        val resultado = useCase("Título", "Autor", "2000", "-1")
        val error = resultado.exceptionOrNull() as LibroInvalidoException
        assertEquals("Los ejemplares no pueden ser negativos", error.errores.ejemplares)
    }

    @Test
    fun el_id_lo_asigna_el_repositorio() = runTest {
        val repo = FakeLibroRepository()
        val useCase = RegistrarLibroUseCase(repo)
        useCase("Primero", "Autor", "2000", "5")
        val resultado = useCase("Segundo", "Autor", "2000", "5")
        assertEquals(2L, resultado.getOrNull()?.id)
    }

    @Test
    fun fallo_del_repositorio_llega_como_result_failure() = runTest {
        val repo = FakeLibroRepository().apply { debeFallar = true }
        val useCase = RegistrarLibroUseCase(repo)
        val resultado = useCase("Título", "Autor", "2000", "5")
        assertTrue(resultado.isFailure)
    }
}
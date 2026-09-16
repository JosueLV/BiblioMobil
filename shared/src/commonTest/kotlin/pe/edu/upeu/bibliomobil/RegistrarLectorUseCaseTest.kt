package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.domain.usecase.LectorInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase
import pe.edu.upeu.bibliomobil.fakes.FakeLectorRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RegistrarLectorUseCaseTest {

    @Test
    fun correo_invalido_devuelve_mensaje_del_anexo() = runTest {
        val useCase = RegistrarLectorUseCase(FakeLectorRepository())
        val resultado = useCase("Nombre", "correo-sin-arroba", "987654321")
        val error = resultado.exceptionOrNull() as LectorInvalidoException
        assertEquals("El correo no tiene un formato válido", error.errores.correo)
    }

    @Test
    fun telefono_corto_devuelve_mensaje_del_anexo() = runTest {
        val useCase = RegistrarLectorUseCase(FakeLectorRepository())
        val resultado = useCase("Nombre", "nombre@correo.com", "123")
        val error = resultado.exceptionOrNull() as LectorInvalidoException
        assertEquals("El teléfono debe tener entre 6 y 9 dígitos", error.errores.telefono)
    }

    @Test
    fun telefono_en_blanco_se_guarda_como_null() = runTest {
        val useCase = RegistrarLectorUseCase(FakeLectorRepository())
        val resultado = useCase("Nombre", "nombre@correo.com", "   ")
        assertTrue(resultado.isSuccess)
        assertNull(resultado.getOrNull()?.telefono)
    }
}
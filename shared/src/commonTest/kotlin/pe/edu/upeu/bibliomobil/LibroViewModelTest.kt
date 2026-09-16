package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import pe.edu.upeu.bibliomobil.fakes.FakeLibroRepository
import pe.edu.upeu.bibliomobil.presentation.libro.FaseLibros
import pe.edu.upeu.bibliomobil.presentation.libro.LibroViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class LibroViewModelTest {

    @BeforeTest
    fun antesDeCadaCaso() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun despuesDeCadaCaso() {
        Dispatchers.resetMain()
    }

    private fun crearViewModel(repo: FakeLibroRepository) =
        LibroViewModel(RegistrarLibroUseCase(repo), ListarLibrosUseCase(repo))

    @Test
    fun arranca_en_sinLibros() = runTest {
        val viewModel = crearViewModel(FakeLibroRepository())
        assertIs<FaseLibros.SinLibros>(viewModel.uiState.value.fase)
    }

    @Test
    fun muestra_linea_secundaria_correcta() = runTest {
        val repo = FakeLibroRepository()
        repo.registrar(
            pe.edu.upeu.bibliomobil.domain.model.Libro(0, "Título", "Autor", 1998, 3)
        )
        val viewModel = crearViewModel(repo)
        viewModel.cargarLibros()
        val fase = viewModel.uiState.value.fase as FaseLibros.ConLibros
        assertEquals("1998 · 3 ejemplares", fase.libros.first().lineaSecundaria)
    }

    @Test
    fun pasa_a_error_si_el_repositorio_falla() = runTest {
        val repo = FakeLibroRepository().apply { debeFallar = true }
        val viewModel = crearViewModel(repo)
        assertIs<FaseLibros.Error>(viewModel.uiState.value.fase)
    }

    @Test
    fun errores_de_validacion_caen_en_el_formulario_no_en_la_fase() = runTest {
        val repo = FakeLibroRepository()
        val viewModel = crearViewModel(repo)
        viewModel.onTituloChange("")
        viewModel.onAutorChange("Autor")
        viewModel.onAnioChange("2000")
        viewModel.onEjemplaresChange("5")
        viewModel.registrar()

        assertEquals("El título es obligatorio", viewModel.uiState.value.formulario.errorTitulo)
        assertIs<FaseLibros.SinLibros>(viewModel.uiState.value.fase)
    }

    @Test
    fun registrar_limpia_el_formulario_y_recarga() = runTest {
        val repo = FakeLibroRepository()
        val viewModel = crearViewModel(repo)
        viewModel.onTituloChange("Título")
        viewModel.onAutorChange("Autor")
        viewModel.onAnioChange("2000")
        viewModel.onEjemplaresChange("5")
        viewModel.registrar()

        assertEquals("", viewModel.uiState.value.formulario.titulo)
        assertIs<FaseLibros.ConLibros>(viewModel.uiState.value.fase)
    }
}
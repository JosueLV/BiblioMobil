package pe.edu.upeu.bibliomobil

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import pe.edu.upeu.bibliomobil.di.dataModule
import pe.edu.upeu.bibliomobil.di.domainModule
import pe.edu.upeu.bibliomobil.data.repository.LibroRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame

class AppModuleTest : KoinTest {

    private val libroRepository: LibroRepository by inject()
    private val registrarLibro: RegistrarLibroUseCase by inject()
    private val listarLibros: ListarLibrosUseCase by inject()
    private val registrarLector: RegistrarLectorUseCase by inject()
    private val listarLectores: ListarLectoresUseCase by inject()

    @BeforeTest
    fun iniciarKoin() {
        startKoin { modules(dataModule, domainModule) }
    }

    @AfterTest
    fun detenerKoin() {
        stopKoin()
    }

    @Test
    fun resuelve_libroRepository_como_libroRepositorioEnMemoria() {
        assertIs<LibroRepositorioEnMemoria>(libroRepository)
    }

    @Test
    fun el_repositorio_es_unico() {
        val otraInstancia: LibroRepository by inject()
        assertSame(libroRepository, otraInstancia)
    }

    @Test
    fun resuelve_los_cuatro_casos_de_uso() {
        assertIs<RegistrarLibroUseCase>(registrarLibro)
        assertIs<ListarLibrosUseCase>(listarLibros)
        assertIs<RegistrarLectorUseCase>(registrarLector)
        assertIs<ListarLectoresUseCase>(listarLectores)
    }
}
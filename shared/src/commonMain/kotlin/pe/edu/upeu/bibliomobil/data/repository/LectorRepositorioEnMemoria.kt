package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository
import kotlin.random.Random

class LectorRepositorioEnMemoria : LectorRepository {

    private val mutex = Mutex()
    private val lectores = mutableListOf<Lector>()
    private var siguienteId = 1L

    override suspend fun registrar(lector: Lector): Lector {
        simularLatencia()
        return mutex.withLock {
            val lectorConId = lector.copy(id = siguienteId)
            siguienteId += 1
            lectores.add(lectorConId)
            lectorConId
        }
    }

    override suspend fun listar(): List<Lector> {
        simularLatencia()
        return mutex.withLock {
            lectores.toList()
        }
    }

    private suspend fun simularLatencia() {
        delay(Random.nextLong(300, 801))
    }
}
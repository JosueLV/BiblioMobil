package pe.edu.upeu.bibliomobil.fakes

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

class FakeLectorRepository : LectorRepository {
    val lectores = mutableListOf<Lector>()
    private var siguienteId = 1L
    var debeFallar = false

    override suspend fun registrar(lector: Lector): Lector {
        if (debeFallar) throw RuntimeException("Fallo simulado al registrar lector")
        val lectorConId = lector.copy(id = siguienteId)
        siguienteId += 1
        lectores.add(lectorConId)
        return lectorConId
    }

    override suspend fun listar(): List<Lector> {
        if (debeFallar) throw RuntimeException("Fallo simulado al listar lectores")
        return lectores.toList()
    }
}
package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Lector

/**
 * Contrato para registrar y consultar la cartera de lectores de la biblioteca.
 * La fuente concreta vive en la capa data; dominio y presentación solo conocen esta interfaz.
 */
interface LectorRepository {

    /** Da de alta un lector en la cartera. El repositorio asigna el id definitivo. */
    suspend fun registrar(lector: Lector): Lector

    /** Devuelve la cartera completa, en el orden en que se registraron los lectores. */
    suspend fun listar(): List<Lector>
}
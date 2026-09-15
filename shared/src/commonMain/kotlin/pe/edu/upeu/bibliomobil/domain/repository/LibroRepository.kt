package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Libro

/**
 * Contrato para registrar y consultar el catálogo de libros de la biblioteca.
 * La fuente concreta (memoria, backend REST, etc.) vive en la capa data;
 * el dominio y la presentación solo dependen de esta interfaz.
 */
interface LibroRepository {

    /** Da de alta un libro en el catálogo. El repositorio asigna el id definitivo. */
    suspend fun registrar(libro: Libro): Libro

    /** Devuelve el catálogo completo, en el orden en que se registraron los libros. */
    suspend fun listar(): List<Libro>
}
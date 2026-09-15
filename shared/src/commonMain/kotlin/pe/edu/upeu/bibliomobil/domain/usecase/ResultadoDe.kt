package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.CancellationException

/**
 * Ejecuta un bloque suspendido y lo envuelve en Result, dejando pasar
 * CancellationException sin capturarla (para no romper la cooperación
 * de coroutines si la pantalla se cierra a mitad de una operación).
 */
suspend fun <T> resultadoDe(bloque: suspend () -> T): Result<T> {
    return try {
        Result.success(bloque())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
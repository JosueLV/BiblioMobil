package pe.edu.upeu.bibliomobil.di

import org.koin.dsl.module

actual val platformModule = module {
    // Sin dependencias específicas de Android por ahora.
    // Aquí irían, por ejemplo, un DataStore o un Context si se necesitara.
}
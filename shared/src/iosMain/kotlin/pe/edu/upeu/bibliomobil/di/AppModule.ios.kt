package pe.edu.upeu.bibliomobil.di

import org.koin.dsl.module

actual val platformModule = module {
    // Sin dependencias específicas de iOS por ahora.
}

fun initKoinIos() {
    initKoin()
}
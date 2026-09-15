package pe.edu.upeu.bibliomobil.presentation.lector

import pe.edu.upeu.bibliomobil.domain.model.Lector

data class LectorUi(
    val id: Long,
    val nombre: String,
    val correo: String,
    val telefonoMostrado: String
)

fun Lector.aUi(): LectorUi = LectorUi(
    id = id,
    nombre = nombre,
    correo = correo,
    telefonoMostrado = telefono ?: "No registrado"
)
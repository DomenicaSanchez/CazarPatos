package com.sanchez.domenica.cazarpatos

import android.content.Context
import java.io.File
import java.io.IOException

class InternalFileManager(private val context: Context) : FileHandler {

    private val defaultFileName = "datos_usuario.txt"


    // Guarda la información en un archivo interno.

    override fun SaveInformation(data: Pair<String, String>) {
        try {
            // openFileOutput crea un flujo de salida privado para la app
            context.openFileOutput(defaultFileName, Context.MODE_PRIVATE).use { output ->
                // Formato simple: linea 1 email, linea 2 password
                val contenido = "${data.first}\n${data.second}"
                output.write(contenido.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


     // Lee la información del archivo interno.

    override fun ReadInformation(): Pair<String, String> {
        val file = File(context.filesDir, defaultFileName)

        if (!file.exists()) {
            return Pair("", "")
        }

        return try {
            // readText lee todo el contenido del archivo de una sola vez
            val contenido = context.openFileInput(defaultFileName).bufferedReader().use {
                it.readText()
            }

            // Separamos por líneas para reconstruir el Pair
            val lineas = contenido.split("\n")
            val email = lineas.getOrElse(0) { "" }
            val password = lineas.getOrElse(1) { "" }

            Pair(email, password)

        } catch (e: IOException) {
            e.printStackTrace()
            Pair("", "")
        }
    }
}

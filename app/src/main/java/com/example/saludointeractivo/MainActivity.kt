package com.example.saludointeractivo

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)

        // Referencias a los elementos de la interfaz
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etEdad = findViewById<EditText>(R.id.etEdad)
        val switchRecordar = findViewById<Switch>(R.id.switchRecordar)
        val btnSaludar = findViewById<Button>(R.id.btnSaludar)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)
        val btnSalir = findViewById<Button>(R.id.btnSalir) // Nuevo botón de salida
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        // Recuperar nombre guardado si el switch estaba activado
        val nombreGuardado = sharedPreferences.getString("nombre", "")
        val recordar = sharedPreferences.getBoolean("recordar", false)
        if (recordar && !nombreGuardado.isNullOrEmpty()) {
            etNombre.setText(nombreGuardado)
            switchRecordar.isChecked = true
        }

        // Acción del botón Saludar
        btnSaludar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val edadStr = etEdad.text.toString().trim()

            // Validaciones
            if (nombre.isEmpty() || edadStr.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val edad = edadStr.toIntOrNull()
            if (edad == null || edad <= 0) {
                Toast.makeText(this, "Ingrese una edad válida mayor que 0.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardar nombre en SharedPreferences si el switch está activado
            if (switchRecordar.isChecked) {
                sharedPreferences.edit()
                    .putString("nombre", nombre)
                    .putBoolean("recordar", true)
                    .apply()
            } else {
                sharedPreferences.edit()
                    .remove("nombre")
                    .putBoolean("recordar", false)
                    .apply()
            }

            // Cambiar el color del TextView según la edad
            val color = when {
                edad < 18 -> Color.BLUE
                edad in 18..50 -> Color.GREEN
                else -> Color.RED
            }
            tvResultado.setTextColor(color)
            tvResultado.text = "¡Hola, $nombre! Tienes $edad años."

            // Mostrar mensaje en AlertDialog
            AlertDialog.Builder(this)
                .setTitle("Saludo")
                .setMessage("¡Hola, $nombre! Tienes $edad años.")
                .setPositiveButton("OK", null)
                .show()
        }

        // Acción del botón Limpiar
        btnLimpiar.setOnClickListener {
            etNombre.text.clear()
            etEdad.text.clear()
            switchRecordar.isChecked = false
            tvResultado.text = ""
            tvResultado.setTextColor(Color.BLACK)

            // Borrar nombre guardado si estaba activado
            sharedPreferences.edit().remove("nombre").putBoolean("recordar", false).apply()

            Toast.makeText(this, "Campos limpiados", Toast.LENGTH_SHORT).show()
        }

        // Acción del botón Salir
        btnSalir.setOnClickListener {
            mostrarDialogoSalida()
        }

        // Manejo del botón "Atrás" con AlertDialog
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mostrarDialogoSalida()
            }
        })
    }

    // Método para mostrar el AlertDialog de salida
    private fun mostrarDialogoSalida() {
        AlertDialog.Builder(this)
            .setTitle("Salir de la aplicación")
            .setMessage("¿Estás seguro de que quieres salir?")
            .setPositiveButton("Sí") { _, _ -> finish() }
            .setNegativeButton("No", null)
            .show()
    }
}






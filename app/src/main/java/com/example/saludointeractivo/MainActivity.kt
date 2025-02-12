package com.example.saludointeractivo

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
        val btnSaludar = findViewById<Button>(R.id.btnSaludar)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        // Recuperar nombre guardado
        val nombreGuardado = sharedPreferences.getString("nombre", "")
        if (!nombreGuardado.isNullOrEmpty()) {
            etNombre.setText(nombreGuardado)
        }

        // Acción del botón Saludar
        btnSaludar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val edadStr = etEdad.text.toString().trim()

            // Validaciones
            if (nombre.isEmpty() || edadStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val edad = edadStr.toIntOrNull()
            if (edad == null || edad <= 0) {
                Toast.makeText(this, getString(R.string.error_edad_invalida), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardar el nombre en SharedPreferences
            sharedPreferences.edit().putString("nombre", nombre).apply()

            // Cambiar el color del TextView según la edad
            tvResultado.setTextColor(
                when {
                    edad < 18 -> Color.BLUE
                    edad in 18..50 -> Color.GREEN
                    else -> Color.RED
                }
            )

            // Mostrar mensaje en un Dialog en lugar de un TextView
            AlertDialog.Builder(this)
                .setTitle("Saludo")
                .setMessage(getString(R.string.mensaje_saludo, nombre, edad))
                .setPositiveButton("OK", null)
                .create()
                .show()
        }

        // Acción del botón Limpiar
        btnLimpiar.setOnClickListener {
            etNombre.text.clear()
            etEdad.text.clear()
            tvResultado.text = ""
            tvResultado.setTextColor(Color.BLACK)
            Toast.makeText(this, "Campos limpiados", Toast.LENGTH_SHORT).show()
        }
    }
}




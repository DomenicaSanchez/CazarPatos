package com.sanchez.domenica.cazarpatos

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query

class RegisterActivity : AppCompatActivity() {

    // Declara la instancia de autenticación de Firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializa la instancia de Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Referencias a los elementos de la UI (asegúrate de que los IDs coincidan con tu activity_register.xml)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)
        val buttonSignUp = findViewById<Button>(R.id.buttonSignUp)
        val buttonBackToLogin = findViewById<Button>(R.id.buttonBackToLogin)

        // --- Lógica para el botón de Registrarse (Sign Up) ---
        buttonSignUp.setOnClickListener {
            // Obtiene los textos de los campos
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val confirmPassword = editTextConfirmPassword.text.toString().trim()

            // Variable para controlar si los datos son válidos
            var isValid = true

            // 1. Validar el formato del correo electrónico
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.error = "Correo electrónico no válido"
                isValid = false
            } else {
                editTextEmail.error = null
            }

            // 2. Validar que la contraseña tenga al menos 8 caracteres
            if (password.length < 8) {
                editTextPassword.error = "La contraseña debe tener al menos 8 caracteres"
                isValid = false
            } else {
                editTextPassword.error = null
            }

            // 3. Validar que las contraseñas coincidan
            if (password != confirmPassword) {
                editTextConfirmPassword.error = "Las contraseñas no coinciden"
                isValid = false
            } else {
                editTextConfirmPassword.error = null
            }

            // 4. Si todos los datos son válidos, crear el usuario
            if (isValid) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Registro exitoso, redirige a LoginActivity
                            Toast.makeText(baseContext, "Registro exitoso.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish() // Cierra esta actividad para que el usuario no pueda volver con el botón "atrás"
                        } else {
                            // Si el registro falla, muestra un mensaje de error
                            Toast.makeText(baseContext, "Falló la autenticación: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        // --- Lógica para el botón de Regresar al Login ---
        buttonBackToLogin.setOnClickListener {
            finish() // Cierra la actividad actual y regresa a la anterior (LoginActivity)
        }
    }
}

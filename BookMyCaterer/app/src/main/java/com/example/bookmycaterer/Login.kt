package com.example.bookmycaterer

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.LoginRequest
import com.example.bookmycaterer.user.LoginResponse
import com.example.bookmycaterer.user.UserHome
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var edtPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var signas: Button
    private lateinit var home: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtPhone = findViewById(R.id.editTextCatererId)
        edtPassword = findViewById(R.id.editTextCreatePassword)
        signas = findViewById(R.id.button3)
        home = findViewById(R.id.button)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        signas.setOnClickListener {
            val intent = Intent(this@Login, SignUpAs::class.java)
            startActivity(intent)
            finish()
        }

        home.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val phone = edtPhone.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (phone.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Please enter phone and password", Toast.LENGTH_SHORT).show()
            return
        }

        val loginRequest = LoginRequest(phone, password)
        val api = Retro.instance.loginUser(loginRequest)

        api.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()

                    if (loginResponse?.success == true) {
                        // ✅ Save user_id in SharedPreferences correctly
                        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        sharedPreferences.edit()
                            .putString("user_id", loginResponse.User._id)
                            .apply()

                        Toast.makeText(this@Login, "Login Successful!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@Login, UserHome::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@Login, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Login, "Login failed! Try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@Login, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}


package com.example.bookmycaterer.caterer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmycaterer.R
import com.example.bookmycaterer.SignUpAs
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.CatererLoginResponse
import com.example.bookmycaterer.user.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatererLoginActivity : AppCompatActivity() {

    private lateinit var edtPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caterer_login)

        edtPhone = findViewById(R.id.edtCatPhone)
        edtPassword = findViewById(R.id.edtCatPassword)
        btnLogin = findViewById(R.id.btnCatLogin)
        btnSignup = findViewById(R.id.btnSignup)

        btnLogin.setOnClickListener { login() }

        btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpAs::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val phone = edtPhone.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (phone.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Phone or password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val loginRequest = LoginRequest(phone, password)

        // API call
        Retro.instance.loginCaterer(loginRequest).enqueue(object : Callback<CatererLoginResponse> {
            override fun onResponse(
                call: Call<CatererLoginResponse>,
                response: Response<CatererLoginResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.success == true) {

                        Log.i("id....", loginResponse.caterer?.id.toString())

                        val intent = Intent(this@CatererLoginActivity, CatererHomeActivity::class.java)
                        val sharedPreferences = getSharedPreferences("CatererPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("caterer_id", loginResponse.caterer?.id)
                        editor.apply()
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@CatererLoginActivity, "Login failed: ${loginResponse?.message}", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@CatererLoginActivity, "Error ${response.code()}: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CatererLoginResponse>, t: Throwable) {
                Toast.makeText(this@CatererLoginActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
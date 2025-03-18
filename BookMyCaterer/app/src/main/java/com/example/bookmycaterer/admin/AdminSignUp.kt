package com.example.bookmycaterer.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookmycaterer.Login
import com.example.bookmycaterer.R


class AdminSignUp : AppCompatActivity() {

    var edtAdminEmail: EditText? = null
    var edtAdminPass: EditText? = null
    var btnLogin: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sign_up)
        edtAdminEmail = findViewById(R.id.edtEmail)
        edtAdminPass = findViewById(R.id.edtPass)
        btnLogin = findViewById(R.id.btnAdminLogin)

        btnLogin?.setOnClickListener {
            val email = edtAdminEmail?.text.toString().trim()
            val password = edtAdminPass?.text.toString().trim()

            if (isValidCredentials(email, password)) {
                startActivity(Intent(this, AdminCatererView::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun isValidCredentials(email: String, password: String): Boolean {
        return email == "admin123@gmail.com" && password == "admin123"
    }
}
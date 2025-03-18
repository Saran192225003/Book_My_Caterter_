package com.example.bookmycaterer.user

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
import com.example.bookmycaterer.api.ApiService
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.caterer.Verifying
import retrofit2.Call

class UserLogin : AppCompatActivity() {
    var edtName: EditText?=null
    var edtEmail: EditText?=null
    var edtPhone: EditText?=null
    var edtCity: EditText?=null
    var edtPassword: EditText?=null
    var edtConfirmPassword: EditText?=null

    var btnSignup: Button?=null
    var si: TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        edtName=findViewById(R.id.fullNameEditText)
        edtEmail=findViewById(R.id.emailEditText)
        edtPhone=findViewById(R.id.phoneEditText)
        edtCity=findViewById(R.id.editTextCity)
        edtPassword=findViewById(R.id.passwordEditText)
        edtConfirmPassword=findViewById(R.id.confirmPasswordEditText)



        btnSignup=findViewById(R.id.signUpButton)

        btnSignup?.setOnClickListener {
//

            signup()
        }


        si=findViewById(R.id.loginRedirectTextView)

        si?.setOnClickListener {
            val intent = Intent(this@UserLogin, Login::class.java)
            startActivity(intent)
        }
    }

    private fun signup() {
        val name = edtName?.text.toString()
        val email = edtEmail?.text.toString()
        val phone = edtPhone?.text.toString()
        val city = edtCity?.text.toString()
        val password = edtPassword?.text.toString()
        val confirmPassword = edtConfirmPassword?.text.toString()

        if (password != confirmPassword) {
            Toast.makeText(this,"Password doesn't match",Toast.LENGTH_SHORT).show()
            return
        }
        if ((edtPhone?.text?.length ?: 0) < 10) {
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
        }


        val userRequest = UserRequest(name, email, phone, city, password)

        //val userApi = Retro.instance.create(ApiService::class.java)

        val api = Retro.instance.createUser(userRequest)
        api.enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: retrofit2.Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse?.success == true) {

                        Toast.makeText(this@UserLogin,"User created successfully",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@UserLogin, UserHome::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@UserLogin,response.toString(),Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@UserLogin,response.message(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(this@UserLogin,t.message,Toast.LENGTH_SHORT).show()
            }
        })
    }

}
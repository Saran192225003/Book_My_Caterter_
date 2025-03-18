package com.example.bookmycaterer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookmycaterer.admin.AdminSignUp
import com.example.bookmycaterer.caterer.CatererSignUp
import com.example.bookmycaterer.caterer.Menu
import com.example.bookmycaterer.user.UserLogin

class SignUpAs : AppCompatActivity() {
    var cat:Button?=null
    var cust:Button?=null
    var ad:Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_as)
        cat=findViewById(R.id.signupCatererButton)

        cat?.setOnClickListener {
            val intent = Intent(this@SignUpAs, CatererSignUp::class.java)
            startActivity(intent)
        }
        cust=findViewById(R.id.signupCustomerButton)

        cust?.setOnClickListener {
            val intent = Intent(this@SignUpAs, UserLogin::class.java)
            startActivity(intent)
        }
        ad=findViewById(R.id.signupAdminButton)

        ad?.setOnClickListener {
            val intent = Intent(this@SignUpAs, AdminSignUp::class.java)
            startActivity(intent)
        }
    }
}
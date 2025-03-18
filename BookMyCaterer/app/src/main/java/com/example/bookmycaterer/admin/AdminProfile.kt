package com.example.bookmycaterer.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookmycaterer.Login
import com.example.bookmycaterer.R

class AdminProfile : AppCompatActivity() {
    var lout: ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_profile)
        lout=findViewById(R.id.logoutIcon1)

        lout?.setOnClickListener {
            val intent = Intent(this@AdminProfile, Login::class.java)
            startActivity(intent)
        }
    }
}
package com.example.bookmycaterer.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmycaterer.R

class AdminHome : AppCompatActivity() {
    var viewcat:Button?=null
    var ver:Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_home)
        viewcat=findViewById(R.id.buttonViewCaterers)

        viewcat?.setOnClickListener {
            val intent = Intent(this@AdminHome, AdminCatererView::class.java)
            startActivity(intent)
        }
        ver=findViewById(R.id.buttonVerifyCaterers)

        ver?.setOnClickListener {
            val intent = Intent(this@AdminHome, ViewVerifications::class.java)
            startActivity(intent)
        }
    }
}
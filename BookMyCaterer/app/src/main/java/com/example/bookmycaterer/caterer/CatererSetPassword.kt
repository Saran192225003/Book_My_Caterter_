package com.example.bookmycaterer.caterer


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmycaterer.R

class CatererSetPassword : AppCompatActivity() {
    var lo: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_caterer_set_password)
        lo=findViewById(R.id.button)

        lo?.setOnClickListener {
            val intent = Intent(this@CatererSetPassword, CatererHomeActivity::class.java)
            startActivity(intent)
        }
    }
}
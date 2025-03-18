package com.example.bookmycaterer.caterer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmycaterer.R

class Verifying : AppCompatActivity() {
    var ver:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_verifying)

        // Set window insets for edge-to-edge support


        // Dot animation setup
        val loadingDots = findViewById<TextView>(R.id.loadingDots)
        val handler = Handler(Looper.getMainLooper())
        val dots = arrayOf(".", "..", "...")
        var dotIndex = 0

        // Runnable to update the text every 500ms
        val runnable = object : Runnable {
            override fun run() {
                loadingDots.text = dots[dotIndex]
                dotIndex = (dotIndex + 1) % dots.size
                handler.postDelayed(this, 500)  // Repeat every 500ms
            }
        }

        handler.post(runnable)  // Start the animation


        ver=findViewById(R.id.verifyingText1)

        ver?.setOnClickListener {
            val intent = Intent(this@Verifying, CatererSetPassword::class.java)
            startActivity(intent)
        }
    }
}

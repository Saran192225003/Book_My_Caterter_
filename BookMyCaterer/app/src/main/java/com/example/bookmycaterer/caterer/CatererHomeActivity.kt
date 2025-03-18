package com.example.bookmycaterer.caterer




import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmycaterer.R

class CatererHomeActivity : AppCompatActivity() {
    var men:Button?=null
    var pack:Button?=null
    var avail:Button?=null
    var ord:ImageView?=null
    var pro:ImageView?=null

    var id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_caterer_home)

        val sharedPreferences = getSharedPreferences("CatererPrefs", MODE_PRIVATE)
        id= sharedPreferences.getString("caterer_id", null).toString()

        Log.i("Cid...",id)

     
        men = findViewById(R.id.buttonMenu)
        men?.setOnClickListener {
            val intent = Intent(this@CatererHomeActivity, Menu::class.java)
            startActivity(intent)
        }
        pack = findViewById(R.id.buttonPackages)
        pack?.setOnClickListener {
            val intent = Intent(this@CatererHomeActivity, Packages::class.java)
            startActivity(intent)
        }
        avail = findViewById(R.id.buttonCalendar)
        avail?.setOnClickListener {
            val intent = Intent(this@CatererHomeActivity, CatererAvailability::class.java)
            startActivity(intent)
        }
        ord = findViewById(R.id.profileOrders)
        ord?.setOnClickListener {
            val intent = Intent(this@CatererHomeActivity, CatererOrdersActivity::class.java)
            startActivity(intent)
        }
        pro = findViewById(R.id.profile)
        pro?.setOnClickListener {
            val intent = Intent(this@CatererHomeActivity, CatererProfile::class.java)
            startActivity(intent)
        }
    }

}
package com.example.bookmycaterer.caterer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.MenuRequest
import com.example.bookmycaterer.user.MenuResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMenu : AppCompatActivity() {
    var btnAdd: Button? = null
    var menuName: EditText? = null
    var edtPrice: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_menu)

        menuName = findViewById(R.id.editTextMenuName)
        edtPrice = findViewById(R.id.editTextPrice)
        btnAdd = findViewById(R.id.buttonSubmit)

        btnAdd?.setOnClickListener {
            ADD()
        }
    }

    private fun ADD() {
        val itemName = menuName?.text.toString()
        val price = edtPrice?.text.toString()

        // Retrieve caterer ID from SharedPreferences
        val sharedPreferences = getSharedPreferences("CatererPrefs", MODE_PRIVATE)
        val catererId = sharedPreferences.getString("caterer_id", null)

        // Validate input
        if (itemName.isBlank() || price.isBlank()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        if (catererId.isNullOrBlank()) {
            Toast.makeText(this, "Caterer ID is missing. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create the request object
        val menuRequest = MenuRequest(itemName, price)

        // Create API instance and make the request
        val api = Retro.instance.createMenu(catererId, menuRequest)
        api.enqueue(object : Callback<MenuResponse> {
            override fun onResponse(call: Call<MenuResponse>, response: Response<MenuResponse>) {
                if (response.isSuccessful) {
                    val menuResponse = response.body()
                    if (menuResponse != null) {
                        Toast.makeText(this@AddMenu, "Menu item added successfully", Toast.LENGTH_SHORT).show()
                        menuName?.text?.clear()
                        edtPrice?.text?.clear()
                    } else {
                        Toast.makeText(this@AddMenu, "Failed to add menu item", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@AddMenu, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MenuResponse>, t: Throwable) {
                Toast.makeText(this@AddMenu, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

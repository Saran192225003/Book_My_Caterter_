package com.example.bookmycaterer.caterer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.MenuItem
import com.example.bookmycaterer.user.MenuResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Menu : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var addNewMenuButton: Button
    private lateinit var editPriceButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Initialize views
        recyclerView = findViewById(R.id.recyclemenu)
        addNewMenuButton = findViewById(R.id.buttonAddNewMenu)
        editPriceButton = findViewById(R.id.buttonEditPrice)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch the menu list
        fetchMenu()

        // Set listeners for buttons
        addNewMenuButton.setOnClickListener {
            val intent = Intent(this@Menu, AddMenu::class.java)
            startActivity(intent)
        }

        editPriceButton.setOnClickListener {
            val intent = Intent(this@Menu, EditPrice::class.java)
            startActivity(intent)
        }
    }

    private fun fetchMenu() {
        // Retrieve the stored caterer ID from SharedPreferences
        val sharedPreferences = getSharedPreferences("CatererPrefs", Context.MODE_PRIVATE)
        val catererId = sharedPreferences.getString("caterer_id", null)

        if (catererId.isNullOrBlank()) {
            Toast.makeText(this, "Caterer ID not found. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        // Make the API call
        val api = Retro.instance.getAllMenu(catererId)
        api.enqueue(object : Callback<List<MenuItem>> {
            override fun onResponse(call: Call<List<MenuItem>>, response: Response<List<MenuItem>>) {
                if (response.isSuccessful) {
                    val menuList = response.body()
                    if (menuList.isNullOrEmpty()) {
                        Toast.makeText(this@Menu, "No menus available", Toast.LENGTH_SHORT).show()
                    } else {
                        // Set the data to the RecyclerView
                        menuAdapter = MenuAdapter(menuList)
                        recyclerView.adapter = menuAdapter
                    }
                } else {
                    Toast.makeText(this@Menu, "Failed to fetch menus: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MenuItem>>, t: Throwable) {
                Toast.makeText(this@Menu, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

package com.example.bookmycaterer.caterer

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.MenuItem
import com.example.bookmycaterer.user.UpdateMenuRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPrice : AppCompatActivity() {

    private lateinit var spinnerMenu: Spinner
    private lateinit var editTextNewPrice: EditText
    private lateinit var buttonSubmit: Button
    private lateinit var menuList: List<MenuItem>
    private lateinit var selectedMenuId: String
    private lateinit var selectedItemName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_price)

        // Initialize Views
        spinnerMenu = findViewById(R.id.spinner1)
        editTextNewPrice = findViewById(R.id.editTextNewPrice)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        // Fetch menu items for the spinner
        fetchMenus()

        // Submit button listener
        buttonSubmit.setOnClickListener {
            val newPrice = editTextNewPrice.text.toString().trim()

            if (newPrice.isEmpty()) {
                Toast.makeText(this, "Please enter a new price", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            updateMenuPrice(selectedMenuId, selectedItemName, newPrice)
        }
    }

    private fun fetchMenus() {
        val sharedPreferences = getSharedPreferences("CatererPrefs", Context.MODE_PRIVATE)
        val catererId = sharedPreferences.getString("caterer_id", null)

        if (catererId.isNullOrBlank()) {
            Toast.makeText(this, "Caterer ID not found. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        val api = Retro.instance.getAllMenu(catererId)
        api.enqueue(object : Callback<List<MenuItem>> {
            override fun onResponse(call: Call<List<MenuItem>>, response: Response<List<MenuItem>>) {
                if (response.isSuccessful) {
                    menuList = response.body() ?: emptyList()

                    if (menuList.isEmpty()) {
                        Toast.makeText(this@EditPrice, "No menus available", Toast.LENGTH_SHORT).show()
                    } else {
                        val menuNames = menuList.map { it.item_name }
                        val adapter = ArrayAdapter(this@EditPrice, android.R.layout.simple_spinner_item, menuNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerMenu.adapter = adapter

                        // Set listener to get selected menu item
                        spinnerMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                selectedMenuId = menuList[position]._id
                                selectedItemName = menuList[position].item_name
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                    }
                } else {
                    Toast.makeText(this@EditPrice, "Failed to fetch menus: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MenuItem>>, t: Throwable) {
                Toast.makeText(this@EditPrice, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateMenuPrice(menuId: String, itemName: String, newPrice: String) {
        val updatedMenu = UpdateMenuRequest(item_name = itemName, price = newPrice)

        val api = Retro.instance.updateMenuPrice(menuId, updatedMenu)
        api.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditPrice, "Price updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@EditPrice, "Failed to update price", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@EditPrice, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

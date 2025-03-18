package com.example.bookmycaterer.caterer

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.MenuItem
import com.example.bookmycaterer.user.PackageRequest
import com.example.bookmycaterer.user.PackageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewPackage : AppCompatActivity() {
    private lateinit var editTextPackageName: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var buttonSavePackage: Button
    private lateinit var textSelectItems: TextView
    private lateinit var spinnerItems: Spinner

    private var menuItems = mutableListOf<MenuItem>()
    private var selectedItems = mutableListOf<String>()
    private lateinit var selectedItemsArray: BooleanArray
    private lateinit var menuItemNames: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_package)

        // Initialize views
        editTextPackageName = findViewById(R.id.editTextPackageName)
        editTextPrice = findViewById(R.id.editTextPrice)
        buttonSavePackage = findViewById(R.id.buttonSavePackage)
        textSelectItems = findViewById(R.id.textSelectItems)
        spinnerItems = findViewById(R.id.spinnerItems)

        // Fetch menu items from API
        fetchMenu()

        // When user clicks on textSelectItems, show multi-select dialog
        textSelectItems.setOnClickListener {
            if (menuItems.isNotEmpty()) {
                showMultiSelectDialog()
            } else {
                Toast.makeText(this, "No menu items available", Toast.LENGTH_SHORT).show()
            }
        }

        // When user clicks on save button, save the package
        buttonSavePackage.setOnClickListener {
            savePackage()
        }

        // Handle spinner item selection
        spinnerItems.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                selectedItems.clear()
                selectedItems.add(selectedItem)
                textSelectItems.text = selectedItems.joinToString(", ")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Optional: Handle when nothing is selected
            }
        }
    }

    private fun fetchMenu() {
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
                    val menuList = response.body()
                    if (menuList.isNullOrEmpty()) {
                        Toast.makeText(this@AddNewPackage, "No menu items available", Toast.LENGTH_SHORT).show()
                    } else {
                        // Store menu items
                        menuItems = menuList.toMutableList()
                        menuItemNames = menuItems.map { it.item_name }.toTypedArray()
                        selectedItemsArray = BooleanArray(menuItems.size)

                        // Update Spinner with menu item names
                        val adapter = ArrayAdapter(this@AddNewPackage, android.R.layout.simple_spinner_item, menuItemNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerItems.adapter = adapter
                    }
                } else {
                    Toast.makeText(this@AddNewPackage, "Failed to fetch menus", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MenuItem>>, t: Throwable) {
                Toast.makeText(this@AddNewPackage, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showMultiSelectDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Items")
        builder.setMultiChoiceItems(menuItemNames, selectedItemsArray) { _, which, isChecked ->
            if (isChecked) {
                selectedItems.add(menuItems[which].item_name)
            } else {
                selectedItems.remove(menuItems[which].item_name)
            }
        }
        builder.setPositiveButton("OK") { _, _ ->
            textSelectItems.text = selectedItems.joinToString(", ")
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun savePackage() {
        val packageName = editTextPackageName.text.toString().trim()
        val price = editTextPrice.text.toString().trim()
        val items = selectedItems.joinToString(", ")

        if (packageName.isEmpty() || price.isEmpty() || items.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPreferences = getSharedPreferences("CatererPrefs", Context.MODE_PRIVATE)
        val catererId = sharedPreferences.getString("caterer_id", null)

        if (catererId == null) {
            Toast.makeText(this, "Caterer ID not found. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        val api = Retro.instance.createPackage(catererId, PackageRequest(packageName, items, price))
        api.enqueue(object : Callback<PackageResponse> {
            override fun onResponse(call: Call<PackageResponse>, response: Response<PackageResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddNewPackage, "Package created successfully!", Toast.LENGTH_SHORT).show()
                    Log.i("package",response.message().toString())
                    finish()
                } else {
                    Toast.makeText(this@AddNewPackage, "Failed to create package", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PackageResponse>, t: Throwable) {
                Toast.makeText(this@AddNewPackage, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

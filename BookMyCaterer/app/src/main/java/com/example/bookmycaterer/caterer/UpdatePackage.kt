package com.example.bookmycaterer.caterer

import UpdatePackageAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.bookmycaterer.user.MenuItem
import com.example.bookmycaterer.user.MenuPackage

class UpdatePackage : AppCompatActivity() {
    private lateinit var packageId: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UpdatePackageAdapter
    private val packageList = mutableListOf<String>() // List of menu item names (Strings)
    private lateinit var addButton: Button
    private lateinit var priceEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var saveButton: Button
    private val menuItems = mutableListOf<String>() // Store fetched menu items

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_package)

        packageId = intent.getStringExtra("PACKAGE_ID") ?: ""
        priceEditText = findViewById(R.id.editTextPackagePrice)
        updateButton = findViewById(R.id.buttonUpdatePrice)
        saveButton= findViewById(R.id.buttonSaveChanges)
        fetchPackageDetails()

        updateButton.setOnClickListener {
            val updatedPrice = priceEditText.text.toString()
            if (updatedPrice.isNotEmpty()) {
                updatePackagePrice(updatedPrice)
            } else {
                Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView = findViewById(R.id.recyclerViewMenuItems)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Adapter
        adapter = UpdatePackageAdapter(packageList) { packageItem ->
            deletePackage(packageItem)
        }
        recyclerView.adapter = adapter

        addButton = findViewById(R.id.buttonAddItems)
        addButton.setOnClickListener {
            showAddItemDialog()
        }
        saveButton.setOnClickListener {
            updatePackageMenuItems()
        }

    }

    private fun fetchPackageDetails() {
        Log.d("UpdatePackage", "Fetching details for packageId: $packageId")

        val apiService = Retro.instance.getPackageDetails(packageId)
        apiService.enqueue(object : Callback<MenuPackage> {
            override fun onResponse(call: Call<MenuPackage>, response: Response<MenuPackage>) {
                if (response.isSuccessful) {
                    val packageData = response.body()
                    packageData?.let {
                        priceEditText.setText(it.price)
                        val itemsList = it.items.split(",").map { item -> item.trim() }
                        updateRecyclerViewWithItems(itemsList)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UpdatePackage", "Failed to fetch package: $errorBody")
                    Toast.makeText(this@UpdatePackage, "Error: $errorBody", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<MenuPackage>, t: Throwable) {
                Log.e("UpdatePackage", "Network error: ${t.message}")
                Toast.makeText(this@UpdatePackage, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateRecyclerViewWithItems(items: List<String>) {
        packageList.clear()
        packageList.addAll(items)
        adapter.notifyDataSetChanged()
    }

    private fun deletePackage(packageItem: String) {
        packageList.remove(packageItem)
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "$packageItem removed", Toast.LENGTH_SHORT).show()
    }

    private fun showAddItemDialog() {
        fetchMenu() // Ensure menuItems is populated before showing dialog

        val selectedItems = mutableListOf<String>() // Store selected items
        val checkedItems = BooleanArray(menuItems.size) // Track checked items

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Menu Items")
        builder.setMultiChoiceItems(menuItems.toTypedArray(), checkedItems) { _, which, isChecked ->
            val selectedItem = menuItems[which]
            if (isChecked) {
                selectedItems.add(selectedItem) // Add item if checked
            } else {
                selectedItems.remove(selectedItem) // Remove item if unchecked
            }
        }

        builder.setPositiveButton("OK") { _, _ ->
            selectedItems.forEach { addNewPackage(it) } // Add selected items to package
        }

        builder.setNegativeButton("Cancel", null)

        builder.show()
    }


    private fun addNewPackage(packageName: String) {
        packageList.add(packageName)
        adapter.notifyItemInserted(packageList.size - 1)
        Toast.makeText(this, "$packageName added", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this@UpdatePackage, "No menus available", Toast.LENGTH_SHORT).show()
                    } else {
                        menuItems.clear()
                        menuItems.addAll(menuList.map { it.item_name })
                    }
                } else {
                    Toast.makeText(this@UpdatePackage, "Failed to fetch menus: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MenuItem>>, t: Throwable) {
                Toast.makeText(this@UpdatePackage, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updatePackagePrice(updatedPrice: String) {
        val updateRequest = hashMapOf<String, Any>("price" to updatedPrice.toInt())
        val apiService = Retro.instance.updatePackagePrice(packageId, updateRequest)
        apiService.enqueue(object : Callback<MenuPackage> {
            override fun onResponse(call: Call<MenuPackage>, response: Response<MenuPackage>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdatePackage, "Package price updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UpdatePackage", "Failed to update price: $errorBody")
                    Toast.makeText(this@UpdatePackage, "Failed to update price. $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MenuPackage>, t: Throwable) {
                Log.e("UpdatePackage", "Network error: ${t.message}")
                Toast.makeText(this@UpdatePackage, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

     private fun updatePackageMenuItems() {
        val updatedItems = packageList.joinToString(",") // Convert list to a comma-separated string

        val updateRequest = hashMapOf<String, Any>(
            "package_name" to "basic 123", // Replace with actual package name if needed
            "items" to updatedItems,
            "price" to priceEditText.text.toString().toInt() as Any // Explicitly cast to Any
        )

        val apiService = Retro.instance.updatePackage(packageId, updateRequest)
        apiService.enqueue(object : Callback<MenuPackage> {
            override fun onResponse(call: Call<MenuPackage>, response: Response<MenuPackage>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdatePackage, "Package updated successfully!", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity after update
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UpdatePackage", "Failed to update package: $errorBody")
                    Toast.makeText(this@UpdatePackage, "Failed to update package: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MenuPackage>, t: Throwable) {
                Log.e("UpdatePackage", "Network error: ${t.message}")
                Toast.makeText(this@UpdatePackage, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}

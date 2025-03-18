package com.example.bookmycaterer.caterer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.MenuPackage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Packages : AppCompatActivity() {

    private lateinit var packageAdapter: PakageAdapter
    private lateinit var packageList: MutableList<MenuPackage> // Updated to hold MenuPackage items
    var addnew: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packages)

        // Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclepackage)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the list of packages (empty at first)
        packageList = mutableListOf()

        // Initialize the adapter
        packageAdapter = PakageAdapter(
            packageList,
            onUpdateClick = { packageItem ->
                Toast.makeText(this, "Update clicked for ${packageItem.package_name}", Toast.LENGTH_SHORT).show()
                // Add update logic here
            },
            onRemoveClick = { packageItem ->
                Toast.makeText(this, "Remove clicked for ${packageItem.package_name}", Toast.LENGTH_SHORT).show()
                packageList.remove(packageItem)
                packageAdapter.notifyDataSetChanged()
            }
        )
        recyclerView.adapter = packageAdapter

        // Button to navigate to AddNewPackage screen
        addnew = findViewById(R.id.buttonAddPackage)
        addnew?.setOnClickListener {
            val intent = Intent(this@Packages, AddNewPackage::class.java)
            startActivity(intent)
        }

        // Fetch packages from the API
        fetchPackages()
    }

    private fun fetchPackages() {
        val sharedPreferences = getSharedPreferences("CatererPrefs", Context.MODE_PRIVATE)
        val catererId = sharedPreferences.getString("caterer_id", null)

        if (catererId.isNullOrBlank()) {
            Toast.makeText(this, "Caterer ID not found. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        val api = Retro.instance.getAllPackages(catererId)
        api.enqueue(object : Callback<List<MenuPackage>> {
            override fun onResponse(call: Call<List<MenuPackage>>, response: Response<List<MenuPackage>>) {
                if (response.isSuccessful) {
                    val packages = response.body()
                    if (packages.isNullOrEmpty()) {
                        Toast.makeText(this@Packages, "No packages available", Toast.LENGTH_SHORT).show()
                    } else {
                        // Update the package list and notify the adapter
                        packageList.clear()
                        packageList.addAll(packages)
                        packageAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@Packages, "Failed to fetch packages", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MenuPackage>>, t: Throwable) {
                Toast.makeText(this@Packages, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

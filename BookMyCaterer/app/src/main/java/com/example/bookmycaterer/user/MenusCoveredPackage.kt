package com.example.bookmycaterer.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenusCoveredPackage : AppCompatActivity() {

    private lateinit var tvPackageName: TextView
    private lateinit var rvMenuItems: RecyclerView
    private lateinit var adapter: PackageCoveredAdapter
    private var packageId: String? = null
    private var btnSelect: Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menus_covered_package)

        tvPackageName = findViewById(R.id.tvPackageName)
        rvMenuItems = findViewById(R.id.rvMenuItems)
        btnSelect = findViewById(R.id.btnSelect)

        btnSelect?.setOnClickListener {
            onBackPressed()
            finish()
        }

        // Get packageId from intent
        packageId = intent.getStringExtra("PACKAGE_ID")

        if (packageId != null) {
            fetchPackageDetails(packageId!!)
        } else {
            Toast.makeText(this, "Invalid Package ID", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetchPackageDetails(packageId: String) {
        val apiService = Retro.instance
        apiService.getPackageDetails(packageId).enqueue(object : Callback<MenuPackage> {
            override fun onResponse(call: Call<MenuPackage>, response: Response<MenuPackage>) {
                if (response.isSuccessful) {
                    val packageData = response.body()
                    packageData?.let {
                        tvPackageName.text = it.package_name

                        // Split items and set up RecyclerView
                        val itemsList = it.items.split(",").map { item -> item.trim() }
                        adapter = PackageCoveredAdapter(itemsList)
                        rvMenuItems.layoutManager = LinearLayoutManager(this@MenusCoveredPackage)
                        rvMenuItems.adapter = adapter
                    }
                } else {
                    Toast.makeText(this@MenusCoveredPackage, "Failed to fetch package", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MenuPackage>, t: Throwable) {
                Log.e("API_ERROR", "Error fetching package: ${t.message}")
                Toast.makeText(this@MenusCoveredPackage, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

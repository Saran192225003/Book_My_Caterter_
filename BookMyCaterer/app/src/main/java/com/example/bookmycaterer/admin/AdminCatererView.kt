package com.example.bookmycaterer.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.SampleUserHome
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminCatererView : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adminHomeAdapter: AdminCatererViewAdapter

    var ver: ImageView? = null
    var pro: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_caterer_view)

        recyclerView = findViewById(R.id.catererRecyclerView)

        // Fetch data asynchronously
        getCaterer()

        ver = findViewById(R.id.profileOrders)
        ver?.setOnClickListener {
            val intent = Intent(this@AdminCatererView, ViewVerifications::class.java)
            startActivity(intent)
        }

        pro = findViewById(R.id.profile)
        pro?.setOnClickListener {
            val intent = Intent(this@AdminCatererView, AdminProfile::class.java)
            startActivity(intent)
        }
    }

    private fun getCaterer() {
        val apiService = Retro.instance
        val call = apiService.getApprovedCaterers() // Updated API call

        call.enqueue(object : Callback<List<SampleUserHome>> {
            override fun onResponse(
                call: Call<List<SampleUserHome>>,
                response: Response<List<SampleUserHome>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    // Map SampleUserHome objects to SampleCatererVeiw objects
                    val catererList = response.body()!!.map { userHome ->
                        SampleCatererVeiw(
                            id = userHome._id,
                            name = userHome.Catering_name,
                            imageRes = R.drawable.img_6 // Replace with logic to fetch appropriate images
                        )
                    }

                    // Set up the RecyclerView adapter with fetched data
                    recyclerView.layoutManager = LinearLayoutManager(this@AdminCatererView)
                    adminHomeAdapter = AdminCatererViewAdapter(this@AdminCatererView, catererList)
                    recyclerView.adapter = adminHomeAdapter
                } else {
                    Toast.makeText(
                        this@AdminCatererView,
                        "Failed to fetch caterers: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<SampleUserHome>>, t: Throwable) {
                Toast.makeText(this@AdminCatererView, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

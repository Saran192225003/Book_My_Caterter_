package com.example.bookmycaterer.admin


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.caterer.OrderHistoryAdapter
import com.example.bookmycaterer.user.OrderResponse

import com.example.bookmycaterer.user.RemoveCatererResponse
import com.example.bookmycaterer.user.ReviewData
import com.example.bookmycaterer.user.ReviewsResponse
import com.example.bookmycaterer.user.SampleUserHome
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ViewProfile : AppCompatActivity() {
    private var orderHistoryList: MutableList<OrderResponse> = mutableListOf()
    private var reviewList: MutableList<ReviewData> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderHistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        val catererId = intent.getStringExtra("caterer_id").toString()
        if (catererId.isEmpty()) {
            Toast.makeText(this, "Caterer ID is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize RecyclerView before fetching data
        recyclerView = findViewById(R.id.orderHistoryRecyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = OrderHistoryAdapter(reviewList)

        recyclerView.adapter = adapter

        fetchCatererDetails(catererId)
       fetchOrderHistory(catererId)  // This now uses the initialized recyclerView

        findViewById<Button>(R.id.removeCatererButton).setOnClickListener {
            remove()
        }
    }

    // Fetch Caterer details from the API and update the UI
    private fun fetchCatererDetails(catererId: String) {
        val apiService = Retro.instance
        val call = apiService.getCatererDetails(catererId)

        Log.i("catID", catererId)

        call.enqueue(object : Callback<SampleUserHome> {
            override fun onResponse(
                call: Call<SampleUserHome>,
                response: Response<SampleUserHome>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val caterer = response.body()

                    // Update the UI with the fetched data
                    findViewById<TextView>(R.id.catererNameLabel).text = caterer?.name ?: "N/A"
                    findViewById<TextView>(R.id.serviceNameLabel).text =
                        caterer?.Catering_name ?: "N/A"
                    findViewById<TextView>(R.id.contactLabel).text = caterer?.phone ?: "N/A"
                    findViewById<TextView>(R.id.businessSizeLabel).text =
                        caterer?.Business_Size ?: "N/A"
                    findViewById<TextView>(R.id.cityLabel).text = caterer?.City ?: "N/A"

                    // Handle license (optional: provide functionality for downloading)
                  

                    Log.d("VerifyCaterer", "Caterer details updated in UI.")
                } else {
                    Toast.makeText(
                        this@ViewProfile,
                        "Failed to fetch caterer details: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("VerifyCaterer", "Error fetching details: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SampleUserHome>, t: Throwable) {
                Toast.makeText(this@ViewProfile, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("VerifyCaterer", "Error fetching details: ${t.message}")
            }
        })
    }

    private fun remove() {
        // Fetching the caterer ID from the intent
        val catererId = intent.getStringExtra("caterer_id").toString()

        // Check if the caterer ID is valid
        if (catererId.isEmpty()) {
            Toast.makeText(this, "Caterer ID is missing", Toast.LENGTH_SHORT).show()
            return
        }

        // Show a confirmation dialog before proceeding with the removal
        val apiService = Retro.instance
        val call = apiService.removeCaterer(catererId) // Make the DELETE request

        call.enqueue(object : Callback<RemoveCatererResponse> {
            override fun onResponse(
                call: Call<RemoveCatererResponse>,
                response: Response<RemoveCatererResponse>
            ) {
                if (response.isSuccessful) {
                    // Successful response, you can process the message or data
                    val message = response.body()?.message ?: "Caterer removed successfully"
                    Toast.makeText(this@ViewProfile, message, Toast.LENGTH_SHORT).show()
                    Log.d("RemoveCaterer", message)

                    // Optionally finish the activity after successful removal
                    finish() // Close the activity after successful removal
                } else {
                    // Handle error response
                    Toast.makeText(
                        this@ViewProfile,
                        "Failed to remove caterer: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("RemoveCaterer", "Error removing caterer: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RemoveCatererResponse>, t: Throwable) {
                // Handle failure (e.g., network issues)
                Toast.makeText(
                    this@ViewProfile,
                    "Error removing caterer: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("RemoveCaterer", "Error removing caterer: ${t.message}")
            }
        })
    }

    private fun fetchOrderHistory(catererId: String) {
        val apiService = Retro.instance
        val call = apiService.getReviewsByCatererID(catererId)

        call.enqueue(object : Callback<ReviewsResponse> {
            override fun onResponse(call: Call<ReviewsResponse>, response: Response<ReviewsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    reviewList.clear()
                    reviewList.addAll(response.body()!!.reviews)  // Accessing `reviews` list
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@ViewProfile, "Failed to fetch order history", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                Toast.makeText(this@ViewProfile, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
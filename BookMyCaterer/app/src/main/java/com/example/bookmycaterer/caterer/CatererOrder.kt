package com.example.bookmycaterer.caterer

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatererOrder : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private var ordersList = mutableListOf<SampleOrder>()
    private lateinit var acceptedOrdersButton: Button  // FIX: Declare but initialize inside onCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caterer_order)

        // FIX: Initialize views after setContentView
        acceptedOrdersButton = findViewById(R.id.buttonAcceptedOrders)
        recyclerView = findViewById(R.id.ordersRecyclerView)

        acceptedOrdersButton.setOnClickListener {
            val intent = Intent(this@CatererOrder, AcceptedOrders::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

//        orderAdapter = OrderAdapter(ordersList,
//            onAcceptClick = { order -> Log.d("Order", "Accepted: ${order.eventType}") },
//            onRejectClick = { order -> Log.d("Order", "Rejected: ${order.eventType}") }
//        )
        recyclerView.adapter = orderAdapter

        fetchOrders()
    }

    private fun fetchOrders() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("CatererPrefs", MODE_PRIVATE)
        val catererId = sharedPreferences.getString("caterer_id", null)

        if (catererId == null) {
            Toast.makeText(this, "Caterer ID not found", Toast.LENGTH_SHORT).show()
            return
        }

        val call = Retro.instance.getAllOrders(catererId)
//        call.enqueue(object : Callback<List<OrderResponse>> {
//            override fun onResponse(call: Call<List<OrderResponse>>, response: Response<List<OrderResponse>>) {
//                if (response.isSuccessful) {
//                    response.body()?.let { orderList ->
//                        ordersList.clear()
//                        ordersList.addAll(orderList.map { order ->
//                            SampleOrder(
//                                id = order._id,
//                                eventType = order.event_type,
//                                date = order.date,
//                                location = order.event_location.trim(),
//                                noOfHeads = order.number_of_heads.toIntOrNull() ?: 0, // FIX: Prevent crash
//                                timeSlot = order.time_slot,
//                                totalAmount = 0, // Replace with actual calculation if needed
//                                selectedPackage = order.selected_package ?: "Not Selected",
//                                customizedMenu = order.extra_menu ?: "No Extra Menu"
//                            )
//                        })
//                        orderAdapter.notifyDataSetChanged()
//                    }
//                } else {
//                    Log.e("API_ERROR", "Failed to fetch orders: ${response.errorBody()?.string()}")
//                    Toast.makeText(this@CatererOrder, "Failed to fetch orders", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<List<OrderResponse>>, t: Throwable) {
//                Log.e("API_ERROR", "Network Error: ${t.message}")
//                Toast.makeText(this@CatererOrder, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
    }
}

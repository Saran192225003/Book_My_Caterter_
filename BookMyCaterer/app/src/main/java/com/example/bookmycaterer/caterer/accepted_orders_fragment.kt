package com.example.bookmycaterer.caterer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

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

class accepted_orders_fragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AcceptedOrdersAdapter
    private var acceptedOrdersList: List<SampleOrder> = mutableListOf() // List to hold accepted orders

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_accepted_orders_fragment, container, false)

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.acceptedOrdersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the adapter after RecyclerView is set up
        adapter = AcceptedOrdersAdapter(acceptedOrdersList)
        recyclerView.adapter = adapter

        // Fetch the accepted orders data
        fetchAcceptedOrders()

        return rootView
    }

    private fun fetchAcceptedOrders() {
        val sharedPreferences = requireContext().getSharedPreferences("CatererPrefs", AppCompatActivity.MODE_PRIVATE)
        val catererId = sharedPreferences.getString("caterer_id", null)

        if (catererId == null) {
            Toast.makeText(context, "Caterer ID not found", Toast.LENGTH_SHORT).show()
            return
        }

        val call = Retro.instance.getAcceptedOrders(catererId)
        call.enqueue(object : Callback<List<OrderResponse>> {
            override fun onResponse(call: Call<List<OrderResponse>>, response: Response<List<OrderResponse>>) {
                if (response.isSuccessful) {
                    response.body()?.let { orderList ->
                        acceptedOrdersList = orderList.map { order ->
                            SampleOrder(
                                id = order._id,
                                eventType = order.event_type,
                                date = order.date,
                                location = order.event_location.trim(),
                                noOfHeads = order.number_of_heads.toIntOrNull() ?: 0,
                                timeSlot = order.time_slot,
                                totalAmount = order.price,
                                selectedPackage = order.selected_package ?: "Not Selected",
                                customizedMenu = order.extra_menu ?: "No Extra Menu",
                                contact=order.contact
                            )
                        }
                        adapter.updateList(acceptedOrdersList)
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch accepted orders", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<OrderResponse>>, t: Throwable) {
                Toast.makeText(context, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    companion object {
        @JvmStatic
        fun newInstance() = accepted_orders_fragment()
    }
}

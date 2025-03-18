package com.example.bookmycaterer.caterer

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.OrderAcceptResponse
import com.example.bookmycaterer.user.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class order_request : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private var ordersList = mutableListOf<OrderResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_order_request, container, false)

        recyclerView = rootView.findViewById(R.id.ordersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        orderAdapter = OrderAdapter(requireActivity(),ordersList)
        recyclerView.adapter = orderAdapter

        fetchOrder()

        return rootView
    }

    private fun fetchOrder() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("CatererPrefs", AppCompatActivity.MODE_PRIVATE)
        val catererId = sharedPreferences.getString("caterer_id", null)

        if (catererId == null) {
            Toast.makeText(context, "Caterer ID not found", Toast.LENGTH_SHORT).show()
            return
        }

        val call = Retro.instance.getAllOrders(catererId)

        call.enqueue(object : Callback<List<OrderResponse>> {
            override fun onResponse(call: Call<List<OrderResponse>>, response: Response<List<OrderResponse>>) {
                if (response.isSuccessful) {
                    ordersList.clear()
                    response.body()?.let { ordersList.addAll(it) }
                    orderAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Failed to fetch orders", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<OrderResponse>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}

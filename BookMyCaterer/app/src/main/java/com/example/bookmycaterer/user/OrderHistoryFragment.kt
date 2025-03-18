package com.example.bookmycaterer.user

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderHistoryFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var orderHistoryRecyclerView: RecyclerView
    private lateinit var orderHistoryAdapter: UserOrderHistoryAdapter
    private val ordersList = mutableListOf<OrderResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_order_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE)

        orderHistoryRecyclerView = view.findViewById(R.id.orderHistoryRecyclerView)
        orderHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        orderHistoryAdapter = UserOrderHistoryAdapter(requireContext(), ordersList)
        orderHistoryRecyclerView.adapter = orderHistoryAdapter

        fetchCompletedOrders()
    }

    private fun fetchCompletedOrders() {
        val userId = sharedPreferences.getString("user_id", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "User ID is missing. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        Retro.instance.getCompletedOrders(userId).enqueue(object : Callback<UserOrdersResponse> {
            override fun onResponse(call: Call<UserOrdersResponse>, response: Response<UserOrdersResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val orders = response.body()?.orders ?: emptyList()
                    ordersList.clear()
                    ordersList.addAll(orders)
                    orderHistoryAdapter.notifyDataSetChanged()

                    Log.d("OrderHistoryFragment", "Completed orders fetched successfully")
                } else {
                    Log.e("OrderHistoryFragment", "Failed to fetch orders: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to fetch orders", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserOrdersResponse>, t: Throwable) {
                Log.e("OrderHistoryFragment", "Network Error: ${t.message}")
                Toast.makeText(requireContext(), "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

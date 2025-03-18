package com.example.bookmycaterer.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.PaymentStatusUpdateListener
import com.example.bookmycaterer.api.Retro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderStatusFragment : Fragment(), PaymentStatusUpdateListener {

    private lateinit var orderStatusRecyclerView: RecyclerView
    private lateinit var orderStatusAdapter: OrderStatusAdapter
    private val statusList = mutableListOf<OrderResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_order_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderStatusRecyclerView = view.findViewById(R.id.orderStatusRecyclerView)
        orderStatusRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        orderStatusAdapter = OrderStatusAdapter(requireActivity(), statusList, this)
        orderStatusRecyclerView.adapter = orderStatusAdapter

        fetchUserOrders()
    }

    private fun fetchUserOrders() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "User ID missing. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        Retro.instance.getUserOrders(userId).enqueue(object : Callback<UserOrdersResponse> {
            override fun onResponse(call: Call<UserOrdersResponse>, response: Response<UserOrdersResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    statusList.clear()
                    statusList.addAll(response.body()?.orders ?: emptyList())
                    orderStatusAdapter.notifyDataSetChanged()

                    Log.d("OrderStatus", "Orders updated: ${response.body()?.orders}")
                } else {
                    Log.e("OrderStatusError", "Failed to fetch orders: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to fetch orders", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserOrdersResponse>, t: Throwable) {
                Log.e("OrderStatusError", "Network Error: ${t.localizedMessage}")
                Toast.makeText(requireContext(), "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onPaymentUpdated() {
        fetchUserOrders() // Refresh orders after payment success
    }
}

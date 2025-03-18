package com.example.bookmycaterer.user


import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserOrders : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var orderStatusRecyclerView: RecyclerView
    private lateinit var orderStatusAdapter: OrderStatusAdapter
    private val statusList = mutableListOf<OrderResponse>()


    private lateinit var orderHistoryRecyclerView: RecyclerView
    private lateinit var orderHistoryAdapter: UserOrderHistoryAdapter
    private val ordersList = mutableListOf<OrderResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_orders)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        orderStatusRecyclerView = findViewById(R.id.orderStatusRecyclerView1)
        orderStatusRecyclerView.layoutManager = LinearLayoutManager(this)
      //  orderStatusAdapter = OrderStatusAdapter(this, statusList)
        orderStatusRecyclerView.adapter = orderStatusAdapter

        orderHistoryRecyclerView = findViewById(R.id.orderHistoryRecyclerView1)
        orderHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        orderHistoryAdapter = UserOrderHistoryAdapter(this, ordersList)
        orderHistoryRecyclerView.adapter = orderHistoryAdapter

        fetchUserOrders()
        fetchCompletedOrders()
    }

    private fun fetchCompletedOrders() {
        val userId = sharedPreferences.getString("user_id", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User ID is missing. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        Retro.instance.getCompletedOrders(userId).enqueue(object : Callback<UserOrdersResponse> {
            override fun onResponse(call: Call<UserOrdersResponse>, response: Response<UserOrdersResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val orders = response.body()?.orders ?: emptyList()
                    ordersList.clear()
                    ordersList.addAll(orders)
                    orderHistoryAdapter.notifyDataSetChanged()

//                    updateOrderList(orders)


                    Log.d("UserOrders", "Orders fetched successfully")
                } else {
                    Log.e("UserOrders", "Failed to fetch orders: ${response.errorBody()?.string()}")
                    Toast.makeText(this@UserOrders, "Failed to fetch orders", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserOrdersResponse>, t: Throwable) {
                Log.e("UserOrders", "Network Error: ${t.message}")
                Toast.makeText(this@UserOrders, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchUserOrders() {
        val userId = sharedPreferences.getString("user_id", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User ID is missing. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        Retro.instance.getUserOrders(userId).enqueue(object : Callback<UserOrdersResponse> {
            override fun onResponse(call: Call<UserOrdersResponse>, response: Response<UserOrdersResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val orders = response.body()?.orders ?: emptyList()
                    statusList.clear()
                    statusList.addAll(orders)
                    orderStatusAdapter.notifyDataSetChanged()

//                    updateOrderList(orders)


                    Log.d("UserOrders", "Orders fetched successfully")
                } else {
                    Log.e("UserOrders", "Failed to fetch orders: ${response.errorBody()?.string()}")
                    Toast.makeText(this@UserOrders, "Failed to fetch orders", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserOrdersResponse>, t: Throwable) {
                Log.e("UserOrders", "Network Error: ${t.message}")
                Toast.makeText(this@UserOrders, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateOrderList(newOrders: List<OrderResponse>) {
        runOnUiThread {
            ordersList.clear()
            ordersList.addAll(newOrders)
            orderStatusAdapter.notifyDataSetChanged()
//            orderHistoryAdapter.notifyDataSetChanged()
        }
    }

}
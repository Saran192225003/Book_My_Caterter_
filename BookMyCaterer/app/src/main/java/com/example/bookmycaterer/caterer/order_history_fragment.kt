package com.example.bookmycaterer.caterer

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
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.ReviewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class order_history_fragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_order_history_fragment, container, false)

        recyclerView = rootView.findViewById(R.id.orderHistoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val sharedPreferences = requireActivity().getSharedPreferences("CatererPrefs", Context.MODE_PRIVATE)
        val catererId = sharedPreferences.getString("caterer_id", null)

        Log.d("CATiD", catererId.toString())

        if (catererId != null) {
            fetchOrders(catererId)
        } else {
            Log.e("SharedPreferences", "Caterer ID not found in SharedPreferences!")
        }

        return rootView
    }

    private fun fetchOrders(catererId: String) {
        Retro.instance.getReviewsByCatererID(catererId).enqueue(object : Callback<ReviewsResponse> {
            override fun onResponse(call: Call<ReviewsResponse>, response: Response<ReviewsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val reviewsList = response.body()!!.reviews
                    adapter = OrderHistoryAdapter(reviewsList)
                    recyclerView.adapter = adapter
                    Log.d("API_RESPONSE", "Fetched ${reviewsList.size} reviews")
                } else {
                    Log.e("API_RESPONSE", "Failed to fetch reviews")
                    Toast.makeText(requireContext(), "Failed to load reviews!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error fetching data!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

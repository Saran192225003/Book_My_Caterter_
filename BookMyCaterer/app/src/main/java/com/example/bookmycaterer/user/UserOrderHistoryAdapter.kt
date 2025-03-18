package com.example.bookmycaterer.user

import OrderHistoryData
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R

class UserOrderHistoryAdapter(
    private val context: Context,
    private val orderHistoryList: List<OrderResponse>,
) : RecyclerView.Adapter<UserOrderHistoryAdapter.OrderHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_order_list, parent, false)
        return OrderHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val orderHistory = orderHistoryList[position]
        holder.cateringName.text = orderHistory.catering_name
        holder.date.text = orderHistory.date

        holder.reviewButton.setOnClickListener {
            val intent = Intent(context, Review::class.java).apply {
                putExtra("ORDER_ID", orderHistory._id ?: "")  // Ensure order ID is not null
                putExtra("CATERER_ID", orderHistory.catererId ?: "")  // Ensure caterer ID is not null
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = orderHistoryList.size

    class OrderHistoryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val cateringName: TextView = view.findViewById(R.id.cateringName)
        val date: TextView = view.findViewById(R.id.orderDate)
        val reviewButton: Button = view.findViewById(R.id.reviewButton)

    }
}

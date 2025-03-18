package com.example.bookmycaterer.caterer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.user.ReviewData

class OrderHistoryAdapter(
    private val orderHistoryList: List<ReviewData>
) : RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_history, parent, false)
        return OrderHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val orderHistory = orderHistoryList[position]
        val orderDetails = orderHistory.orderId

        if (orderDetails != null) {
            Log.i("details..", "Price: ${orderDetails.price}, Date: ${orderDetails.date}")

            holder.orderDate.text = "Order Date: ${orderDetails.date}"
            holder.priceText.text = "Price: ₹${orderDetails.price}"
        }

        holder.reviewText.text = "Review: ${orderHistory.write}"
        holder.ratingBarCaterer.rating = orderHistory.rate.toFloat()
    }

    override fun getItemCount(): Int = orderHistoryList.size

    class OrderHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderDate: TextView = itemView.findViewById(R.id.orderDateTextView)
        val priceText: TextView = itemView.findViewById(R.id.packageTextView)
        val reviewText: TextView = itemView.findViewById(R.id.reviewTextView)
        val ratingBarCaterer: RatingBar = itemView.findViewById(R.id.ratingBarCaterer)
    }
}

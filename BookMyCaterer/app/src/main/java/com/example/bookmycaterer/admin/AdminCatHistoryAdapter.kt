package com.example.bookmycaterer.caterer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.user.OrderResponse
import com.example.bookmycaterer.user.ReviewData

class AdminCatHistoryAdapter(
    private val orderHistoryList: List<OrderResponse>
) : RecyclerView.Adapter<AdminCatHistoryAdapter.OrderHistoryViewHolder>() {  // ✅ Corrected this line

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_history, parent, false)
        return OrderHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val orderHistory = orderHistoryList[position]
        holder.bind(orderHistory)

        // If the review exists, update the view
//        orderHistory.review?.let { reviewData ->
//            holder.updateReview(reviewData)
//        }
    }

    override fun getItemCount(): Int = orderHistoryList.size

    // ✅ Moved ViewHolder inside AdminCatHistoryAdapter
    class OrderHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderDate: TextView = itemView.findViewById(R.id.orderDateTextView)
        private val packageText: TextView = itemView.findViewById(R.id.packageTextView)
        private val reviewText: TextView = itemView.findViewById(R.id.reviewTextView)
        private var ratingBarCaterer: RatingBar = itemView.findViewById(R.id.ratingBarCaterer)

        fun bind(orderHistory: OrderResponse) {
            orderDate.text = "Order Date: ${orderHistory.date}"
            packageText.text = "Slot: ${orderHistory.time_slot}"
        }

        fun updateReview(reviewData: ReviewData) {
            reviewText.text = "Review: ${reviewData.write}"
            ratingBarCaterer.rating = reviewData.rate.toFloat()
        }
    }
}

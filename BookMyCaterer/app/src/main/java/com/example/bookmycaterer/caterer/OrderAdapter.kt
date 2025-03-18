package com.example.bookmycaterer.caterer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.OrderAcceptResponse
import com.example.bookmycaterer.user.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderAdapter(private var context: Context,
                   private var orders: MutableList<OrderResponse>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventType: TextView = itemView.findViewById(R.id.eventType)
        val noOfHeads: TextView = itemView.findViewById(R.id.noOfHeads)
        val date: TextView = itemView.findViewById(R.id.date)
        val timeSlot: TextView = itemView.findViewById(R.id.timeSlot)
        val location: TextView = itemView.findViewById(R.id.location)
        val selectedPackage: TextView = itemView.findViewById(R.id.selectedPackage)
        val customizedMenu: TextView = itemView.findViewById(R.id.customizedMenu)
        val totalAmount: TextView = itemView.findViewById(R.id.totalAmount)
        val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
        val rejectButton: Button = itemView.findViewById(R.id.rejectButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_list, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.eventType.text = "Event Type: ${order.event_type}"
        holder.noOfHeads.text = "No. of Heads: ${order.number_of_heads}"
        holder.date.text = "Date: ${order.date}"
        holder.timeSlot.text = "Time Slot: ${order.time_slot}"
        holder.location.text = "Location: ${order.event_location}"
        holder.selectedPackage.text = "Selected Package: ${order.selected_package}"
        holder.customizedMenu.text = "Customized Menu: ${order.extra_menu}"
        holder.totalAmount.text = "Total Estimated Amount: ${order.price}"

        holder.acceptButton.setOnClickListener {
            acceptOrder(order._id, position)
        }

        holder.rejectButton.setOnClickListener {
            rejectOrder(order._id, position)
        }

    }

    private fun rejectOrder(order: String, position: Int) {

        val requestBody = mapOf("isAccepted" to "rejected")

        Retro.instance.rejectOrder(order, requestBody).enqueue(object :
            Callback<OrderAcceptResponse> {
            override fun onResponse(call: Call<OrderAcceptResponse>, response: Response<OrderAcceptResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        Log.d("OrderAdapter", "Order Rejected: ${responseBody.message}")
                        Toast.makeText(context, "Order Rejected", Toast.LENGTH_SHORT).show()
                        orders.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, orders.size)
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("OrderAdapter", "Failed to reject order: $errorMsg")
                    Toast.makeText(context, "Order already rejected", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OrderAcceptResponse>, t: Throwable) {
                Log.e("OrderAdapter", "Network Error: ${t.message}")
                Toast.makeText(context, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun getItemCount(): Int {
        return orders.size
    }

    private fun acceptOrder(order: String, position: Int) {
        
        val requestBody = mapOf("isAccepted" to "accepted")

        Retro.instance.acceptOrder(order, requestBody).enqueue(object :
            Callback<OrderAcceptResponse> {
            override fun onResponse(call: Call<OrderAcceptResponse>, response: Response<OrderAcceptResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        Log.d("OrderAdapter", "Order Accepted: ${responseBody.message}")
                        Toast.makeText(context, "Order Accepted", Toast.LENGTH_SHORT).show()
                        orders.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, orders.size)
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("OrderAdapter", "Failed to accept order: $errorMsg")
                    Toast.makeText(context, "Order already accepted", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OrderAcceptResponse>, t: Throwable) {
                Log.e("OrderAdapter", "Network Error: ${t.message}")
                Toast.makeText(context, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

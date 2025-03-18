package com.example.bookmycaterer.user

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.PaymentStatusUpdateListener
import com.example.bookmycaterer.api.Retro
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderStatusAdapter(
    private val context: Context,
    private val orderList: List<OrderResponse>,
    private val listener: PaymentStatusUpdateListener
) : RecyclerView.Adapter<OrderStatusAdapter.OrderStatusViewHolder>(), PaymentResultListener {

    private var currentOrderId: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderStatusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_order_status, parent, false)
        return OrderStatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderStatusViewHolder, position: Int) {
        val order = orderList[position]

        holder.cateringName.text = order.catering_name
        holder.orderDate.text = order.date
        holder.slot.text = order.time_slot
        holder.totalAmount.text = "₹${order.price}"

        when (order.isAccepted) {
            "accepted" -> {
                holder.payNowButton.visibility = View.VISIBLE
                holder.pendingLay.visibility = View.GONE
            }
            "rejected" -> {
                holder.payNowButton.visibility = View.GONE
                holder.pendingLay.visibility = View.VISIBLE
                holder.pending.text = "Rejected"
                holder.pending.setTextColor(Color.RED)
            }
            else -> {
                holder.payNowButton.visibility = View.GONE
                holder.pendingLay.visibility = View.VISIBLE
            }
        }

        if (order.paymentStatus == "true") {
            holder.payNowButton.visibility = View.GONE
        }

        holder.payNowButton.setOnClickListener {
            currentOrderId = order._id
            startPayment(order.price)
        }
    }

    private fun startPayment(amount: String) {
        val paymentAmount = amount.toDoubleOrNull()
        if (paymentAmount == null || paymentAmount <= 0) {
            Toast.makeText(context, "Invalid payment amount", Toast.LENGTH_LONG).show()
            Log.e("PaymentError", "Invalid amount: $amount")
            return
        }

        val checkout = Checkout()
        checkout.setKeyID("rzp_test_U2XWpODmhRkL0l") // Replace with your actual key

        try {
            val options = JSONObject().apply {
                put("name", "Book A Caterer")
                put("description", "Order Payment")
                put("currency", "INR")
                put("amount", (paymentAmount * 100).toInt()) // Convert to paise
                put("prefill", JSONObject().apply {
                    put("email", "test@example.com")
                    put("contact", "9999999999")
                })
            }

            Checkout.preload(context) // Ensure Razorpay SDK is initialized
            checkout.open(context as Activity?, options) // Make sure activity is an Activity, not Context

        } catch (e: Exception) {
            Log.e("PaymentError", "Error: ${e.localizedMessage}")
            Toast.makeText(context, "Payment error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentSuccess(paymentID: String) {
        Toast.makeText(context, "Payment successful!", Toast.LENGTH_SHORT).show()
        Log.d("PaymentSuccess", "Payment ID: $paymentID")

        if (currentOrderId != null) {
            updatePaymentStatus(paymentID)
        }
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(context, "Payment failed! $response", Toast.LENGTH_SHORT).show()
        Log.e("PaymentError", "Error Code: $code, Response: $response")
    }

    private fun updatePaymentStatus(paymentID: String) {
        val apiService = Retro.instance
        val paymentRequest = PaymentRequest(paymentID)

        apiService.updatePaymentStatus(currentOrderId!!, paymentRequest).enqueue(object :
            Callback<PaymentResponse> {
            override fun onResponse(call: Call<PaymentResponse>, response: Response<PaymentResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Payment Updated Successfully!", Toast.LENGTH_SHORT).show()
                    listener.onPaymentUpdated()
                } else {
                    Toast.makeText(context, "Failed to update payment!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                Toast.makeText(context, "Network error! ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun getItemCount() = orderList.size

    class OrderStatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cateringName: TextView = view.findViewById(R.id.cateringName_ord_status)
        val orderDate: TextView = view.findViewById(R.id.orderDate1)
        val slot: TextView = view.findViewById(R.id.slot)
        val totalAmount: TextView = view.findViewById(R.id.totalAmount)
        val payNowButton: Button = view.findViewById(R.id.payNowButton)
        val pendingLay: LinearLayout = view.findViewById(R.id.pendingLay)
        val pending: TextView = view.findViewById(R.id.txtPending)
    }
}

package com.example.bookmycaterer.caterer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R


class AcceptedOrdersAdapter(private var orders: List<SampleOrder>) :
    RecyclerView.Adapter<AcceptedOrdersAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventType: TextView = view.findViewById(R.id.eventType)
        val date: TextView = view.findViewById(R.id.date)  // Fixed ID
        val location: TextView = view.findViewById(R.id.location)  // Fixed ID
        val noOfHeads: TextView = view.findViewById(R.id.noOfHeads)
        val timeSlot: TextView = view.findViewById(R.id.timeSlot)
        val packageName: TextView = view.findViewById(R.id.selectedPackage)
        val extraMenu: TextView = view.findViewById(R.id.customizedMenu)
        val totalAmount: TextView = view.findViewById(R.id.totalAmount)
        val contact: TextView = view.findViewById(R.id.contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orders_accepted_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.eventType.text = "Event Type: ${order.eventType}"
        holder.date.text = "Date: ${order.date}"
        holder.location.text = "Location: ${order.location}"
        holder.noOfHeads.text = "No. of Heads: ${order.noOfHeads}"
        holder.timeSlot.text = "Time Slot: ${order.timeSlot}"
        holder.packageName.text = "Selected Package: ${order.selectedPackage}"
        holder.extraMenu.text = "Customized Menu: ${order.customizedMenu}"
        holder.totalAmount.text = "Total Estimated Amount: ${order.totalAmount}"
        holder.contact.text = "Contact: ${order.contact}"
    }

    override fun getItemCount(): Int = orders.size

    fun updateList(newOrders: List<SampleOrder>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}

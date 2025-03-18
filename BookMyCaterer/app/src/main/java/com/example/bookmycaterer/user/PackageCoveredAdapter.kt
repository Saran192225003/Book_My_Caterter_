package com.example.bookmycaterer.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R

class PackageCoveredAdapter(private val items: List<String>) :
    RecyclerView.Adapter<PackageCoveredAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMenuItemName: TextView = view.findViewById(R.id.tvMenuItemName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.package_covered_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvMenuItemName.text = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

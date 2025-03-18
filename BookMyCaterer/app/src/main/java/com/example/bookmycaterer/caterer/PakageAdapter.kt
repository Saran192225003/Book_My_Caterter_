package com.example.bookmycaterer.caterer

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.user.MenuPackage
class PakageAdapter(
    private val packageList: List<MenuPackage>,
    private val onUpdateClick: (MenuPackage) -> Unit,
    private val onRemoveClick: (MenuPackage) -> Unit,
) : RecyclerView.Adapter<PakageAdapter.PakageViewHolder>() {

    // ViewHolder class to hold and manage the views
    class PakageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val packageName: TextView = itemView.findViewById(R.id.packagename)
        val buttonUpdate: Button = itemView.findViewById(R.id.buttonUpdatePackage1)
        val buttonRemove: Button = itemView.findViewById(R.id.buttonRemovePackage1)
    }

    // Inflate the layout for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PakageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.package_list_items, parent, false)
        return PakageViewHolder(view)
    }

    // Bind data to each item in the RecyclerView
    override fun onBindViewHolder(holder: PakageViewHolder, position: Int) {
        val packageItem = packageList[position]
        holder.packageName.text = packageItem.package_name

        // Set click listeners for buttons
        holder.buttonUpdate.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdatePackage::class.java)
            // Pass the packageId through the intent
            intent.putExtra("PACKAGE_ID", packageItem._id)  // Pass the correct ID
            holder.itemView.context.startActivity(intent)
        }

        holder.buttonRemove.setOnClickListener { onRemoveClick(packageItem) }
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int = packageList.size
}

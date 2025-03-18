package com.example.bookmycaterer.admin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R // Import the activity for navigation

class AdminCatererViewAdapter(
    private val context: Context, // Add context to the constructor
    private val caterers: List<SampleCatererVeiw>
) : RecyclerView.Adapter<AdminCatererViewAdapter.CatererViewHolder>() {

    class CatererViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catererImage: ImageView = itemView.findViewById(R.id.catererImage)
        val catererName: TextView = itemView.findViewById(R.id.catererName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatererViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_caterer_view_list, parent, false)
        return CatererViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatererViewHolder, position: Int) {
        val caterer = caterers[position]
        holder.catererName.text = caterer.name
        holder.catererImage.setImageResource(caterer.imageRes)

        // Set an OnClickListener for the image to navigate to VerifyCaterer activity
        holder.catererImage.setOnClickListener {
            val intent = Intent(context, ViewProfile::class.java)
            intent.putExtra("caterer_name", caterer.name)
            intent.putExtra("caterer_id", caterer.id)// Pass data (if needed)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return caterers.size
    }
}

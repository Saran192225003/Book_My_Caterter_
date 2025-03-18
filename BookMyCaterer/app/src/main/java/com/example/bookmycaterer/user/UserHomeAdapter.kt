package com.example.bookmycaterer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.user.SampleUserHome
import com.example.bookmycaterer.user.SelectDate
import java.util.Locale

class UserHomeAdapter(
    private val context: Context,
    private var catererList: List<SampleUserHome>
) : RecyclerView.Adapter<UserHomeAdapter.CatererViewHolder>() {

    private var filteredList: List<SampleUserHome> = catererList

    class CatererViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catererName: TextView = itemView.findViewById(R.id.tvCatererName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatererViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_home_list, parent, false)
        return CatererViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatererViewHolder, position: Int) {
        val caterer = filteredList[position]

        holder.catererName.text = caterer.Catering_name
        holder.itemView.setOnClickListener {
            val intent = Intent(context, SelectDate::class.java)
            intent.putExtra("caterer_id", caterer._id)
            intent.putExtra("caterer_name", caterer.Catering_name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = filteredList.size

    // Function to filter results based on search query
    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            catererList
        } else {
            catererList.filter {
                it.Catering_name.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }
        }
        notifyDataSetChanged()
    }
}

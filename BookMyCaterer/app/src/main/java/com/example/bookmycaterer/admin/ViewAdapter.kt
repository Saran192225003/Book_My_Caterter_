package com.example.bookmycaterer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.admin.SampleCaterer
import com.example.bookmycaterer.admin.VerifyCaterer

class ViewAdapter(
    private val context: Context,
    private val catererList: List<SampleCaterer>
) : RecyclerView.Adapter<ViewAdapter.ViewHolder>() {

    // ViewHolder class for binding views
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewCatererName: TextView = view.findViewById(R.id.textViewCatererName)
        val buttonView: Button = view.findViewById(R.id.buttonView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val caterer = catererList[position]
        holder.textViewCatererName.text = caterer.name // Displaying only the catering name

        // Navigate to VerifyCaterer page on button click
        holder.buttonView.setOnClickListener {
            val intent = Intent(context, VerifyCaterer::class.java)
            intent.putExtra("caterer_id", caterer.id) // Pass the caterer's ID
            intent.putExtra("caterer_name", caterer.name) // Pass the catering name
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int = catererList.size
}

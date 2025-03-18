package com.example.bookmycaterer.caterer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.user.MenuItem

class MenuAdapter(private val menuList: List<MenuItem>) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuName: TextView = view.findViewById(R.id.txtMenuName)
        val menuPrice: TextView = view.findViewById(R.id.textViewMenuPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_list, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = menuList[position]
        holder.menuName.text = menu.item_name
        holder.menuPrice.text = "Price: ₹${menu.price}"
    }

    override fun getItemCount(): Int {
        return menuList.size
    }
}

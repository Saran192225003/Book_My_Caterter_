package com.example.bookmycaterer.caterer


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OrdersPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> order_request() // Show the OrderRequestFragment
            1 -> accepted_orders_fragment() // Show the AcceptedOrdersFragment
            2 -> order_history_fragment() // Show the OrderHistoryFragment
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}

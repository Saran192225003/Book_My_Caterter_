package com.example.bookmycaterer.user

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class UserOrdersPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OrderStatusFragment()
            1 -> OrderHistoryFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}

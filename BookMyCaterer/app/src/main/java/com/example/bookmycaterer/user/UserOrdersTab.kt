package com.example.bookmycaterer.user

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.bookmycaterer.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.razorpay.PaymentResultListener

class UserOrdersTab : AppCompatActivity()
//    , PaymentResultListener
{

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: UserOrdersPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_orders_tab)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        adapter = UserOrdersPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Order Status"
                1 -> "Order History"
                else -> ""
            }
        }.attach()
    }

//    override fun onPaymentSuccess(paymentID: String) {
//        Toast.makeText(this, "Payment Successful! ID: $paymentID", Toast.LENGTH_LONG).show()
//        refreshOrderStatus()
//    }
//
//    override fun onPaymentError(code: Int, response: String?) {
//        Toast.makeText(this, "Payment Failed! Code: $code, Response: $response", Toast.LENGTH_LONG).show()
//    }
//
//    fun refreshOrderStatus() {
//        val fragment = supportFragmentManager.fragments.find { it is OrderStatusFragment } as? OrderStatusFragment
//        fragment?.fetchUserOrders()
//    }
}

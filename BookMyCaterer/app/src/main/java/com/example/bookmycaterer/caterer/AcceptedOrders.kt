package com.example.bookmycaterer.caterer


import SampleOrderHistory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R

class AcceptedOrders : AppCompatActivity() {

    private lateinit var acceptedOrdersRecyclerView: RecyclerView
    private lateinit var orderHistoryRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accepted_orders)

        // Initialize RecyclerViews
        acceptedOrdersRecyclerView = findViewById(R.id.acceptedOrdersRecyclerView)
        orderHistoryRecyclerView = findViewById(R.id.orderHistoryRecyclerView)

        // Set Layout Managers
        acceptedOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        orderHistoryRecyclerView.layoutManager = LinearLayoutManager(this)

        // Sample Data for Accepted Orders
      /*  val ordersList = listOf(
            SampleOrder(
                "Wedding", "2024-12-25", "Downtown Hall", 150,
                "Dinner", 10000, "Premium Package", "Salad, Pasta, Chicken"
            ),
            SampleOrder(
                "Corporate Event", "2024-12-30", "City Center", 500,
                "Lunch", 30000, "Basic Package", "Sandwiches, Juice"
            )
        )
*/
        // Sample Data for Order History
        val orderHistory = listOf(
            SampleOrderHistory(
                "2024-12-01",
                "Premium Package",
                "Excellent service!",
                5.0f
            ),
        )


       /* val acceptedOrdersAdapter = AcceptedOrdersAdapter(ordersList)
        acceptedOrdersRecyclerView.adapter = acceptedOrdersAdapter*/

       // val orderHistoryAdapter = OrderHistoryAdapter(orderHistory)
        //orderHistoryRecyclerView.adapter = orderHistoryAdapter
    }
}

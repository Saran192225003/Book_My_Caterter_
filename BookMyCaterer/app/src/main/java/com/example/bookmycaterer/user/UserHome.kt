package com.example.bookmycaterer.user

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.adapter.UserHomeAdapter
import com.example.bookmycaterer.api.Retro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserHome : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var catererList: List<SampleUserHome> = listOf()
    private lateinit var userHomeAdapter: UserHomeAdapter
    private var orders: ImageView? = null
    private var profile: ImageView? = null
    private var search: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        recyclerView = findViewById(R.id.rvCaterers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        orders = findViewById(R.id.profileOrders)
        search = findViewById(R.id.searchBar)
        profile = findViewById(R.id.profile)

        orders?.setOnClickListener {
            startActivity(Intent(this@UserHome, UserOrdersTab::class.java))
        }

        profile?.setOnClickListener {
            startActivity(Intent(this@UserHome, UserProfile::class.java))
        }

        getCaterer()

        // Set up the SearchView listener
        search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                userHomeAdapter.filter(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userHomeAdapter.filter(newText ?: "")
                return true
            }
        })
    }

    private fun getCaterer() {
        val call = Retro.instance.getApprovedCaterers()

        call.enqueue(object : Callback<List<SampleUserHome>> {
            override fun onResponse(call: Call<List<SampleUserHome>>, response: Response<List<SampleUserHome>>) {
                if (response.isSuccessful && response.body() != null) {
                    catererList = response.body()!!
                    userHomeAdapter = UserHomeAdapter(this@UserHome, catererList)
                    recyclerView.adapter = userHomeAdapter
                } else {
                    Toast.makeText(this@UserHome, "Failed to fetch caterers: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<SampleUserHome>>, t: Throwable) {
                Toast.makeText(this@UserHome, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

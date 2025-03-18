package com.example.bookmycaterer.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R
import com.example.bookmycaterer.ViewAdapter
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.SampleUserHome
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewVerifications : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adminHomeAdapter: ViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_verifications)

        recyclerView = findViewById(R.id.recyclerViewCaterers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch caterer list from the API
        getCaterer()
    }

    private fun getCaterer() {
        val apiService = Retro.instance
        val call = apiService.getCaterers()

        call.enqueue(object : Callback<List<SampleUserHome>> {
            override fun onResponse(
                call: Call<List<SampleUserHome>>,
                response: Response<List<SampleUserHome>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val catererList = response.body()!!.map { userHome ->
                        SampleCaterer(
                            id = userHome._id,
                            name = userHome.Catering_name // Only fetching Catering_name
                        )
                    }

                    // Update RecyclerView with the list of catering names
                    adminHomeAdapter = ViewAdapter(this@ViewVerifications, catererList)
                    recyclerView.adapter = adminHomeAdapter
                } else {
                    Toast.makeText(
                        this@ViewVerifications,
                        "Error: ${response.code()} - ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<SampleUserHome>>, t: Throwable) {
                Toast.makeText(this@ViewVerifications, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}

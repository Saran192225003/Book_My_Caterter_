package com.example.bookmycaterer.user

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Review : AppCompatActivity() {

    var reviewEditText: EditText? = null
    var ratingBar: RatingBar? = null
    var orderId = ""
    var catererId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        // Initialize views
        reviewEditText = findViewById(R.id.reviewEditText)
        ratingBar = findViewById(R.id.ratingBar)
        val submitReviewButton: Button = findViewById(R.id.submitReviewButton)

        // Retrieve orderId and catererId from the Intent
        orderId = intent.getStringExtra("ORDER_ID") ?: ""
        catererId = intent.getStringExtra("CATERER_ID") ?: ""

        Log.d("ReviewActivity", "Received Order ID: $orderId, Caterer ID: $catererId")

        // Check if orderId or catererId are missing
        if (orderId.isEmpty() || catererId.isEmpty()) {
            Toast.makeText(this, "Missing order or caterer ID.", Toast.LENGTH_LONG).show()
            finish() // Close the activity if IDs are missing
            return
        }

        // Handle submit button click
        submitReviewButton.setOnClickListener {
          //  postReview()
        }
    }

    // Function to post the review to the backend API
//    private fun postReview() {
//        val reviewText = reviewEditText?.text.toString()
//        val rating = ratingBar?.rating?.toDouble()
//
//        // Validate review input
//        if (reviewText.isEmpty()) {
//            Toast.makeText(this, "Please enter a review.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if (rating == 0.0) {
//            Toast.makeText(this, "Please rate your experience.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        // Validate orderId and catererId
//        if (orderId.isEmpty() || catererId.isEmpty()) {
//            Toast.makeText(this, "Invalid order or caterer ID", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        // Create the review data to send in the request body
//        val reviewData = rating?.let {
//            ReviewData(
//                orderId = orderId,
//                catererId = catererId,
//                write = reviewText,
//                rate = it
//            )
//        }
//
//        // Make the API call
//        val apiService = Retro.instance
//        val call = reviewData?.let { apiService.postReview(orderId, catererId, it) }
//
//        // Enqueue the request
//        call?.enqueue(object : Callback<ReviewResponse> {
//            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
//                if (response.isSuccessful) {
//                    Toast.makeText(this@Review, "Review submitted successfully.", Toast.LENGTH_SHORT).show()
//                    Log.d("Review", "Review submitted: ${response.body()}")
//                    finish() // Close the activity after submission
//                } else {
//                    Toast.makeText(this@Review, "Failed to submit review: ${response.message()}", Toast.LENGTH_SHORT).show()
//                    Log.e("Review", "Error: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
//                Toast.makeText(this@Review, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//                Log.e("Review", "Error submitting review: ${t.message}")
//            }
//        })
//    }
}

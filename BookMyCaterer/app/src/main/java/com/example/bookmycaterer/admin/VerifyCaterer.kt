package com.example.bookmycaterer.admin

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.ApproveResponse
import com.example.bookmycaterer.user.RejectRequest
import com.example.bookmycaterer.user.RejectResponse
import com.example.bookmycaterer.user.SampleUserHome
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyCaterer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_caterer)

        val approveButton: Button = findViewById(R.id.approveButton)
        val rejectButton: Button = findViewById(R.id.rejectButton)

        // Retrieve catererId from intent
        val catererId = intent.getStringExtra("caterer_id").toString()
        Log.i("VerifyCaterer", "Caterer ID: $catererId")

        if (catererId.isEmpty()) {
            Log.e("VerifyCaterer", "Caterer ID is missing")
            Toast.makeText(this, "Caterer ID is missing", Toast.LENGTH_SHORT).show()
            finish() // Optionally close the activity if ID is not provided
            return
        }

        // Fetch the caterer details using the catererId
        fetchCatererDetails(catererId)

        // Handle Approve button click
        approveButton.setOnClickListener {
            Toast.makeText(this, "Caterer Approved", Toast.LENGTH_SHORT).show()
            approve()

        }

        // Handle Reject button click
        rejectButton.setOnClickListener {

            showInputDialog("Enter Reason for Rejecting") { input ->
                Toast.makeText(this, "Rejected with Reason: $input", Toast.LENGTH_SHORT).show()

            }

            reject()
        }
    }

    // Function to show a dialog with a title and input field
    private fun showInputDialog(title: String, onDone: (String) -> Unit) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.approve, null)
        val dialogTitle: TextView = dialogView.findViewById(R.id.dialogTitle)
        val dialogInput: EditText = dialogView.findViewById(R.id.dialogInput)
        val dialogDoneButton: Button = dialogView.findViewById(R.id.dialogDoneButton)

        dialogTitle.text = title

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogDoneButton.setOnClickListener {
            val inputText = dialogInput.text.toString()
            if (inputText.isNotEmpty()) {
                onDone(inputText)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Input cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    // Function to fetch caterer details by ID
    // Function to fetch caterer details by ID
    private fun fetchCatererDetails(catererId: String) {
        val apiService = Retro.instance
        val call = apiService.getCatererDetails(catererId)

        call.enqueue(object : Callback<SampleUserHome> {
            override fun onResponse(
                call: Call<SampleUserHome>,
                response: Response<SampleUserHome>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val caterer = response.body()

                    // Update the UI with the fetched data
                    findViewById<TextView>(R.id.catererNameValue).text = caterer?.name ?: "N/A"
                    findViewById<TextView>(R.id.cateringNameValue).text = caterer?.Catering_name ?: "N/A"
                    findViewById<TextView>(R.id.contactValue).text = caterer?.phone ?: "N/A"
                    findViewById<TextView>(R.id.businessSizeValue).text = caterer?.Business_Size ?: "N/A"
                    findViewById<TextView>(R.id.cityValue).text = caterer?.City ?: "N/A"

                    // Handle license (optional: provide functionality for downloading)
                    findViewById<Button>(R.id.fssaiDownloadButton).setOnClickListener {
                        // Assuming license is a URL path; you can implement download functionality here
                        Toast.makeText(
                            this@VerifyCaterer,
                            "License Path: ${caterer?.license}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    Log.d("VerifyCaterer", "Caterer details updated in UI.")
                } else {
                    Toast.makeText(
                        this@VerifyCaterer,
                        "Failed to fetch caterer details: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("VerifyCaterer", "Error fetching details: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SampleUserHome>, t: Throwable) {
                Toast.makeText(this@VerifyCaterer, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("VerifyCaterer", "Error fetching details: ${t.message}")
            }
        })
    }

    private fun approve() {
        val apiService = Retro.instance
        val catererId = intent.getStringExtra("caterer_id").toString()

        if (catererId.isEmpty()) {
            Toast.makeText(this, "Caterer ID is missing", Toast.LENGTH_SHORT).show()
            return
        }

        val call = apiService.approveCaterer(catererId)

        call.enqueue(object : Callback<ApproveResponse> {
            override fun onResponse(call: Call<ApproveResponse>, response: Response<ApproveResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    Toast.makeText(
                        this@VerifyCaterer,
                        responseBody?.message ?: "Caterer approved successfully!",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Optionally update the UI or finish the activity
                    Log.d("VerifyCaterer", "Caterer approved: ${responseBody?.updatedCaterer}")
                } else {
                    Toast.makeText(
                        this@VerifyCaterer,
                        "Failed to approve caterer: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("VerifyCaterer", "Error approving caterer: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApproveResponse>, t: Throwable) {
                Toast.makeText(this@VerifyCaterer, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("VerifyCaterer", "Error approving caterer: ${t.message}")
            }
        })
    }
    private fun reject() {
        val apiService = Retro.instance
        val catererId = intent.getStringExtra("caterer_id").toString()

        if (catererId.isEmpty()) {
            Toast.makeText(this, "Caterer ID is missing", Toast.LENGTH_SHORT).show()
            return
        }

        // Show dialog to get the reject reason
        showInputDialog("Enter Reason for Rejecting") { reason ->
            val rejectRequest = RejectRequest(reason)

            val call = apiService.rejectCaterer(catererId, rejectRequest)

            call.enqueue(object : Callback<RejectResponse> {
                override fun onResponse(call: Call<RejectResponse>, response: Response<RejectResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()
                        Toast.makeText(
                            this@VerifyCaterer,
                            responseBody?.message ?: "Caterer rejected successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Optionally update the UI or finish the activity
                        Log.d("VerifyCaterer", "Caterer rejected: ${responseBody?.updatedCaterer}")
                    } else {
                        Toast.makeText(
                            this@VerifyCaterer,
                            "Failed to reject caterer: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("VerifyCaterer", "Error rejecting caterer: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<RejectResponse>, t: Throwable) {
                    Toast.makeText(this@VerifyCaterer, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("VerifyCaterer", "Error rejecting caterer: ${t.message}")
                }
            })
        }
    }



}

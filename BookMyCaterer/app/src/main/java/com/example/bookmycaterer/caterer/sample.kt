//package com.example.bookmycaterer.caterer
//
//import android.app.AlertDialog
//import android.graphics.Color
//import android.os.Bundle
//import android.util.Log
//import android.widget.*
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import com.example.bookmycaterer.R
//import com.example.bookmycaterer.api.Retro
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.text.SimpleDateFormat
//import java.util.*
//
//class sample : AppCompatActivity() {
//
//    private lateinit var calendarView: CalendarView
//    private lateinit var radioGroupTimeSlots: RadioGroup
//    private lateinit var buttonSave: Button
//    private var selectedDate: String = ""
//    private var selectedTimeSlot: String = ""
//    private var unavailableSlots: MutableMap<String, MutableList<String>> = mutableMapOf() // Stores unavailable dates and time slots
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_caterer_availability)
//
//        calendarView = findViewById(R.id.calendarView)
//        radioGroupTimeSlots = findViewById(R.id.radioGroupTimeSlots)
//        buttonSave = findViewById(R.id.buttonSave)
//
//        // Get catererId from SharedPreferences
//        val sharedPreferences = getSharedPreferences("CatererPrefs", MODE_PRIVATE)
//        val catererId = sharedPreferences.getString("caterer_id", null)
//
//        // Fetch unavailable dates
//        if (catererId != null) {
//            fetchUnavailableSlots(catererId)
//        }
//
//        // Handle Calendar Date Selection
//        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
//            val formattedMonth = String.format("%02d", month + 1) // Month is 0-based
//            val formattedDay = String.format("%02d", dayOfMonth)
//            selectedDate = "$formattedDay/$formattedMonth/$year"
//            Log.d("SelectedDate", selectedDate)
//
//            // Disable already booked time slots for selected date
//            updateTimeSlotAvailability(selectedDate)
//        }
//
//        // Handle Time Slot Selection
//        radioGroupTimeSlots.setOnCheckedChangeListener { _, checkedId ->
//            selectedTimeSlot = when (checkedId) {
//                R.id.radioBreakfast -> "breakfast"
//                R.id.radioLunch -> "lunch"
//                R.id.radioDinner -> "dinner"
//                else -> ""
//            }
//            Log.d("SelectedTimeSlot", selectedTimeSlot)
//        }
//
//        // Handle Save Button Click
//        buttonSave.setOnClickListener {
//            if (catererId != null && selectedDate.isNotEmpty() && selectedTimeSlot.isNotEmpty()) {
//                createAvailability(catererId, selectedDate, selectedTimeSlot)
//            } else {
//                Toast.makeText(this, "Please select a date and time slot!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    // 🟢 API Call to Fetch Unavailable Slots
//    private fun fetchUnavailableSlots(catererId: String) {
//        val apiService = Retro.instance
//        apiService.getAllAvailability(catererId).enqueue(object : Callback<List<AvailabilityResponse>> {
//            override fun onResponse(call: Call<List<AvailabilityResponse>>, response: Response<List<AvailabilityResponse>>) {
//                if (response.isSuccessful) {
//                    val availabilityList = response.body() ?: emptyList()
//                    unavailableSlots.clear()
//
//                    for (item in availabilityList) {
//                        val date = item.date
//                        val timeSlot = item.time_slot
//
//                        if (!unavailableSlots.containsKey(date)) {
//                            unavailableSlots[date] = mutableListOf()
//                        }
//                        unavailableSlots[date]?.add(timeSlot)
//                    }
//
//                    Log.d("API_SUCCESS", "Unavailable Slots: $unavailableSlots")
//                } else {
//                    Log.e("API_FAILURE", "Failed to fetch availability: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<AvailabilityResponse>>, t: Throwable) {
//                Log.e("API_ERROR", "Error: ${t.message}")
//            }
//        })
//    }
//
//    // 🔴 Disable Already Selected Time Slots
//    private fun updateTimeSlotAvailability(date: String) {
//        val unavailableTimes = unavailableSlots[date] ?: emptyList()
//
//        // Reset all radio buttons
//        findViewById<RadioButton>(R.id.radioBreakfast).isEnabled = true
//        findViewById<RadioButton>(R.id.radioLunch).isEnabled = true
//        findViewById<RadioButton>(R.id.radioDinner).isEnabled = true
//
//        // Disable already selected time slots
//        for (time in unavailableTimes) {
//            when (time) {
//                "breakfast" -> findViewById<RadioButton>(R.id.radioBreakfast).isEnabled = false
//                "lunch" -> findViewById<RadioButton>(R.id.radioLunch).isEnabled = false
//                "dinner" -> findViewById<RadioButton>(R.id.radioDinner).isEnabled = false
//            }
//        }
//    }
//
//    // 🟢 API Call to Create Availability
//    private fun createAvailability(catererId: String, date: String, timeSlot: String) {
//        val apiService = Retro.instance
//        val requestBody = hashMapOf(
//            "date" to date,
//            "time_slot" to timeSlot
//        )
//
//        apiService.createAvailability(catererId, requestBody).enqueue(object : Callback<Map<String, Any>> {
//            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
//                if (response.isSuccessful) {
//                    Log.d("API_SUCCESS", "Availability Created: ${response.body()}")
//                    showSuccessDialog("Availability successfully marked!")
//                    unavailableSlots[date]?.add(timeSlot) ?: unavailableSlots.put(date, mutableListOf(timeSlot))
//                    updateTimeSlotAvailability(date) // Update UI
//                } else {
//                    Log.e("API_FAILURE", "Failed to create availability: ${response.errorBody()?.string()}")
//                    Toast.makeText(this@CatererAvailability, "Failed to mark availability!", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
//                Log.e("API_ERROR", "Error: ${t.message}")
//                Toast.makeText(this@CatererAvailability, "Network error!", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    // 🟢 Show Success Dialog
//    private fun showSuccessDialog(message: String) {
//        AlertDialog.Builder(this)
//            .setTitle("Success")
//            .setMessage(message)
//            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
//            .show()
//    }
//}
//
//// 🟢 Data Model for API Response
//data class AvailabilityResponse(
//    val _id: String,
//    val catererId: String,
//    val date: String,
//    val time_slot: String
//)

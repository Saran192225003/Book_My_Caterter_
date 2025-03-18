package com.example.bookmycaterer.caterer

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.AvailabilityRequest
import com.example.bookmycaterer.user.AvailabilityResponse
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CatererAvailability : AppCompatActivity() {

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var buttonSave: Button
    private lateinit var radioBreakfast: CheckBox
    private lateinit var radioLunch: CheckBox
    private lateinit var radioDinner: CheckBox

    private var unavailableSlots: MutableMap<String, MutableSet<String>> = mutableMapOf()
    private var selectedDate: String = ""
    private val selectedTimeSlots: MutableSet<String> = mutableSetOf()
    private var catererId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caterer_availability)

        calendarView = findViewById(R.id.calendarView)
        buttonSave = findViewById(R.id.buttonSave)
        radioBreakfast = findViewById(R.id.radioBreakfast)
        radioLunch = findViewById(R.id.radioLunch)
        radioDinner = findViewById(R.id.radioDinner)

        val sharedPreferences = getSharedPreferences("CatererPrefs", MODE_PRIVATE)
        catererId = sharedPreferences.getString("caterer_id", null)

        catererId?.let { fetchUnavailableSlots(it) }

        calendarView.setOnDateChangedListener { _, date, _ ->
            selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date.date)
            updateCheckBoxes(selectedDate)
        }

        buttonSave.setOnClickListener {
            selectedTimeSlots.clear()
            if (radioBreakfast.isChecked && radioBreakfast.isEnabled) selectedTimeSlots.add("breakfast")
            if (radioLunch.isChecked && radioLunch.isEnabled) selectedTimeSlots.add("lunch")
            if (radioDinner.isChecked && radioDinner.isEnabled) selectedTimeSlots.add("dinner")

            if (catererId != null && selectedDate.isNotEmpty() && selectedTimeSlots.isNotEmpty()) {
                createAvailability(catererId!!, selectedDate, selectedTimeSlots)
            } else {
                showError("Please select a date and at least one available time slot!")
            }
        }
    }

    /**
     * API Call: Create Availability for additional time slots
     */
    private fun createAvailability(catererId: String, date: String, timeSlots: Set<String>) {
        val apiService = Retro.instance
        val unavailableTimes = unavailableSlots[date] ?: mutableSetOf()

        timeSlots.filterNot { it in unavailableTimes }.forEach { timeSlot ->
            val request = AvailabilityRequest(date, timeSlot)

            apiService.createAvailability(catererId, request).enqueue(object : Callback<AvailabilityResponse> {
                override fun onResponse(call: Call<AvailabilityResponse>, response: Response<AvailabilityResponse>) {
                    if (response.isSuccessful) {
                        unavailableSlots.getOrPut(date) { mutableSetOf() }.add(timeSlot)
                        updateCheckBoxes(date)
                        highlightUnavailableDates()
                        showSuccessDialog("Time slot '$timeSlot' successfully added for $date!")
                    } else {
                        showError("Failed to mark availability! ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<AvailabilityResponse>, t: Throwable) {
                    showError("Network error!")
                }
            })
        }
    }

    /**
     * API Call: Fetch Unavailable Slots
     */
    private fun fetchUnavailableSlots(catererId: String) {
        val apiService = Retro.instance
        apiService.getAllAvailability(catererId).enqueue(object : Callback<List<AvailabilityResponse>> {
            override fun onResponse(call: Call<List<AvailabilityResponse>>, response: Response<List<AvailabilityResponse>>) {
                if (response.isSuccessful) {
                    unavailableSlots.clear()
                    response.body()?.forEach {
                        unavailableSlots.getOrPut(it.date) { mutableSetOf() }.add(it.time_slot)
                    }
                    highlightUnavailableDates()
                } else {
                    showError("Failed to fetch availability!")
                }
            }

            override fun onFailure(call: Call<List<AvailabilityResponse>>, t: Throwable) {
                showError("Network error!")
            }
        })
    }

    /**
     * Highlight Partially Unavailable Dates on Calendar
     */
    private fun highlightUnavailableDates() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val decorators = unavailableSlots.keys.mapNotNull { date ->
            try {
                val parsedDate = dateFormat.parse(date)
                parsedDate?.let { PartialUnavailableDateDecorator(CalendarDay.from(it)) }
            } catch (e: Exception) {
                Log.e("DateParseError", "Error parsing date: $date")
                null
            }
        }

        runOnUiThread {
            calendarView.invalidateDecorators() // Refresh decorators
            calendarView.addDecorators(*decorators.toTypedArray())
        }
    }

    /**
     * Decorator for Partially Unavailable Dates
     */
    inner class PartialUnavailableDateDecorator(private val date: CalendarDay) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean = day == date
        override fun decorate(view: DayViewFacade) {
            view.addSpan(DotSpan(10f, Color.RED)) // Show red dot, but keep date selectable
        }
    }

    /**
     * Update Checkboxes Based on Unavailable Slots
     */
    private fun updateCheckBoxes(date: String) {
        val unavailableTimes = unavailableSlots[date] ?: emptySet()

        setCheckBoxState(radioBreakfast, "breakfast" in unavailableTimes)
        setCheckBoxState(radioLunch, "lunch" in unavailableTimes)
        setCheckBoxState(radioDinner, "dinner" in unavailableTimes)
    }

    /**
     * Enable/Disable Checkboxes Based on Time Slots
     */
    private fun setCheckBoxState(checkBox: CheckBox, isUnavailable: Boolean) {
        checkBox.isChecked = isUnavailable
        checkBox.isEnabled = !isUnavailable
        checkBox.setTextColor(if (isUnavailable) Color.GRAY else Color.BLACK)
    }

    /**
     * Show Success Dialog
     */
    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    /**
     * Show Error Toast
     */
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

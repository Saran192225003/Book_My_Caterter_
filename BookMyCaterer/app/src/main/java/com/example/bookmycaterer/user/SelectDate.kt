package com.example.bookmycaterer.user

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SelectDate : AppCompatActivity() {

    private lateinit var edtDate: EditText
    private lateinit var edtTime: Spinner
    private lateinit var edtPhone: EditText
    private lateinit var edtHead: EditText
    private lateinit var edtLocation: EditText
    private lateinit var eventType: Spinner
    private lateinit var edtPack: Spinner
    private lateinit var edtMenu: Spinner
    private lateinit var txtCatererName: TextView
    private lateinit var price: TextView
    private lateinit var noHeads: TextView
    private lateinit var pricePerHeadText: TextView
    private lateinit var totalPriceText: TextView
    private var cID = ""
    private var catererName = ""
    private var total = ""
    private val extraMenuList = mutableListOf<String>()
    private val selectedExtraMenus = mutableListOf<String>()

    private val bookedSlots = mutableListOf<BookedSlot>()


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_date)

        edtDate = findViewById(R.id.datePicker)
        edtTime = findViewById(R.id.timeSlotSpinner)
        edtPhone = findViewById(R.id.contactEditText)
        edtHead = findViewById(R.id.numberOfHeadsEditText)
        edtMenu = findViewById(R.id.extraMenuSpinner)
        edtPack = findViewById(R.id.packageSpinner)
        edtLocation = findViewById(R.id.eventLocationEditText)
        eventType = findViewById(R.id.eventTypeSpinner)
        txtCatererName = findViewById(R.id.catererLabel)

        cID = intent.getStringExtra("caterer_id").toString()
        catererName = intent.getStringExtra("caterer_name").toString()
        txtCatererName.text = catererName

        fetchExtraMenu(cID)
        fetchPackage(cID)

        edtDate.setOnClickListener {
            showDatePicker()
        }


        edtTime = findViewById(R.id.timeSlotSpinner)

        val timeSlots = listOf("Select Time Slot", "Breakfast", "Lunch", "Dinner")

        val timeSlotAdapter = object : ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_dropdown_item, timeSlots
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view as TextView

                if (position == 0) {
                    textView.setTextColor(android.graphics.Color.parseColor("#2A6B93"))
                } else {
                    textView.setTextColor(android.graphics.Color.parseColor("#000000"))
                }
                return view
            }
        }

        edtTime.adapter = timeSlotAdapter
        edtTime.setSelection(0)

        edtTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    (parent?.getChildAt(0) as? TextView)?.setTextColor(android.graphics.Color.parseColor("#A0A0A0"))
                } else {
                    (parent?.getChildAt(0) as? TextView)?.setTextColor(android.graphics.Color.parseColor("#000000"))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val eventTypes = listOf("Select Event Type", "Marriage", "Birthday", "Corporate Party")

        val eventAdapter = object : ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_dropdown_item, eventTypes
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                if (position == 0) {
                    (view as TextView).setTextColor(android.graphics.Color.parseColor("#2A6B93"))
                } else {
                    (view as TextView).setTextColor(android.graphics.Color.parseColor("#000000"))
                }
                return view
            }
        }

        eventType.adapter = eventAdapter
        eventType.setSelection(0)


        edtMenu.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                showMultiSelectMenuDialog()
            }
            true
        }

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            showCateringDetailsDialog()
        }
    }



    private fun fetchBookedSlots(catererId: String) {
        Retro.instance.getBookedSlots(catererId).enqueue(object : Callback<List<BookedSlot>> {
            override fun onResponse(call: Call<List<BookedSlot>>, response: Response<List<BookedSlot>>) {
                if (response.isSuccessful) {
                    bookedSlots.clear()
                    bookedSlots.addAll(response.body() ?: emptyList())
                    disableUnavailableTimeSlots()
                } else {
                    Toast.makeText(this@SelectDate, "Failed to fetch booked slots", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<BookedSlot>>, t: Throwable) {
                Toast.makeText(this@SelectDate, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun disableUnavailableTimeSlots() {
        val selectedDate = edtDate.text.toString().trim()

        val bookedTimeSlots = bookedSlots
            .filter { it.date == selectedDate }  // Filter by selected date
            .map { it.time_slot.lowercase() }    // Extract booked time slots

        val timeSlots = listOf("Select Time Slot", "Breakfast", "Lunch", "Dinner")

        val timeSlotAdapter = object : ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_dropdown_item, timeSlots
        ) {
            override fun isEnabled(position: Int): Boolean {
                if (position == 0) return false // Disable "Select Time Slot"

                val timeSlot = timeSlots[position].lowercase()
                return !bookedTimeSlots.contains(timeSlot) // Disable if booked
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                val timeSlot = timeSlots[position].lowercase()

                if (position == 0 || bookedTimeSlots.contains(timeSlot)) {
                    view.setTextColor(android.graphics.Color.GRAY)  // Disable booked slots
                } else {
                    view.setTextColor(android.graphics.Color.BLACK)
                }
                return view
            }
        }

        edtTime.adapter = timeSlotAdapter
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
            val selectedDate = "$day/${month + 1}/$year"

            // Check if the selected date is fully booked
            if (bookedSlots.any { it.date == selectedDate }) {
                Toast.makeText(this, "This date is fully booked. Choose another.", Toast.LENGTH_SHORT).show()
            } else {
                edtDate.setText(selectedDate)
                disableUnavailableTimeSlots()
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000 // Disable past dates
        datePickerDialog.show()
    }



    private var isDialogShowing = false
    private fun showMultiSelectMenuDialog() {
        if (extraMenuList.isEmpty()) {
            Toast.makeText(this, "No extra menu items available", Toast.LENGTH_SHORT).show()
            return
        }

        val menuItemsArray = extraMenuList.toTypedArray()
        val selectedItems = BooleanArray(menuItemsArray.size) { selectedExtraMenus.contains(menuItemsArray[it]) }
        val tempSelectedItems = selectedExtraMenus.toMutableList()

        isDialogShowing = true

        val dialog = AlertDialog.Builder(this)
            .setTitle("Select Extra Menu Items")
            .setMultiChoiceItems(menuItemsArray, selectedItems) { _, index, isChecked ->
                if (isChecked) {
                    tempSelectedItems.add(menuItemsArray[index])
                } else {
                    tempSelectedItems.remove(menuItemsArray[index])
                }
            }
            .setPositiveButton("OK") { _, _ ->
                selectedExtraMenus.clear()
                selectedExtraMenus.addAll(tempSelectedItems)
                updateSelectedMenuText()
                isDialogShowing = false
            }
            .setNegativeButton("Cancel") { _, _ ->
                isDialogShowing = false
            }

            .create()

        dialog.show()
    }

    private fun updateSelectedMenuText() {
        val displayText = if (selectedExtraMenus.isNotEmpty()) {
            selectedExtraMenus.joinToString(", ")
        } else {
            "Select Extra Menu"
        }

        (edtMenu.selectedView as? TextView)?.text = displayText
    }


    private fun updateMenuSpinner() {
        val displayText = if (selectedExtraMenus.isNotEmpty()) {
            selectedExtraMenus.joinToString(", ")
        } else {
            "Select Extra Menu"
        }

        val menuAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf(displayText))
        menuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edtMenu.adapter = menuAdapter
    }


    private val extraMenuPriceMap = mutableMapOf<String, Int>()

    private fun fetchExtraMenu(catererId: String) {
        Retro.instance.getAllMenu(catererId).enqueue(object : Callback<List<MenuItem>> {
            override fun onResponse(call: Call<List<MenuItem>>, response: Response<List<MenuItem>>) {
                if (response.isSuccessful) {
                    extraMenuList.clear()
                    extraMenuPriceMap.clear()

                    response.body()?.forEach {
                        extraMenuList.add(it.item_name)
                        extraMenuPriceMap[it.item_name] = it.price.toIntOrNull() ?: 0
                    }
                    updateMenuSpinner()
                } else {
                    Toast.makeText(this@SelectDate, "No extra menu available", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MenuItem>>, t: Throwable) {
                Toast.makeText(this@SelectDate, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private val packagePriceMap = mutableMapOf<String, Int>()
    private var selectedPackagePrice = 0

    private fun fetchPackage(catererId: String) {
        Retro.instance.getAllPackages(catererId).enqueue(object : Callback<List<MenuPackage>> {
            override fun onResponse(call: Call<List<MenuPackage>>, response: Response<List<MenuPackage>>) {
                if (response.isSuccessful) {
                    val packages = response.body() ?: emptyList()

                    if (packages.isEmpty()) {
                        Toast.makeText(this@SelectDate, "No menu package available", Toast.LENGTH_SHORT).show()
                    } else {
                        val packageNames = mutableListOf("Select Package")
                        val packageIdMap = mutableMapOf<String, String>()

                        packages.forEach {
                            packageNames.add(it.package_name)
                            packageIdMap[it.package_name] = it._id
                            packagePriceMap[it.package_name] = it.price.toIntOrNull() ?: 0
                            // Store package price
                        }

                        val packageAdapter = object : ArrayAdapter<String>(
                            this@SelectDate, android.R.layout.simple_spinner_dropdown_item, packageNames
                        ) {
                            override fun isEnabled(position: Int) = position != 0

                            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                                val view = super.getDropDownView(position, convertView, parent)
                                (view as TextView).setTextColor(if (position == 0) android.graphics.Color.parseColor("#2A6B93") else android.graphics.Color.BLACK)
                                return view
                            }
                        }

                        edtPack.adapter = packageAdapter
                        edtPack.setSelection(0)

                        edtPack.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                if (position == 0) {
                                    selectedPackagePrice = 0
                                } else {
                                    val selectedPackage = packageNames[position]
                                    selectedPackagePrice = packagePriceMap[selectedPackage] ?: 0
                                    val selectedPackageId = packageIdMap[selectedPackage] ?: ""


                                    val intent = Intent(this@SelectDate, MenusCoveredPackage::class.java)
                                    intent.putExtra("PACKAGE_ID", selectedPackageId)
                                    startActivity(intent)

                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                    }
                } else {
                    Toast.makeText(this@SelectDate, "No menu package available", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MenuPackage>>, t: Throwable) {
                Toast.makeText(this@SelectDate, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showCateringDetailsDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.book, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        noHeads = dialogView.findViewById(R.id.noOfHeads)
        price = dialogView.findViewById(R.id.Price)
        pricePerHeadText = dialogView.findViewById(R.id.PackagePrice)
        totalPriceText = dialogView.findViewById(R.id.totalPrice)
        val bookButton: Button = dialogView.findViewById(R.id.bookButton)
        val txtCaterer = dialogView.findViewById<TextView>(R.id.cateringName)  // Assuming you added this TextView in your layout

        // Fetch values
        val noOfHeads = edtHead.text.toString().toIntOrNull() ?: 0
        val extraMenuTotal = selectedExtraMenus.sumOf { extraMenuPriceMap[it] ?: 0 }
        val finalTotal = (selectedPackagePrice + extraMenuTotal) * noOfHeads

        // Set values
        txtCaterer.text = catererName
        noHeads.text = "No. of Heads: $noOfHeads"
        price.text="Package price: ₹${selectedPackagePrice} /head"
        pricePerHeadText.text = "Price including extra menu: ₹${selectedPackagePrice + extraMenuTotal} /head"
        totalPriceText.text = "Total Price: ₹$finalTotal"
        total = finalTotal.toString()

        dialog.show()

        bookButton.setOnClickListener {
            bookOrder(dialog)
        }
    }


    private fun bookOrder(dialog: AlertDialog) {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User ID is missing. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedDate = edtDate.text.toString()
        val selectedTimeSlot = edtTime.selectedItem.toString().lowercase()
        val numberOfHeads = edtHead.text.toString()
        val contact = edtPhone.text.toString()
        val eventLocation = edtLocation.text.toString()
        val selectedEventType = eventType.selectedItem.toString().lowercase()
        val selectedPackage = edtPack.selectedItem.toString()
        val extraMenu = edtMenu.selectedItem.toString()

        val catererName = txtCatererName.text.toString()

        Log.i("OrderRequest", "Sending Order -> Date: $selectedDate, TimeSlot: $selectedTimeSlot, Heads: $numberOfHeads, Contact: $contact, EventLocation: $eventLocation, EventType: $selectedEventType, Package: $selectedPackage, ExtraMenu: $extraMenu, CatererName: $catererName, CatererId: $cID, UserId: $userId")

        if (selectedDate.isEmpty() || selectedTimeSlot.isEmpty() || numberOfHeads.isEmpty() ||
            contact.isEmpty() || eventLocation.isEmpty() || selectedEventType.isEmpty() ||
            selectedPackage.isEmpty() || extraMenu.isEmpty() || catererName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val orderRequest = OrderRequest( catererName,
            edtDate.text.toString(),
            edtTime.selectedItem.toString(),

            edtHead.text.toString(),
            edtPhone.text.toString(),
            edtLocation.text.toString(),
            eventType.selectedItem.toString(),
            edtPack.selectedItem.toString(),
            selectedExtraMenus.joinToString(", "),
            total
        )

        val call = Retro.instance.createOrder(cID, userId, orderRequest)

        call.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    val orderResponse = response.body()
                    Log.i("OrderSuccess", "Order placed: ${orderResponse?.toString()}")
                    Toast.makeText(this@SelectDate, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    response.errorBody()?.let {
                        val errorMsg = it.string()
                        Log.e("OrderError", "Server Error: ${response.code()} - $errorMsg")
                    } ?: Log.e("OrderError", "Unknown Server Error: ${response.code()} - ${response.message()}")

                    Toast.makeText(this@SelectDate, "Failed to place order. Try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                Log.e("OrderError", "Network error: ${t.message}", t)
                Toast.makeText(this@SelectDate, "Network error. Check connection.", Toast.LENGTH_SHORT).show()
            }
        })
    }


}

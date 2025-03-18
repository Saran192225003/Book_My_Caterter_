package com.example.bookmycaterer.caterer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.CatererProfileResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CatererProfile : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var cateringNameTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var businessSizeInput: EditText
    private lateinit var foodTypeInput: EditText
    private lateinit var cityInput: TextView
    private lateinit var cityTextView: TextView
    private lateinit var profileImageView: ImageView
    private lateinit var updateButton: Button
    private lateinit var editProfilePicButton: Button
    private var profileImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var catererId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caterer_profile)

        // Initialize Views
        nameTextView = findViewById(R.id.catererNameValue)
        cateringNameTextView = findViewById(R.id.serviceNameValue)
        phoneTextView = findViewById(R.id.contactValue)
        businessSizeInput = findViewById(R.id.businessSizeValue)
        foodTypeInput = findViewById(R.id.FoodTypeValue)
        cityTextView = findViewById(R.id.cityLabel)
        cityInput = findViewById(R.id.cityValue)
        profileImageView = findViewById(R.id.profilePicture)
        updateButton = findViewById(R.id.update)
        editProfilePicButton = findViewById(R.id.editProfilePicButton)

        val sharedPreferences = getSharedPreferences("CatererPrefs", MODE_PRIVATE)
        catererId = sharedPreferences.getString("caterer_id", null) ?: ""

        // Fetch existing profile details
        fetchCatererProfile(catererId)

        // Image Picker
        editProfilePicButton.setOnClickListener {
            selectImage()
        }


        // Update Profile Button
        updateButton.setOnClickListener {
            updateCatererProfile()
        }
    }

    private fun fetchCatererProfile(catererId: String) {
        Retro.instance.getCatererProfile(catererId).enqueue(object : Callback<CatererProfileResponse> {
            override fun onResponse(call: Call<CatererProfileResponse>, response: Response<CatererProfileResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val caterer = response.body()!!

                    // Set profile details
                    nameTextView.text = caterer.name
                    cateringNameTextView.text = caterer.Catering_name
                    phoneTextView.text = caterer.phone
                    businessSizeInput.setText(caterer.Business_Size)
                    foodTypeInput.setText(caterer.Food_Type)
                    cityTextView.text = caterer.City

                    // Load Profile Picture using Glide (if API provides a URL)
                    caterer.Profile?.let { imageUrl ->
                        Glide.with(this@CatererProfile)
                            .load(imageUrl)
                            .placeholder(R.drawable.img_2)
                            .into(profileImageView)
                    }
                } else {
                    Toast.makeText(this@CatererProfile, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CatererProfileResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error fetching profile", t)
                Toast.makeText(this@CatererProfile, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            profileImageUri = data.data
            profileImageView.setImageURI(profileImageUri)
        }
    }

    private fun updateCatererProfile() {
        val businessSize = RequestBody.create("text/plain".toMediaTypeOrNull(), businessSizeInput.text.toString())
        val foodType = RequestBody.create("text/plain".toMediaTypeOrNull(), foodTypeInput.text.toString())
        val city = RequestBody.create("text/plain".toMediaTypeOrNull(), cityInput.text.toString())

        var profilePart: MultipartBody.Part? = null
        profileImageUri?.let {
            val file = File(getRealPathFromURI(it))
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            profilePart = MultipartBody.Part.createFormData("Profile", file.name, requestFile)  // ✅ This matches the API
        }

        Retro.instance.updateCatererProfile(catererId, businessSize, foodType, city, profilePart)
            .enqueue(object : Callback<CatererProfileResponse> {
                override fun onResponse(call: Call<CatererProfileResponse>, response: Response<CatererProfileResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CatererProfile, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@CatererProfile, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CatererProfileResponse>, t: Throwable) {
                    Log.e("API_ERROR", "Error updating profile", t)
                    Toast.makeText(this@CatererProfile, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun getRealPathFromURI(uri: Uri): String {
        var filePath = ""
        val cursor = contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = it.getString(columnIndex)
            }
        }
        return filePath
    }

}

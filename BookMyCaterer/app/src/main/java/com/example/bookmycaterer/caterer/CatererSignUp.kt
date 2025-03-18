package com.example.bookmycaterer.caterer

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.bookmycaterer.R
import com.example.bookmycaterer.api.Retro
import com.example.bookmycaterer.user.CatererSignUpResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CatererSignUp : AppCompatActivity() {

    private var edtName: EditText? = null
    private var edtCateringName: EditText? = null
    private var edtPhone: EditText? = null
    private var edtBusinessSize: EditText? = null
    private var edtCity: EditText? = null
    private var buttonLicense: ImageView? = null

    private var txtLogin: TextView? = null
    private var submit: Button? = null
    private var upload: Button? = null
    private var selectedLicenseUri: Uri? = null

    private var FILE_REQUEST_CODE = 1001
    private var PERMISSION_REQUEST_CODE = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caterer_sign_up)

        edtName = findViewById(R.id.editTextName)
        edtCateringName = findViewById(R.id.editTextCateringName)
        edtPhone = findViewById(R.id.editTextContact)
        edtCity = findViewById(R.id.editTextCity)
        edtBusinessSize = findViewById(R.id.editTextBusinessSize)
        buttonLicense = findViewById(R.id.imageViewLicense)
        txtLogin = findViewById(R.id.textViewLogin)
        upload = findViewById(R.id.buttonUploadLicense)
        submit = findViewById(R.id.buttonSubmitVerification)

        txtLogin?.setOnClickListener {
            val intent = Intent(this@CatererSignUp, CatererLoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        upload?.setOnClickListener {
            requestGalleryPermission()
        }

        submit?.setOnClickListener {
            signUp()
        }
    }

    private fun requestGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                openGallery()
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, FILE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(this, "Permission denied to access gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val fileUri: Uri? = data?.data
            if (fileUri != null) {
                selectedLicenseUri = fileUri
                buttonLicense?.setImageURI(fileUri)
                Log.d("FileSelected", "Selected Image URI: $fileUri")
            } else {
                Log.d("FileSelected", "No image selected")
            }
        }
    }

    private fun signUp() {
        val name = edtName?.text.toString()
        val cateringName = edtCateringName?.text.toString()
        val phone = edtPhone?.text.toString()
        val businessSize = edtBusinessSize?.text.toString()
        val city = edtCity?.text.toString()

        if ((edtPhone?.text?.length ?: 0) < 10) {
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
        }
        if (name.isBlank() || cateringName.isBlank() || phone.isBlank() || businessSize.isBlank() || city.isBlank() || selectedLicenseUri == null) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        val namePart = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
        val cateringNamePart = RequestBody.create("text/plain".toMediaTypeOrNull(), cateringName)
        val phonePart = RequestBody.create("text/plain".toMediaTypeOrNull(), phone)
        val businessSizePart = RequestBody.create("text/plain".toMediaTypeOrNull(), businessSize)
        val cityPart = RequestBody.create("text/plain".toMediaTypeOrNull(), city)

        val mimeType = contentResolver.getType(selectedLicenseUri!!)
        if (mimeType == "image/jpeg" || mimeType == "image/png" || mimeType == "application/pdf") {
            val filePath = FileUtils.getPath(this, selectedLicenseUri!!)
            val file = File(filePath)

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val licensePart = MultipartBody.Part.createFormData("license", file.name, requestFile)

            val api = Retro.instance.createCaterer(
                namePart,
                cateringNamePart,
                phonePart,
                businessSizePart,
                cityPart,
                licensePart
            )

            api.enqueue(object : Callback<CatererSignUpResponse> {
                override fun onResponse(
                    call: Call<CatererSignUpResponse>,
                    response: Response<CatererSignUpResponse>
                ) {
                    if (response.isSuccessful) {
                        val catererResponse = response.body()
                        if (catererResponse?.success == true) {
                            Toast.makeText(this@CatererSignUp, "Caterer registered successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@CatererSignUp, Verifying::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@CatererSignUp, "Failed to register caterer", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@CatererSignUp, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CatererSignUpResponse>, t: Throwable) {
                    Toast.makeText(this@CatererSignUp, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Only images or PDF files are allowed", Toast.LENGTH_SHORT).show()
        }
    }

    object FileUtils {
        fun getPath(context: Context, uri: Uri): String? {
            var path: String? = null
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    path = it.getString(columnIndex)
                }
            }
            return path
        }
    }
}

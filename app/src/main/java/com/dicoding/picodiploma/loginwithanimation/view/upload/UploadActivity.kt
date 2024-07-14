package com.dicoding.picodiploma.loginwithanimation.view.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityUploadBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.getImageUri
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.reduceFileImage
import com.dicoding.picodiploma.loginwithanimation.view.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private var currentImageUri: Uri? = null
    private var lat: Float? = null
    private var lon: Float? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val toolbar: androidx.appcompat.widget.Toolbar = binding.myToolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.scLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getLocation()
            } else {
                Toast.makeText(this, "Lokasi tidak aktif", Toast.LENGTH_SHORT).show()
            }
        }

        binding.bGalery.setOnClickListener {
            startGallery()
        }

        binding.bCamera.setOnClickListener {
            startCamera()
        }

        binding.btnUpload.setOnClickListener {
            uploadImage()
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.uploadState.observe(this) {
            when (it.error) {
                false -> {
                    alertDialog("Success!", "Story terupload!")
                }

                true -> {
                    alertDialog("Gagal!", it.message)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.finishActivityEvent.collect { finish() }
            }
        }
    }

    private fun getLocation() {
        val priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val cancellationTokenSource = CancellationTokenSource()

        if (ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        }

        try {
            fusedLocationProviderClient.getCurrentLocation(
                priority,
                cancellationTokenSource.token
            )
                .addOnSuccessListener { location ->
                    lon = location.longitude.toFloat()
                    lat = location.latitude.toFloat()
                }
                .addOnFailureListener { exception ->
                    Log.d("Location", "Oops location failed with exception: $exception")
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.edAddDescription.text.toString()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            Log.d("Upload", "Upload withe image: $lat $lon")
            viewModel.uploadStory(multipartBody, requestBody, lat, lon)
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPreview.setImageURI(it)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btnUpload.isEnabled = !isLoading
        binding.bCamera.isEnabled = !isLoading
        binding.bGalery.isEnabled = !isLoading
        binding.tfDescription.isEnabled = !isLoading
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun alertDialog(title: String, description: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(description)
            setCancelable(false)
            when (title) {
                "Success!" -> {
                    setPositiveButton(R.string.continue_button) { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }

                else -> {
                    setNegativeButton(R.string.retry_button) { _, _ -> }
                }
            }
            create()
            show()
        }
    }
}
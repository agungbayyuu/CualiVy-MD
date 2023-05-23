package app.project.cualivy_capstone


import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import app.project.cualivy_capstone.databinding.ActivityMainBinding
import app.project.cualivy_capstone.login.LoginActivity
import app.project.cualivy_capstone.preview.CameraPreviewActivity
import app.project.cualivy_capstone.preview.GalleryPreviewActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnGallery.setOnClickListener {
            openGallery()
        }

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        setupView()
    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


    @Suppress("DEPRECATION")
    private fun startCamera() {
        // Open camera to take picture
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show()
        }

    }

    @Suppress("DEPRECATION")
    private fun openGallery() {
        // Open gallery to choose image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            val intent = Intent(this, CameraPreviewActivity::class.java)
            intent.putExtra("imageBitmap", imageBitmap)
            startActivity(intent)

            //binding.ivUpload.setImageBitmap(imageBitmap)
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageUri = data?.data

            val intent = Intent(this, GalleryPreviewActivity::class.java)
            intent.putExtra("imageUri", imageUri)
            startActivity(intent)
        }

    }

    companion object {
        const val GALLERY_REQUEST_CODE = 100
        const val CAMERA_REQUEST_CODE = 200
    }


}
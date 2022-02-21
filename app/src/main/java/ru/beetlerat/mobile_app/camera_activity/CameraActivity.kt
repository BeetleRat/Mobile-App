package ru.beetlerat.mobile_app.camera_activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ru.beetlerat.mobile_app.PermissionsCode
import ru.beetlerat.mobile_app.R

class CameraActivity : AppCompatActivity() {
    // Объявление объектов разметки
    private lateinit var avatar: ImageButton
    private lateinit var backButton: Button

    // Объявление лаунчеров activity
    private lateinit var avatarLauncher: ActivityResultLauncher<Intent>

    private fun initElements() {
        avatar = findViewById(R.id.avatarButton)
        avatar.setImageResource(R.drawable.photo)
        backButton = findViewById(R.id.backButton)
    }

    private fun addListenersToElements() {
        backButton.setOnClickListener {
            finish()
        }

        avatar.setOnClickListener {
            val activityContext = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Если есть разрешение на использование камеры
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                try {
                    avatarLauncher.launch(activityContext)
                } catch (e: ActivityNotFoundException) {
                    Log.e("camera","Не получилось создать activity камеры")
                }
            } else {
                // Если нет разрешения на использование камеры, тогда запрасить разрешения: использование камеры
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                    PermissionsCode.AVATAR_CAMERA_PERMISSION
                )
            }
        }
    }

    private fun initActivityLaunchers() {
        avatarLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    result: ActivityResult ->
                // Если дочерняя activity завершилась со статусом RESULT_OK
                if (result.resultCode == RESULT_OK) {
                    val imageBitmap = result.data?.extras?.get("data") as Bitmap

                    if (imageBitmap == null) {
                        Toast.makeText(this, R.string.image_does_not_exists, Toast.LENGTH_LONG).show()
                    } else {
                        avatar.setImageBitmap(imageBitmap)
                    }
                }
            }
    }

    // Функция выполняемая после ответа пользователя на запрос разрешений для приложения
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PermissionsCode.AVATAR_CAMERA_PERMISSION -> {
                // Запрашивали 1 разрешение - оно храниться в 0 индексе grantResults
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    avatar.callOnClick()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        initElements()

        addListenersToElements()

        initActivityLaunchers()
    }
}
package ru.beetlerat.mobile_app.geo_activity

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ru.beetlerat.mobile_app.PermissionsCode
import ru.beetlerat.mobile_app.R


class GeoActivity : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var currentCoord: TextView
    private lateinit var locationManager: LocationManager
    private lateinit var globePicture:ImageView
    private lateinit var waitingBar:ProgressBar

    // Минимальное время в милисекундах между получением данных
    private val minTimeBetweenGettingData: Long = 1000 * 1 // 1 секунда
    // Минимальное расстояние в метрах, между получением данных
    private val minDistanceBetweenGettingData: Float = 10F // 10 метров

    private fun initElements() {
        backButton = findViewById(R.id.globeBackButton)
        currentCoord = findViewById(R.id.currentCoord)
        currentCoord.setText(R.string.coord_waiting)
        globePicture=findViewById(R.id.globeImage)
        globePicture.visibility= View.INVISIBLE
        waitingBar=findViewById(R.id.waitingCoord)
        waitingBar.visibility=View.VISIBLE

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private fun addListenersToElements() {
        backButton.setOnClickListener {
            finish()
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
            PermissionsCode.GEO_PERMISSIONS -> {
                // Если получены разрешения на получение данных о местоположении
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    updateLocationData()
                } else {
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geo)

        initElements()

        addListenersToElements()

        updateLocationData()
    }

    override fun onPause() {
        super.onPause()
        // Отключить слушателя locationManager
        locationManager.removeUpdates(locationListener)

        currentCoord.setText(R.string.coord_waiting)
        globePicture.visibility= View.INVISIBLE
        waitingBar.visibility=View.VISIBLE
    }

    override fun onResume() {
        super.onResume()

        updateLocationData()
    }

    private fun updateLocationData() {
        // Если нет разрешения на доступ к геолокации
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Запросить доступ к геолокации
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PermissionsCode.GEO_PERMISSIONS
            )
        } else {
            // Запросить новые данные через GPS_PROVIDER
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTimeBetweenGettingData,
                minDistanceBetweenGettingData,
                locationListener
            )
        }
    }

    private fun setCoordinates(location: Location) {
        val locationString: String =
            "${getString(R.string.coord_latitude)} ${location.latitude};\n" +
            "${getString(R.string.coord_longitude)} ${location.longitude};\n" +
            "${getString(R.string.coord_altitude)} ${location.altitude};\n" +
            "${getString(R.string.coord_accuracy)} ${location.accuracy};\n" +
            "${getString(R.string.coord_bearing)} ${location.bearing}"

        currentCoord.setText(locationString)

        globePicture.visibility= View.VISIBLE
        waitingBar.visibility=View.INVISIBLE
    }

    val locationListener = object : LocationListener {
        override fun onLocationChanged(newLocation: Location) {
            setCoordinates(newLocation)
        }
    }
}
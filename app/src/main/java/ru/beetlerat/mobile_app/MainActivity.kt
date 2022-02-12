package ru.beetlerat.mobile_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    // Объявление объектов разметки
    private lateinit var calculatorActivityButton:Button
    private lateinit var cameraActivityButton:Button
    private lateinit var locationActivityButton:Button
    private lateinit var databaseActivityButton:Button
    private lateinit var restActivityButton:Button

    private fun initElements(){
        calculatorActivityButton=findViewById(R.id.calculatorActivityButton)
        calculatorActivityButton.setText(R.string.calculator_activity_button)

        cameraActivityButton=findViewById(R.id.cameraActivityButton)
        cameraActivityButton.setText(R.string.camera_activity_button)

        locationActivityButton=findViewById(R.id.locationActivityButton)
        locationActivityButton.setText(R.string.location_activity_button)

        databaseActivityButton=findViewById(R.id.databaseActivityButton)
        databaseActivityButton.setText(R.string.database_activity_button)

        restActivityButton=findViewById(R.id.restActivityButton)
        restActivityButton.setText(R.string.rest_activity_button)
    }

    private fun addListenersToElements(){
        calculatorActivityButton.setOnClickListener {
            // Вывести заглушку на экран
            Toast.makeText(this,R.string.calculator_activity_button_pressed,Toast.LENGTH_LONG).show()
        }

        cameraActivityButton.setOnClickListener {
            // Вывести заглушку на экран
            Toast.makeText(this,R.string.camera_activity_button_pressed,Toast.LENGTH_LONG).show()
        }

        locationActivityButton.setOnClickListener {
            // Вывести заглушку на экран
            Toast.makeText(this,R.string.location_activity_button_pressed,Toast.LENGTH_LONG).show()
        }

        databaseActivityButton.setOnClickListener {
            // Вывести заглушку на экран
            Toast.makeText(this,R.string.database_activity_button_pressed,Toast.LENGTH_LONG).show()
        }

        restActivityButton.setOnClickListener {
            // Вывести заглушку на экран
            Toast.makeText(this,R.string.rest_activity_button_pressed,Toast.LENGTH_LONG).show()
        }
    }

    // Метод запускаемый при создании activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Установка разметки которую отображает данная activity
        setContentView(R.layout.activity_main)

        initElements()

        addListenersToElements()
    }
}
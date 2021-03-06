package ru.beetlerat.mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ru.beetlerat.mobile_app.calculator_activity.CalculatorActivity
import ru.beetlerat.mobile_app.camera_activity.CameraActivity
import ru.beetlerat.mobile_app.database_activity.EnglishVerbsSelectActivity
import ru.beetlerat.mobile_app.geo_activity.GeoActivity
import ru.beetlerat.mobile_app.rest_activity.RestActivity

class MainActivity : AppCompatActivity() {

    // Объявление объектов разметки
    private lateinit var calculatorActivityButton:Button
    private lateinit var cameraActivityButton:Button
    private lateinit var locationActivityButton:Button
    private lateinit var databaseActivityButton:Button
    private lateinit var restActivityButton:Button

    // Объявление лаунчеров activity
    private lateinit var mainActivityLauncher: ActivityResultLauncher<Intent>

    private fun initElements(){
        calculatorActivityButton=findViewById(R.id.calculatorActivityButton)
        calculatorActivityButton.setText(R.string.calculator_title)

        cameraActivityButton=findViewById(R.id.cameraActivityButton)
        cameraActivityButton.setText(R.string.camera_title)

        locationActivityButton=findViewById(R.id.locationActivityButton)
        locationActivityButton.setText(R.string.location_title)

        databaseActivityButton=findViewById(R.id.databaseActivityButton)
        databaseActivityButton.setText(R.string.database_title)

        restActivityButton=findViewById(R.id.restActivityButton)
        restActivityButton.setText(R.string.rest_title)
    }

    private fun initActivityLaunchers(){
        mainActivityLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
            // Если дочерняя activity завершилась со статусом RESULT_OK
            if(result.resultCode==RESULT_OK){
                // обрабатываем результаты, которые вернула дочерняя activity
            }
        }
    }

    private fun addListenersToElements(){
        calculatorActivityButton.setOnClickListener {
            /* Создать переменную объекта Intent
            данный объект служит для создания инструкции запуска новой activity
            в данном случае мы создаем новое activity от this класса
            (когда новое activity закроется, откроется this класс)
            по данной инструкции создается activity из класса CalculatorActivity
             */
            val activityContext = Intent(this, CalculatorActivity::class.java)
            /* Запускаем новое activity согласно activityContext
            через обработчик результата activity(launcher) - mainActivityLauncher
             */
            mainActivityLauncher.launch(activityContext)
        }

        cameraActivityButton.setOnClickListener {
            val activityContext = Intent(this, CameraActivity::class.java)
            mainActivityLauncher.launch(activityContext)
        }

        locationActivityButton.setOnClickListener {
            val activityContext = Intent(this, GeoActivity::class.java)
            mainActivityLauncher.launch(activityContext)
        }

        databaseActivityButton.setOnClickListener {
            val activityContext = Intent(this, EnglishVerbsSelectActivity::class.java)
            mainActivityLauncher.launch(activityContext)
        }

        restActivityButton.setOnClickListener {
            val activityContext = Intent(this, RestActivity::class.java)
            mainActivityLauncher.launch(activityContext)
        }
    }

    // Метод запускаемый при создании activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Установка разметки которую отображает данная activity
        setContentView(R.layout.activity_main)

        initElements()

        initActivityLaunchers()

        addListenersToElements()
    }
}
package ru.beetlerat.mobile_app.database_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.beetlerat.mobile_app.R
import ru.beetlerat.mobile_app.database_activity.database.DatabaseService
import ru.beetlerat.mobile_app.databinding.ActivityEnglishVerbsInsertBinding

class EnglishVerbsInsertActivity : AppCompatActivity() {
    // Переменная содержащая все объекты Activity English Verbs Insert layout
    private lateinit var elements: ActivityEnglishVerbsInsertBinding

    // Объявление переменных
    private lateinit var database: DatabaseService

    private fun initElements(){
        database.openDatabase()
    }

    private fun addListenersToElements(){
        elements.backToMainDB.setOnClickListener {
            finish()
        }

        elements.addVerbToDBButton.setOnClickListener {
            // Если все editText заполнены
            if(elements.russianText.text.isNotEmpty()&&elements.firstFormAddText.text.isNotEmpty()&&elements.secondFormAddText.text.isNotEmpty()&&elements.thirdFormAddText.text.isNotEmpty()){
                // Добавляем их значения в БД
                database.insertVerb(elements.russianText.text.toString(),elements.firstFormAddText.text.toString(),elements.secondFormAddText.text.toString(),elements.thirdFormAddText.text.toString())
                // Очищаем
                elements.russianText.text.clear()
                elements.russianText.setBackgroundColor(resources.getColor(R.color.white))
                elements.firstFormAddText.text.clear()
                elements.firstFormAddText.setBackgroundColor(resources.getColor(R.color.white))
                elements.secondFormAddText.text.clear()
                elements.secondFormAddText.setBackgroundColor(resources.getColor(R.color.white))
                elements.thirdFormAddText.text.clear()
                elements.thirdFormAddText.setBackgroundColor(resources.getColor(R.color.white))
            }
            else{
                // Иначе выделяем красным не заполненный editText
                if(elements.russianText.text.isEmpty()){
                    elements.russianText.setBackgroundColor(resources.getColor(R.color.red))
                }
                else{
                    elements.russianText.setBackgroundColor(resources.getColor(R.color.white))
                }
                if(elements.firstFormAddText.text.isEmpty()){
                    elements.firstFormAddText.setBackgroundColor(resources.getColor(R.color.red))
                }
                else{
                    elements.firstFormAddText.setBackgroundColor(resources.getColor(R.color.white))
                }
                if(elements.secondFormAddText.text.isEmpty()){
                    elements.secondFormAddText.setBackgroundColor(resources.getColor(R.color.red))
                }
                else{
                    elements.secondFormAddText.setBackgroundColor(resources.getColor(R.color.white))
                }
                if(elements.thirdFormAddText.text.isEmpty()){
                    elements.thirdFormAddText.setBackgroundColor(resources.getColor(R.color.red))
                }
                else{
                    elements.thirdFormAddText.setBackgroundColor(resources.getColor(R.color.white))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Задание значения переменной содержащей все объекты layout
        elements= ActivityEnglishVerbsInsertBinding.inflate(layoutInflater)
        // Отображаем форму из переменной elements как контент в данном activity
        setContentView(elements.root)
        // Создаем объект взаимодействия с БД
        database=DatabaseService(this)

        addListenersToElements()
    }

    override fun onResume() {
        super.onResume()
        initElements()
    }
    override fun onPause() {
        super.onPause()
        database.closeDatabase()
    }
}
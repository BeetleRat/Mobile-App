package ru.beetlerat.mobile_app.database_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ru.beetlerat.mobile_app.R
import ru.beetlerat.mobile_app.database_activity.database.DatabaseService
import ru.beetlerat.mobile_app.database_activity.database.VerbModel
import ru.beetlerat.mobile_app.database_activity.database.VerbsIDBuffer
import ru.beetlerat.mobile_app.databinding.ActivityEnglishVerbsSelectBinding
import java.util.*

class EnglishVerbsSelectActivity : AppCompatActivity() {
    // Переменная содержащая все объекты Activity English Verbs Select layout
    private lateinit var elements: ActivityEnglishVerbsSelectBinding

    // Объявление лаунчеров activity
    private lateinit var activityLauncher: ActivityResultLauncher<Intent>

    // Объявление переменных
    private lateinit var verbsIDBuffer:VerbsIDBuffer
    private lateinit var database:DatabaseService
    private var currentVerb:VerbModel?=null // Глагол отображаемый в текущий момент

    private fun initElements(){
        database.openDatabase()
        verbsIDBuffer=VerbsIDBuffer(database.selectVerbsIDs())
        if(verbsIDBuffer.getSize()==0){
            elements.russianTextView.setText(R.string.empty_database)
            elements.firstFormText.setText(R.string.empty_database)
            elements.firstFormText.isEnabled=false
            elements.secondFormText.setText(R.string.empty_database)
            elements.secondFormText.isEnabled=false
            elements.thirdFormText.setText(R.string.empty_database)
            elements.thirdFormText.isEnabled=false
        }else{
            setNewVerb(verbsIDBuffer.getRandomID())
        }
    }

    private fun addListenersToElements(){
        elements.backToMainButton.setOnClickListener {
            finish()
        }

        elements.addVerbButton.setOnClickListener {
            val activityContext = Intent(this, EnglishVerbsInsertActivity::class.java)
            activityLauncher.launch(activityContext)
        }

        elements.editVerbButton.setOnClickListener {
            val activityContext = Intent(this, EnglishVerbsUpdateActivity::class.java)
            activityLauncher.launch(activityContext)
        }

        // Слушатели изменения текста в editText
        elements.firstFormText.addTextChangedListener (object : TextWatcher {
            // Перегрузка функции действий после изменения текста
            override fun afterTextChanged(newText: Editable) {
                // если введенный пользователем текст совпал с currentVerb
                if(newText.toString().uppercase(Locale.getDefault()) ==currentVerb?.getFirstFormVerb()?.uppercase(Locale.getDefault())){
                    elements.firstFormText.isEnabled=false
                    elements.firstFormText.setBackgroundColor(resources.getColor(R.color.correct_edit_text))
                    isAllVerbsRight() // Проверить, написал ли пользователь остальные формы глагола
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        elements.secondFormText.addTextChangedListener (object : TextWatcher {
            // Перегрузка функции действий после изменения текста
            override fun afterTextChanged(newText: Editable) {
                // если введенный пользователем текст совпал с currentVerb
                if(newText.toString().uppercase(Locale.getDefault())==currentVerb?.getSecondFormVerb()?.uppercase(Locale.getDefault())){
                    elements.secondFormText.isEnabled=false
                    elements.secondFormText.setBackgroundColor(resources.getColor(R.color.correct_edit_text))
                    isAllVerbsRight() // Проверить, написал ли пользователь остальные формы глагола
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        elements.thirdFormText.addTextChangedListener (object : TextWatcher {
            // Перегрузка функции действий после изменения текста
            override fun afterTextChanged(newText: Editable) {
                // если введенный пользователем текст совпал с currentVerb
                if(newText.toString().uppercase(Locale.getDefault())==currentVerb?.getThirdFormVerb()?.uppercase(Locale.getDefault())){
                    elements.thirdFormText.isEnabled=false
                    elements.thirdFormText.setBackgroundColor(resources.getColor(R.color.correct_edit_text))
                    isAllVerbsRight() // Проверить, написал ли пользователь остальные формы глагола
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun initActivityLaunchers(){
        activityLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result: ActivityResult ->
            // Если дочерняя activity завершилась со статусом RESULT_OK
            if(result.resultCode==RESULT_OK){
                // обрабатываем результаты, которые вернула дочерняя activity

            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Задание значения переменной содержащей все объекты layout
        elements= ActivityEnglishVerbsSelectBinding.inflate(layoutInflater)
        // Отображаем форму из переменной elements как контент в данном activity
        setContentView(elements.root)
        // Создаем объект взаимодействия с БД
        database=DatabaseService(this)

        addListenersToElements()

        initActivityLaunchers()
    }

    override fun onResume() {
        super.onResume()
        initElements()
    }

    override fun onPause() {
        super.onPause()
        database.closeDatabase()
    }

    private fun setNewVerb(id:Int){

        currentVerb=database.selectVerbByID(id)

        if(currentVerb!=null){
            elements.firstFormText.isEnabled=true
            elements.firstFormText.text.clear()
            elements.firstFormText.setBackgroundColor(resources.getColor(R.color.white))
            elements.secondFormText.isEnabled=true
            elements.secondFormText.setBackgroundColor(resources.getColor(R.color.white))
            elements.secondFormText.text.clear()
            elements.thirdFormText.isEnabled=true
            elements.thirdFormText.setBackgroundColor(resources.getColor(R.color.white))
            elements.thirdFormText.text.clear()
            elements.russianTextView.setText(currentVerb?.getRussianVerb())
        }
        else{
            Log.e("database","Не удалось получить данные. ID:${id}")
        }
    }

    private fun isAllVerbsRight(){
        // Если все editText заблокированы(значит пользователь ввел все слова)
        if(!elements.firstFormText.isEnabled&&!elements.secondFormText.isEnabled&&!elements.thirdFormText.isEnabled){
            setNewVerb(verbsIDBuffer.getRandomID())
        }
    }
}
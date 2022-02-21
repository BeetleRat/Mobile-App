package ru.beetlerat.mobile_app.database_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import ru.beetlerat.mobile_app.R
import ru.beetlerat.mobile_app.database_activity.database.DatabaseService
import ru.beetlerat.mobile_app.database_activity.database.VerbModel
import ru.beetlerat.mobile_app.database_activity.database.VerbsIDBuffer
import ru.beetlerat.mobile_app.databinding.ActivityEnglishVerbsUpdateBinding

class EnglishVerbsUpdateActivity : AppCompatActivity() {
    // Константы
    private object DataBaseElement{
        const val NEXT_ELEMENT=true
        const val PREVIOUS_ELEMENT=false
    }

    // Переменная содержащая все объекты Activity English Verbs Insert layout
    private lateinit var elements: ActivityEnglishVerbsUpdateBinding

    // Объявление переменных
    private lateinit var verbsIDBuffer: VerbsIDBuffer
    private lateinit var database: DatabaseService

    private fun initElements() {
        database.openDatabase()
        verbsIDBuffer=VerbsIDBuffer(database.selectVerbsIDs())
        if(verbsIDBuffer.getSize()==0){
           setEmptyDatabaseStatement()
        }else{
            loadNewElement(DataBaseElement.NEXT_ELEMENT)
        }
    }

    private fun addListenersToElements() {
        elements.backToDBMain.setOnClickListener {
            finish()
        }

        // При изменении текста в editText сразу же изменяем его в БД
        elements.russianEditText.addTextChangedListener (object : TextWatcher {
            // Перегрузка функции действий после изменения текста
            override fun afterTextChanged(newText: Editable) {
                database.updateVerbByID(verbsIDBuffer.getCurrentID(),newText.toString(),null,null,null)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        elements.firstFormEditText.addTextChangedListener (object : TextWatcher {
            // Перегрузка функции действий после изменения текста
            override fun afterTextChanged(newText: Editable) {
                database.updateVerbByID(verbsIDBuffer.getCurrentID(),null,newText.toString(),null,null)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        elements.secondFormEditText.addTextChangedListener (object : TextWatcher {
            // Перегрузка функции действий после изменения текста
            override fun afterTextChanged(newText: Editable) {
                database.updateVerbByID(verbsIDBuffer.getCurrentID(),null,null,newText.toString(),null)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        elements.thirdFormEditText.addTextChangedListener (object : TextWatcher {
            // Перегрузка функции действий после изменения текста
            override fun afterTextChanged(newText: Editable) {
                database.updateVerbByID(verbsIDBuffer.getCurrentID(),null,null,null,newText.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        elements.nextVerbButton.setOnClickListener {
            loadNewElement(DataBaseElement.NEXT_ELEMENT)
        }
        elements.prevVerbButton.setOnClickListener {
            loadNewElement(DataBaseElement.PREVIOUS_ELEMENT)
        }

        elements.deleteVerbButton.setOnClickListener {
            // Удалить элемент из БД
            database.deleteVerb(verbsIDBuffer.getCurrentID())
            // Удалить из буффера ID только что удаленного элемента
            verbsIDBuffer.deleteCurrentID()
            // Если в буфере не осталось ID(а значит и в БД)
            if(!verbsIDBuffer.isExist()){
                setEmptyDatabaseStatement()
            }
            else{
                loadNewElement(DataBaseElement.PREVIOUS_ELEMENT)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Задание значения переменной содержащей все объекты layout
        elements= ActivityEnglishVerbsUpdateBinding.inflate(layoutInflater)
        // Отображаем форму из переменной elements как контент в данном activity
        setContentView(elements.root)
        // Создаем объект взаимодействия с БД
        database= DatabaseService(this)

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

    private fun loadNewElement(isNextElement: Boolean) {

        val verb: VerbModel?

        if (isNextElement) {
            verb = database.selectVerbByID(verbsIDBuffer.next())
        } else {
            verb = database.selectVerbByID(verbsIDBuffer.back())
        }

        elements.russianEditText.isEnabled = true
        elements.firstFormEditText.isEnabled = true
        elements.secondFormEditText.isEnabled = true
        elements.thirdFormEditText.isEnabled = true
        elements.deleteVerbButton.isEnabled = true
        elements.nextVerbButton.isEnabled = true
        elements.prevVerbButton.isEnabled = true

        elements.idLabel.setText("${getString(R.string.id_label)} ${verb?.getID()}")
        elements.russianEditText.setText(verb?.getRussianVerb())
        elements.firstFormEditText.setText(verb?.getFirstFormVerb())
        elements.secondFormEditText.setText(verb?.getSecondFormVerb())
        elements.thirdFormEditText.setText(verb?.getThirdFormVerb())
    }

    private fun setEmptyDatabaseStatement(){
        elements.russianEditText.setText(R.string.empty_database)
        elements.firstFormEditText.setText(R.string.empty_database)
        elements.secondFormEditText.setText(R.string.empty_database)
        elements.thirdFormEditText.setText(R.string.empty_database)

        elements.russianEditText.isEnabled=false
        elements.firstFormEditText.isEnabled=false
        elements.secondFormEditText.isEnabled=false
        elements.thirdFormEditText.isEnabled=false
        elements.deleteVerbButton.isEnabled=false
        elements.nextVerbButton.isEnabled=false
        elements.prevVerbButton.isEnabled=false
    }
}
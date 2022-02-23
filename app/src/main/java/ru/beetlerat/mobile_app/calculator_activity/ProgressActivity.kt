package ru.beetlerat.mobile_app.calculator_activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import ru.beetlerat.mobile_app.ActivityName
import ru.beetlerat.mobile_app.databinding.ActivityProgressBinding


class ProgressActivity : AppCompatActivity() {

    // Переменная содержащая все объекты Activity Calculator layout
    lateinit var elements:ActivityProgressBinding

    private fun initElements(){
        elements.progressBar.setProgress(1)
        // Устанавливаем в numberText значение из родительского activity с именем progressNumber
        // или 100 если такого значения нет в родительском activity
        elements.numberText.setText(intent.getIntExtra("progressNumber",100).toString())
        elements.percentText.setText("1")
        // Выводим результат в resultText
        elements.resultText.setText(calculatePercentage().toString())
    }

    private fun addListenersToElements(){
        // Задаем слушателя изменения значения SeekBar через адаптер SeekBar.OnSeekBarChangeListener
        elements.progressBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            // Перегрузка функции изменения значения в SeekBar
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Устанавливаем новое значение в percentText
                elements.percentText.setText("$i")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            
            }
        )

        // Устанавливаем слушателя изменения значения в percentText через адаптер TextWatcher
        elements.percentText.addTextChangedListener(object : TextWatcher {
            // Перегрузка функции действий после изменения текста
            override fun afterTextChanged(newText: Editable) {
                if(newText.length==0){
                    elements.percentText.setText("0")
                }
                // Устанавливаем progressBar в соответствии с новым значением
                elements.progressBar.setProgress(Integer.parseInt(elements.percentText.text.toString()))
                // Выводим результат в resultText
                elements.resultText.setText(calculatePercentage().toString())
                // Устанавливаем каретку в конец строки
                elements.percentText.setSelection(newText.length)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        // Устанавливаем слушателя изменения значения в numberText через адаптер TextWatcher
        elements.numberText.addTextChangedListener(object : TextWatcher {
            // Перегрузка функции действий после изменения текста
            override fun afterTextChanged(newText: Editable) {
                if(newText.length==0){
                    elements.numberText.setText("0")
                }
                // Выводим результат в resultText
                elements.resultText.setText(calculatePercentage().toString())
                // Устанавливаем каретку в конец строки
                elements.numberText.setSelection(newText.length)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        elements.fromProgressToCalculatorButton.setOnClickListener {
            // Формируем результат работы activity для родительского activity
            intent.putExtra("toActivity", ActivityName.CALCULATOR_ACTIVITY)
            // activity завершен со статусом RESULT_OK
            // результат работы передается в объекте класса Intent
            setResult(RESULT_OK, intent)
            finish()
        }
        elements.fromProgressToCounterButton.setOnClickListener {
            // Формируем результат работы activity для родительского activity
            intent.putExtra("toActivity", ActivityName.COUNTER_ACTIVITY)
            // activity завершен со статусом RESULT_OK
            // результат работы передается в объекте класса Intent
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Задание значения переменной содержащей все объекты layout
        elements= ActivityProgressBinding.inflate(layoutInflater)
        // Отображаем форму из переменной elements как контент в данном activity
        setContentView(elements.root)

        initElements()

        addListenersToElements()
    }

    private fun calculatePercentage():Double{
        return elements.numberText.text.toString().toDouble()/100*elements.percentText.text.toString().toDouble()
    }
}
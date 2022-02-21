package ru.beetlerat.mobile_app.calculator_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import ru.beetlerat.mobile_app.ActivityName
import ru.beetlerat.mobile_app.R

class CounterActivity : AppCompatActivity() {
    // Объявление объектов разметки
    private lateinit var pluseButton: Button
    private lateinit var minusButton: Button
    private lateinit var counter:TextView
    private lateinit var toCalculatorActivityButton: Button
    private lateinit var toProgressActivityButton: Button
    private lateinit var cancelButton: Button

    // Объявление переменных
    private var counterValue:Int=0

    private fun initElements(){
        pluseButton=findViewById(R.id.plusButton)
        minusButton=findViewById(R.id.minusButton)
        toCalculatorActivityButton=findViewById(R.id.fromCounterToCalculatorButton)
        toProgressActivityButton=findViewById(R.id.fromCounterToProgressButton)
        cancelButton=findViewById(R.id.cancelButton)
        counter=findViewById(R.id.counter1Text)
        counter.setText(counterValue.toString())
    }

    private fun addListenersToElements(){
        pluseButton.setOnClickListener {
            counterValue++
            counter.setText(counterValue.toString())
        }

        minusButton.setOnClickListener {
            if(counterValue>0){
                counterValue--
                counter.setText(counterValue.toString())
            }
        }
        cancelButton.setOnClickListener {
            counterValue=0
            counter.setText(counterValue.toString())
        }

        toCalculatorActivityButton.setOnClickListener {
            // Формируем результат работы activity для родительского activity
            intent.putExtra("toActivity", ActivityName.CALCULATOR_ACTIVITY)
            // activity завершен со статусом RESULT_OK
            // результат работы передается в объекте класса Intent
            setResult(RESULT_OK,intent)
            finish()
        }
        toProgressActivityButton.setOnClickListener {
            // Формируем результат работы activity для родительского activity
            intent.putExtra("toActivity", ActivityName.PROGRESS_ACTIVITY)
            intent.putExtra("progressNumber",counterValue)
            // activity завершен со статусом RESULT_OK
            // результат работы передается в объекте класса Intent
            setResult(RESULT_OK,intent)
            finish()
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        initElements()

        addListenersToElements()
    }
}
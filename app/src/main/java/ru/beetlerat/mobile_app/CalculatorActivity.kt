package ru.beetlerat.mobile_app


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import ru.beetlerat.mobile_app.databinding.ActivityCalculatorBinding

class CalculatorActivity : AppCompatActivity() {

    // Переменная содержащая все объекты Activity Calculator layout
    private lateinit var elements:ActivityCalculatorBinding

    private fun initElements(){
        elements.calculatorTitle.setText(R.string.calculator_title)
    }

    private fun addListenersToElements(){
        elements.buttonClear.setOnClickListener {
            elements.textAnswer.setText("")
        }

        elements.buttonEqual.setOnClickListener {
            // Строка из цифр и знаков, которую надо подсчитать
            val equalsString:String=elements.textAnswer.text.toString()
            var answer=0.0
            // Расчет выражения

            // Запись результата в view
            elements.textAnswer.setText(answer.toString())
        }
    }


    fun numberOnClickListener(view: View){
        val button:Button=findViewById(view.id)
        if(button!=null){
            var newAnswer:String =elements.textAnswer.text.toString()
            if(newAnswer.isEmpty()){
                // Проверка, что бы первым символом не шли знаки + * /
                if(button.text !in "/*+"){
                    elements.textAnswer.setText(button.text.toString())
                }
            }
            else {
                // Проверка, что бы не шли два знака подряд
                if (newAnswer.last()  != '+' && newAnswer.last()!= '-' && newAnswer.last() != '*' && newAnswer.last() != '/' || button.text in "0123456789") {
                    newAnswer = newAnswer + button.text.toString()
                    elements.textAnswer.setText(newAnswer)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Задание значения переменной содержащей все объекты layout
        elements= ActivityCalculatorBinding.inflate(layoutInflater)
        // Отображаем форму из переменной elements как контент в данном activity
        setContentView(elements.root)

        initElements()

        addListenersToElements()
    }
}
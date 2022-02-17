package ru.beetlerat.mobile_app


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ru.beetlerat.mobile_app.calculator.StringCalculate

import ru.beetlerat.mobile_app.databinding.ActivityCalculatorBinding

class CalculatorActivity : AppCompatActivity() {

    // Переменная содержащая все объекты Activity Calculator layout
    private lateinit var elements:ActivityCalculatorBinding

    // Объявление лаунчеров activity
    private lateinit var activityLauncher: ActivityResultLauncher<Intent>

    // Объявление переменных
    private var isAnswerInTextAnswer:Boolean=false

    private fun initActivityLaunchers(){
        activityLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result: ActivityResult ->
            // Если дочерняя activity завершилась со статусом RESULT_OK
            if(result.resultCode==RESULT_OK){
                // обрабатываем результаты, которые вернула дочерняя activity
                val nextActivity= result.data?.getStringExtra("toActivity")
                when(nextActivity){
                    ActivityName.COUNTER_ACTIVITY->{
                        /* Создать переменную объекта Intent
                       данный объект служит для создания инструкции запуска новой activity
                       в данном случае мы создаем новое activity от this класса
                       (когда новое activity закроется, откроется this класс)
                       по данной инструкции создается activity из класса CounterActivity
                        */
                        val activityContext = Intent(this,CounterActivity::class.java)
                        /* Запускаем новое activity согласно activityContext
                        через обработчик результата activity(launcher) - activityLauncher
                         */
                        activityLauncher.launch(activityContext)
                    }
                    ActivityName.PROGRESS_ACTIVITY->{
                        /* Создать переменную объекта Intent
                       данный объект служит для создания инструкции запуска новой activity
                       в данном случае мы создаем новое activity от this класса
                       (когда новое activity закроется, откроется this класс)
                       по данной инструкции создается activity из класса ProgressActivity
                        */
                        val activityContext = Intent(this,ProgressActivity::class.java)
                        // Наполняем контекст переменными
                        // Передаем переменную результата работы дочерней activity progressNumber,
                        // в activityContext под именем progressNumber
                        activityContext.putExtra("progressNumber",result.data?.getIntExtra("progressNumber",100))
                        /* Запускаем новое activity согласно activityContext
                        через обработчик результата activity(launcher) - activityLauncher
                         */
                        activityLauncher.launch(activityContext)
                    }
                    ActivityName.CALCULATOR_ACTIVITY->{
                        // Ничего не делаем
                    }
                }
            }
        }
    }

    private fun addListenersToElements(){
        elements.buttonClear.setOnClickListener {
            elements.textAnswer.setText("")
        }

        elements.buttonEqual.setOnClickListener {
            // Строка из цифр и знаков, которую надо подсчитать
            val equalsString:String=elements.textAnswer.text.toString()
            // Расчет выражения
            val answer=StringCalculate.calculate(equalsString)
            // Запись результата в view
            elements.textAnswer.setText(answer.toString())
            isAnswerInTextAnswer=true
        }

        elements.fromCalculatorToCounterButton.setOnClickListener {
            /* Создать переменную объекта Intent
            данный объект служит для создания инструкции запуска новой activity
            в данном случае мы создаем новое activity от this класса
            (когда новое activity закроется, откроется this класс)
            по данной инструкции создается activity из класса CounterActivity
             */
            val activityContext = Intent(this,CounterActivity::class.java)
            /* Запускаем новое activity согласно activityContext
            через обработчик результата activity(launcher) - activityLauncher
             */
            activityLauncher.launch(activityContext)
        }

        elements.fromCalculatorToProgressButton.setOnClickListener {
            /* Создать переменную объекта Intent
            данный объект служит для создания инструкции запуска новой activity
            в данном случае мы создаем новое activity от this класса
            (когда новое activity закроется, откроется this класс)
            по данной инструкции создается activity из класса ProgressActivity
             */
            val activityContext = Intent(this,ProgressActivity::class.java)

            if(elements.textAnswer.text.isNotEmpty()){
                // Наполняем контекст переменными
                activityContext.putExtra("progressNumber",Integer.parseInt(elements.textAnswer.text.toString()))
            }
            /* Запускаем новое activity согласно activityContext
            через обработчик результата activity(launcher) - activityLauncher
             */
            activityLauncher.launch(activityContext)
        }
    }

    fun numberOnClickListener(view: View){
        if(isAnswerInTextAnswer){
            isAnswerInTextAnswer=false
            elements.textAnswer.text.clear()
        }
        val button:Button?=findViewById(view.id)
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

        initActivityLaunchers()

        addListenersToElements()
    }
}
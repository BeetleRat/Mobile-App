package ru.beetlerat.mobile_app.rest_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.beetlerat.mobile_app.R
import ru.beetlerat.mobile_app.databinding.ActivityAddBookBinding
import ru.beetlerat.mobile_app.rest_activity.service.BookModel
import ru.beetlerat.mobile_app.rest_activity.service.ServerAccessObject

class AddBookActivity : AppCompatActivity() {
    // Переменная содержащая все объекты Activity Calculator layout
    private lateinit var elements: ActivityAddBookBinding

    // Объявление переменных
    private lateinit var sao: ServerAccessObject

    private fun initElements(){
        sao= ServerAccessObject(intent.getStringExtra("endpoint").toString())
    }

    private fun addListenersToElements(){
        elements.addBookCancelButton.setOnClickListener {
            finish()
        }

        elements.addNewBookButton.setOnClickListener {
            // Если все поля заполнены
            if (elements.addBookNameText.text.isNotEmpty() && elements.addBookAuthorText.text.isNotEmpty() && elements.addBookPriceText.text.isNotEmpty() && elements.addBookDescriptionText.text.isNotEmpty()) {
                // Упоковать их содержимое в BookModel
                val name: String = elements.addBookNameText.text.toString()
                val authorName: String = elements.addBookAuthorText.text.toString()
                val price: Int = Integer.parseInt(elements.addBookPriceText.text.toString())
                val description: String = elements.addBookDescriptionText.text.toString()

                val newBook: BookModel = BookModel(
                    intent.getIntExtra("lastID", 0) + 1,
                    name,
                    authorName,
                    price,
                    description
                )

                // Записать BookModel на сервер
                Thread {
                    sao.addNewBook(newBook)
                    finish()
                }.start()

                // Заморозить слой, пока данные грузятся на сервер
                elements.addNewBookButton.isEnabled = false
                elements.addBookCancelButton.isEnabled = false
                elements.addBookNameText.setBackgroundColor(resources.getColor(R.color.correct_edit_text))
                elements.addBookAuthorText.setBackgroundColor(resources.getColor(R.color.correct_edit_text))
                elements.addBookPriceText.setBackgroundColor(resources.getColor(R.color.correct_edit_text))
                elements.addBookDescriptionText.setBackgroundColor(resources.getColor(R.color.correct_edit_text))
            } else {
                // Если какое-то поле не заполнено, выделить его красным цветом
                if (elements.addBookNameText.text.isEmpty()) {
                    elements.addBookNameText.setBackgroundColor(resources.getColor(R.color.red))
                } else {
                    elements.addBookNameText.setBackgroundColor(resources.getColor(R.color.white))
                }
                if (elements.addBookAuthorText.text.isEmpty()) {
                    elements.addBookAuthorText.setBackgroundColor(resources.getColor(R.color.red))
                } else {
                    elements.addBookAuthorText.setBackgroundColor(resources.getColor(R.color.white))
                }
                if (elements.addBookPriceText.text.isEmpty()) {
                    elements.addBookPriceText.setBackgroundColor(resources.getColor(R.color.red))
                } else {
                    elements.addBookPriceText.setBackgroundColor(resources.getColor(R.color.white))
                }
                if (elements.addBookDescriptionText.text.isEmpty()) {
                    elements.addBookDescriptionText.setBackgroundColor(resources.getColor(R.color.red))
                } else {
                    elements.addBookDescriptionText.setBackgroundColor(resources.getColor(R.color.white))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        elements= ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(elements.root)

        initElements()

        addListenersToElements()
    }
}
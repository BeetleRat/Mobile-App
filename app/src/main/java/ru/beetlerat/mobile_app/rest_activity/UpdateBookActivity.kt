package ru.beetlerat.mobile_app.rest_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import ru.beetlerat.mobile_app.R
import ru.beetlerat.mobile_app.databinding.ActivityUpdateBookBinding
import ru.beetlerat.mobile_app.rest_activity.service.BookModel
import ru.beetlerat.mobile_app.rest_activity.service.ServerAccessObject

class UpdateBookActivity : AppCompatActivity() {
    // Переменная содержащая все объекты Activity Calculator layout
    private lateinit var elements: ActivityUpdateBookBinding

    // Объявление переменных
    private lateinit var sao: ServerAccessObject
    private var currentBook: BookModel?=null

    private fun initElements(){
        sao= ServerAccessObject(intent.getStringExtra("endpoint").toString())
        Thread{
            currentBook=sao.getBookByID(intent.getIntExtra("book_id",-1))
            if(currentBook==null){
                finish()
            }
            Handler(Looper.getMainLooper()).post{
                elements.updateBookNameText.setText(currentBook?.getName())
                elements.updateBookAuthorText.setText(currentBook?.getAuthorName())
                elements.updateBookPriceText.setText(currentBook?.getPrice().toString())
                elements.updateBookDescriptionText.setText(currentBook?.getDescription())
                elements.updateBookCancelButton.isEnabled=true
                elements.updateBookButton.isEnabled=true
            }
        }.start()
    }

    private fun addListenersToElements(){
        elements.updateBookCancelButton.setOnClickListener {
            finish()
        }

        elements.updateBookButton.setOnClickListener {
            if(elements.updateBookNameText.text.isNotEmpty()&&elements.updateBookAuthorText.text.isNotEmpty()&&elements.updateBookPriceText.text.isNotEmpty()&&elements.updateBookDescriptionText.text.isNotEmpty()&&currentBook!=null){
                val name:String = elements.updateBookNameText.text.toString()
                val authorName:String = elements.updateBookAuthorText.text.toString()
                val price:Int = Integer.parseInt(elements.updateBookPriceText.text.toString())
                val description:String = elements.updateBookDescriptionText.text.toString()

                val newBook: BookModel =
                    BookModel(
                        Integer.parseInt(currentBook?.getID().toString()),
                        name,
                        authorName,
                        price,
                        description
                    )

                Thread{
                    sao.updateBook(newBook,newBook.getID())
                    finish()
                }.start()

                elements.updateBookButton.isEnabled=false
                elements.updateBookCancelButton.isEnabled=false
                elements.updateBookNameText.setBackgroundColor(resources.getColor(R.color.correct_edit_text))
                elements.updateBookAuthorText.setBackgroundColor(resources.getColor(R.color.correct_edit_text))
                elements.updateBookPriceText.setBackgroundColor(resources.getColor(R.color.correct_edit_text))
                elements.updateBookDescriptionText.setBackgroundColor(resources.getColor(R.color.correct_edit_text))
            }
            else{
                if(elements.updateBookNameText.text.isEmpty()){
                    elements.updateBookNameText.setBackgroundColor(resources.getColor(R.color.red))
                }
                else{
                    elements.updateBookNameText.setBackgroundColor(resources.getColor(R.color.white))
                }
                if(elements.updateBookAuthorText.text.isEmpty()){
                    elements.updateBookAuthorText.setBackgroundColor(resources.getColor(R.color.red))
                }
                else{
                    elements.updateBookAuthorText.setBackgroundColor(resources.getColor(R.color.white))
                }
                if(elements.updateBookPriceText.text.isEmpty()){
                    elements.updateBookPriceText.setBackgroundColor(resources.getColor(R.color.red))
                }
                else{
                    elements.updateBookPriceText.setBackgroundColor(resources.getColor(R.color.white))
                }
                if(elements.updateBookDescriptionText.text.isEmpty()){
                    elements.updateBookDescriptionText.setBackgroundColor(resources.getColor(R.color.red))
                }
                else{
                    elements.updateBookDescriptionText.setBackgroundColor(resources.getColor(R.color.white))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        elements= ActivityUpdateBookBinding.inflate(layoutInflater)
        setContentView(elements.root)

        initElements()

        addListenersToElements()
    }
}
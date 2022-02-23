package ru.beetlerat.mobile_app.rest_activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ru.beetlerat.mobile_app.R
import ru.beetlerat.mobile_app.databinding.ActivityBooksListBinding
import ru.beetlerat.mobile_app.rest_activity.service.BookModel
import ru.beetlerat.mobile_app.rest_activity.service.BooksBuffer
import ru.beetlerat.mobile_app.rest_activity.service.RestConstant
import ru.beetlerat.mobile_app.rest_activity.service.ServerAccessObject


class BooksListActivity : AppCompatActivity() {
    // Переменная содержащая все объекты Activity Calculator layout
    private lateinit var elements: ActivityBooksListBinding

    // Объявление лаунчеров activity
    private lateinit var activityLauncher: ActivityResultLauncher<Intent>

    // Объявление переменных
    private lateinit var sao:ServerAccessObject
    private lateinit var books:BooksBuffer

    private fun initElements(){
        sao= ServerAccessObject(intent.getStringExtra("endpoint").toString())

        initBooks()

        // Делаем textView прокручиваемыми
        elements.bookNameText.setMovementMethod(ScrollingMovementMethod())
        elements.bookAuthorText.setMovementMethod(ScrollingMovementMethod())
        elements.bookDescriptionText.setMovementMethod(ScrollingMovementMethod())
    }

    private fun addListenersToElements(){
        elements.nextBookButton.setOnClickListener {
            if(books.isExist()){
                setNewBook(RestConstant.NEXT_BOOK)
            }
        }
        elements.prevBookButton.setOnClickListener {
            if(books.isExist()){
                setNewBook(RestConstant.PREVIOUS_BOOK)
            }
        }

        elements.addBookButton.setOnClickListener {
            val activityContext = Intent(this, AddBookActivity::class.java)
            // Передаем endpoint из родительского activity в дочернее
            activityContext.putExtra("endpoint",intent.getStringExtra("endpoint").toString())
            activityContext.putExtra("lastID",books.getLastID())
            activityLauncher.launch(activityContext)
        }

        elements.updateBookButton.setOnClickListener {
            if(books.isExist()) {
                val activityContext = Intent(this, UpdateBookActivity::class.java)
                // Передаем endpoint из родительского activity в дочернее
                activityContext.putExtra("endpoint", intent.getStringExtra("endpoint").toString())
                activityContext.putExtra("book_id", books.getCurrentBook()?.getID())
                activityLauncher.launch(activityContext)
            }
        }

        elements.deleteBookButton.setOnClickListener {
            elements.deleteBookButton.isEnabled=false
            elements.updateBookButton.isEnabled=false
            elements.nextBookButton.isEnabled=false
            elements.prevBookButton.isEnabled=false

            Thread{
                sao.deleteBookByID(books.getCurrentID())
                initBooks()
            }.start()
        }
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
        elements= ActivityBooksListBinding.inflate(layoutInflater)
        setContentView(elements.root)

        initElements()

        addListenersToElements()

        initActivityLaunchers()
    }

    override fun onResume() {
        super.onResume()
        initBooks()
    }

    private fun initBooks(){
        Thread{
            // Получаем с сервера список книг
            val booksList:ArrayList<BookModel> = sao.getAllBooks()
            books= BooksBuffer(booksList)
            // Заполняем layout в соответствии с полученными данными
            Handler(Looper.getMainLooper()).post{
                if(books.isExist()){
                   setNewBook(RestConstant.NEXT_BOOK)
                    elements.updateBookButton.isEnabled=true
                    elements.deleteBookButton.isEnabled=true
                    elements.nextBookButton.isEnabled=true
                    elements.prevBookButton.isEnabled=true
                }
                else{
                    elements.bookIdText.setText(getString(R.string.empty_database))
                    elements.bookNameText.setText(getString(R.string.empty_database))
                    elements.bookAuthorText.setText(getString(R.string.empty_database))
                    elements.bookPriceText.setText(getString(R.string.empty_database))
                    elements.bookDescriptionText.setText(getString(R.string.empty_database))
                    elements.updateBookButton.isEnabled=false
                    elements.deleteBookButton.isEnabled=false
                    elements.nextBookButton.isEnabled=false
                    elements.prevBookButton.isEnabled=false
                }
            }

        }.start()
    }

    private fun setNewBook(isNext:Boolean){
        val currentBook:BookModel?
        if(isNext){
            currentBook= books.next()
        }
        else{
            currentBook= books.back()
        }

        elements.bookIdText.setText(currentBook?.getID().toString())
        elements.bookNameText.setText(currentBook?.getName())
        elements.bookAuthorText.setText(currentBook?.getAuthorName())
        elements.bookPriceText.setText(currentBook?.getPrice().toString())
        elements.bookDescriptionText.setText(currentBook?.getDescription())
    }
}
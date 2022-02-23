package ru.beetlerat.mobile_app.rest_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ru.beetlerat.mobile_app.databinding.ActivityRestBinding
import ru.beetlerat.mobile_app.rest_activity.service.RestConstant
import ru.beetlerat.mobile_app.rest_activity.service.ServerAccessObject

class RestActivity : AppCompatActivity() {

    // Переменная содержащая все объекты Activity Calculator layout
    private lateinit var elements:ActivityRestBinding

    // Объявление лаунчеров activity
    private lateinit var activityLauncher: ActivityResultLauncher<Intent>

    private fun initElements(){
        elements.endpointEditText.setText(RestConstant.ENDPOINT)
        elements.connectionFailedText.visibility=View.INVISIBLE
        elements.conectionWaitingBar.visibility=View.INVISIBLE
    }

    private fun addListenersToElements(){
        elements.connectToServerButton.setOnClickListener {
            tryConnectToServer()
        }
    }

    private fun initActivityLaunchers(){
        activityLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result: ActivityResult ->
                finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        elements= ActivityRestBinding.inflate(layoutInflater)
        setContentView(elements.root)

        initElements()

        addListenersToElements()

        initActivityLaunchers()
    }

    private fun tryConnectToServer(){
        elements.connectionFailedText.visibility=View.INVISIBLE
        elements.conectionWaitingBar.visibility=View.VISIBLE
        elements.connectToServerButton.visibility=View.GONE

        Thread{
            val endpoint:String=elements.endpointEditText.text.toString()
            if(ServerAccessObject(endpoint).isConnection()){
                // Вернуться в основной поток
                Handler(Looper.getMainLooper()).post{
                    val activityContext = Intent(this, BooksListActivity::class.java)
                    activityContext.putExtra("endpoint",endpoint)
                    activityLauncher.launch(activityContext)
                }
            }else{
                // Вернуться в основной поток
                Handler(Looper.getMainLooper()).post{
                    elements.connectionFailedText.visibility=View.VISIBLE
                    elements.conectionWaitingBar.visibility=View.INVISIBLE
                    elements.connectToServerButton.visibility=View.VISIBLE
                }
            }
        }.start()
    }
}
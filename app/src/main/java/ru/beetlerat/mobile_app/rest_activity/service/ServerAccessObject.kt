package ru.beetlerat.mobile_app.rest_activity.service

import android.util.Log
import androidx.annotation.WorkerThread
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL


class ServerAccessObject(private val endpoint: String) {

    // Данный метод должен выполняться только в отдельном потоке
    @WorkerThread
    fun isConnection(): Boolean {
        // Создаем httpURLConnection по адресу endpoint + RestConstant.BOOKS_LIST_URL
        val httpURLConnection =
            URL(endpoint + RestConstant.BOOKS_LIST_URL).openConnection() as HttpURLConnection

        try {
            httpURLConnection.apply {
                connectTimeout = 10000
                requestMethod = "GET"
                doInput = true // Открыть inputStream
            }
            // Если от сервера не поступило ответа о том, что он жив
            if (httpURLConnection.responseCode != HttpURLConnection.HTTP_OK&&httpURLConnection.responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
                Log.e("internet", RestConstant.SERVER_NOT_ANSWER)
                return false
            }
            return true
        } catch (exception: SocketTimeoutException) {
            return false
        } catch (exception: Exception) {
            Log.e("internet", RestConstant.SERVER_EXCEPTION+": "+exception.toString())
        } finally {
            // Всегда закрываем открытое httpURLConnection
            httpURLConnection.disconnect()
        }
        return false
    }

    // Данный метод должен выполняться только в отдельном потоке
    @WorkerThread
    fun getBookByID(id: Int): BookModel? {
        var book: BookModel? = null
        // Создаем httpURLConnection по адресу endpoint + RestConstant.BOOKS_LIST_URL + "/${id}"
        val httpURLConnection =
            URL(endpoint + RestConstant.BOOKS_LIST_URL + "/${id}").openConnection() as HttpURLConnection

        try {
            httpURLConnection.apply {
                connectTimeout = 10000
                requestMethod = "GET"
                doInput = true // Открыть inputStream
            }
            if (httpURLConnection.responseCode != HttpURLConnection.HTTP_OK) {
                Log.e("internet", RestConstant.SERVER_NOT_ANSWER)
                return book
            }
            // Считываем из потока httpURLConnection text
            val streamReader = InputStreamReader(httpURLConnection.inputStream)
            var text: String = ""
            streamReader.use {
                text = it.readText()
            }

            // Преобразуем text в JSON объект
            val json = JSONObject(text)
            // Преобразуем JSON объект в BookModel
            val idFromJson = json.getInt(RestConstant.ID)
            val name = json.getString(RestConstant.NAME)
            val authorName = json.getString(RestConstant.AUTHOR_NAME)
            val price = json.getInt(RestConstant.PRICE)
            val description = json.getString(RestConstant.DESCRIPTION)
            book = BookModel(idFromJson, name, authorName, price, description)

        } catch (exception: SocketTimeoutException) {
            Log.e("internet", RestConstant.SERVER_NOT_FOUND)
            return book
        } catch (exception: Exception) {
            Log.e("internet", RestConstant.SERVER_EXCEPTION+": "+exception.toString())
        } finally {
            // Всегда закрываем открытое httpURLConnection
            httpURLConnection.disconnect()
        }
        return book
    }

    // Данный метод должен выполняться только в отдельном потоке
    @WorkerThread
    fun getAllBooks(): ArrayList<BookModel> {
        var booksList = ArrayList<BookModel>()
        // Создаем httpURLConnection по адресу endpoint + RestConstant.BOOKS_LIST_URL
        val httpURLConnection =
            URL(endpoint + RestConstant.BOOKS_LIST_URL).openConnection() as HttpURLConnection

        try {
            httpURLConnection.apply {
                connectTimeout = 10000
                requestMethod = "GET"
                doInput = true // Открыть inputStream
            }
            if (httpURLConnection.responseCode != HttpURLConnection.HTTP_OK) {
                Log.e("internet", RestConstant.SERVER_NOT_ANSWER)
                return booksList
            }
            // Считываем из потока httpURLConnection text
            val streamReader = InputStreamReader(httpURLConnection.inputStream)
            var text: String = ""
            streamReader.use {
                text = it.readText()
            }

            // Преобразуем text в массив JSON объектов
            val json = JSONArray(text)
            // Преобразуем массив JSON объектов в массив BookModel
            for (i in 0 until json.length()) {
                val jsonBook = json.getJSONObject(i)
                val id = jsonBook.getInt(RestConstant.ID)
                val name = jsonBook.getString(RestConstant.NAME)
                val authorName = jsonBook.getString(RestConstant.AUTHOR_NAME)
                val price = jsonBook.getInt(RestConstant.PRICE)
                val description = jsonBook.getString(RestConstant.DESCRIPTION)

                booksList.add(BookModel(id, name, authorName, price, description))
            }

        } catch (exception: SocketTimeoutException) {
            Log.e("internet", RestConstant.SERVER_NOT_FOUND)
            return booksList
        } catch (exception: Exception) {
            Log.e("internet", RestConstant.SERVER_EXCEPTION+": "+exception.toString())
        } finally {
            // Всегда закрываем открытое httpURLConnection
            httpURLConnection.disconnect()
        }
        return booksList
    }

    // Данный метод должен выполняться только в отдельном потоке
    @WorkerThread
    fun addNewBook(newBook: BookModel) {

        val requestBody = JSONObject().apply {
            put(RestConstant.ID, newBook.getID())
            put(RestConstant.NAME, newBook.getName())
            put(RestConstant.AUTHOR_NAME, newBook.getAuthorName())
            put(RestConstant.PRICE, newBook.getPrice())
            put(RestConstant.DESCRIPTION, newBook.getDescription())
        }
        // Создаем httpURLConnection по адресу endpoint + RestConstant.BOOKS_LIST_URL
        val httpURLConnection =
            URL(endpoint + RestConstant.BOOKS_LIST_URL).openConnection() as HttpURLConnection

        try {
            httpURLConnection.apply {
                connectTimeout = 10000
                requestMethod = "POST"
                doOutput = true // Открыть outputStream
                // Указание серваку, что мы на него отправляем
                setRequestProperty("Content-Type", "application/json")
            }
            // Отправляем на сервер requestBody в виде строки
            OutputStreamWriter(httpURLConnection.outputStream).use {
                it.write(requestBody.toString())
            }
            httpURLConnection.responseCode // Ждем ответа сервака

        } catch (exception: SocketTimeoutException) {
            Log.e("internet", RestConstant.SERVER_NOT_FOUND)
        } catch (exception: Exception) {
            Log.e("internet", RestConstant.SERVER_EXCEPTION+": "+exception.toString())
        } finally {
            // Всегда закрываем открытое httpURLConnection
            httpURLConnection.disconnect()
        }
    }

    // Данный метод должен выполняться только в отдельном потоке
    @WorkerThread
    fun updateBook(newBook: BookModel, id: Int) {
        val requestBody = JSONObject().apply {
            put(RestConstant.ID, newBook.getID())
            put(RestConstant.NAME, newBook.getName())
            put(RestConstant.AUTHOR_NAME, newBook.getAuthorName())
            put(RestConstant.PRICE, newBook.getPrice())
            put(RestConstant.DESCRIPTION, newBook.getDescription())
        }
        // Создаем httpURLConnection по адресу endpoint + RestConstant.BOOKS_LIST_URL
        val httpURLConnection =
            URL(endpoint + RestConstant.BOOKS_LIST_URL + "/${id}").openConnection() as HttpURLConnection

        try {

            httpURLConnection.apply {
                connectTimeout = 10000
                requestMethod = "PATCH"
                doOutput = true // Открыть outputStream
                // Указание серваку, что мы на него отправляем
                setRequestProperty("Content-Type", "application/json")
            }
            // Отправляем на сервер requestBody в виде строки
            OutputStreamWriter(httpURLConnection.outputStream).use {
                it.write(requestBody.toString())
            }
            httpURLConnection.responseCode // Ждем ответа сервака

        } catch (exception: SocketTimeoutException) {
            Log.e("internet", RestConstant.SERVER_NOT_FOUND)
        } catch (exception: Exception) {
            Log.e("internet", RestConstant.SERVER_EXCEPTION+": "+exception.toString())
        } finally {
            // Всегда закрываем открытое httpURLConnection
            httpURLConnection.disconnect()
        }
    }

    // Данный метод должен выполняться только в отдельном потоке
    @WorkerThread
    fun deleteBookByID(id: Int) {
        // Создаем httpURLConnection по адресу endpoint + RestConstant.BOOKS_LIST_URL + "/${id}"
        val httpURLConnection =
            URL(endpoint + RestConstant.BOOKS_LIST_URL + "/${id}").openConnection() as HttpURLConnection
        try {
            httpURLConnection.apply {
                connectTimeout = 10000
                requestMethod = "DELETE"
                //doInput = true // Открыть inputStream
            }
            if (httpURLConnection.responseCode != HttpURLConnection.HTTP_OK) {
                Log.e("internet", RestConstant.SERVER_NOT_ANSWER)
            }
            httpURLConnection.responseCode // Ждем ответа сервака
        } catch (exception: SocketTimeoutException) {
            Log.e("internet", RestConstant.SERVER_NOT_FOUND)
        } catch (exception: Exception) {
            Log.e("internet", RestConstant.SERVER_EXCEPTION+": "+exception.toString())
        } finally {
            // Всегда закрываем открытое httpURLConnection
            httpURLConnection.disconnect()
        }
    }
}
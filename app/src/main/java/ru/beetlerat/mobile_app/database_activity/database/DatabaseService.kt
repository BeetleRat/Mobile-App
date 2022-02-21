package ru.beetlerat.mobile_app.database_activity.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log

class DatabaseService(context: Context) {
    private val databaseManager:DatabaseManager= DatabaseManager(context)
    private var database:SQLiteDatabase?=null

    fun openDatabase(){
        // Открыть базу данных для записи
        database=databaseManager.writableDatabase
    }

    fun insertVerb(russian:String?, firstForm:String?, secondForm:String?, thirdForm:String?){
        if(russian!=null||firstForm!=null||secondForm!=null||thirdForm!=null){
            // Создаем объект переменных добавляемых в БД
            val values=ContentValues().apply {
                // put(название столбца, записываемое значение)
                if(russian!=null){
                    put(DataBaseConstants.Tables.Verbs.RUSSIAN_WORD_COLUMN,russian)
                }
                if(firstForm!=null){
                    put(DataBaseConstants.Tables.Verbs.FIRST_VERB_FORM_COLUMN,firstForm)
                }
                if(secondForm!=null){
                    put(DataBaseConstants.Tables.Verbs.SECOND_VERB_FORM_COLUMN,secondForm)
                }
                if(thirdForm!=null){
                    put(DataBaseConstants.Tables.Verbs.THIRD_VERB_FORM_COLUMN,thirdForm)
                }
            }

            // Добавляем переменные объекта в таблицу
            database?.insert(DataBaseConstants.Tables.Verbs.TABLE_NAME,null,values)
        }else{
            Log.e("database","Попытка записать пустые значения")
        }
    }

    fun deleteVerb(id: Int) {
        val deleteCondition:String="${BaseColumns._ID} = ${id}"
        // Удаляем из таблицы TABLE_NAME элемент, который удовлетворяет условию deleteCondition
        database?.delete(DataBaseConstants.Tables.Verbs.TABLE_NAME, deleteCondition, null)
    }

    // Анотация для исключения пустой БД
    @SuppressLint("Range")
    fun selectVerbsIDs():ArrayList<Int>{
        val requestResult=ArrayList<Int>()

        // Открываем курсор
        val cursor= database?.query(DataBaseConstants.Tables.Verbs.TABLE_NAME,
            null,null,null,null,null,null)
        // Проходим курсором по всем строкам БД
        while (cursor?.moveToNext()!!) {
            // Получаем ID
            val id=cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            requestResult.add(id)
        }

        // Закрываем курсор
        cursor.close()
        return requestResult
    }

    // Анотация для исключения пустой БД
    @SuppressLint("Range")
    fun selectAllVerbs():ArrayList<VerbModel>{

        val requestResult=ArrayList<VerbModel>()

        // Открываем курсор
        val cursor= database?.query(DataBaseConstants.Tables.Verbs.TABLE_NAME,
            null,null,null,null,null,null)
        // Проходим курсором по всем строкам БД
        while (cursor?.moveToNext()!!) {
            // Получаем данные из строки таблицы
            val id=cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            val russian=cursor.getString(cursor.getColumnIndex(DataBaseConstants.Tables.Verbs.RUSSIAN_WORD_COLUMN))
            val firstForm=cursor.getString(cursor.getColumnIndex(DataBaseConstants.Tables.Verbs.FIRST_VERB_FORM_COLUMN))
            val secondForm=cursor.getString(cursor.getColumnIndex(DataBaseConstants.Tables.Verbs.SECOND_VERB_FORM_COLUMN))
            val thirdForm=cursor.getString(cursor.getColumnIndex(DataBaseConstants.Tables.Verbs.THIRD_VERB_FORM_COLUMN))
            // Упаковываем эти данные в VerbModel и добавляем в список requestResult
            requestResult.add(VerbModel(id,russian,firstForm,secondForm,thirdForm))
        }
        // Закрываем курсор
        cursor.close()
        return requestResult
    }

    // Анотация для исключения пустой БД
    @SuppressLint("Range")
    fun selectVerbByID(id:Int):VerbModel?{

        val requestResult=ArrayList<VerbModel>()
        val selectCondition:String="${BaseColumns._ID} = ${id}"
        // Открываем курсор
        // в запросе к бд указываем ограничение selectCondition
        val cursor= database?.query(DataBaseConstants.Tables.Verbs.TABLE_NAME,
            null,selectCondition,null,null,null,null)
        // Проходим курсором по всем строкам БД
        while (cursor?.moveToNext()!!) {
            // Получаем данные из строки таблицы
            val id=cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            val russian=cursor.getString(cursor.getColumnIndex(DataBaseConstants.Tables.Verbs.RUSSIAN_WORD_COLUMN))
            val firstForm=cursor.getString(cursor.getColumnIndex(DataBaseConstants.Tables.Verbs.FIRST_VERB_FORM_COLUMN))
            val secondForm=cursor.getString(cursor.getColumnIndex(DataBaseConstants.Tables.Verbs.SECOND_VERB_FORM_COLUMN))
            val thirdForm=cursor.getString(cursor.getColumnIndex(DataBaseConstants.Tables.Verbs.THIRD_VERB_FORM_COLUMN))
            // Упаковываем эти данные в VerbModel и добавляем в список requestResult
            requestResult.add(VerbModel(id,russian,firstForm,secondForm,thirdForm))
        }
        // Закрываем курсор
        cursor.close()
        // Если мы нашли указанный id в базе,
        // то requestResult будет содержать 1 элемент
        if(requestResult.size>0){
            return requestResult[0]
        }
        else{
            Log.e("database","Поиск элемента по не существующему ID")
            return null
        }
    }

    fun updateVerbByID(id:Int,russian:String?, firstForm:String?, secondForm:String?, thirdForm:String?){

        val oldVerb=selectVerbByID(id)
        if(oldVerb!=null){
            if(russian!=null||firstForm!=null||secondForm!=null||thirdForm!=null){
                // Создаем объект переменных добавляемых в БД
                val values=ContentValues().apply {
                    // put(название столбца, записываемое значение)
                    if(russian!=null){
                        put(DataBaseConstants.Tables.Verbs.RUSSIAN_WORD_COLUMN,russian)
                    }
                    else{
                        put(DataBaseConstants.Tables.Verbs.RUSSIAN_WORD_COLUMN,oldVerb.getRussianVerb())
                    }
                    if(firstForm!=null){
                        put(DataBaseConstants.Tables.Verbs.FIRST_VERB_FORM_COLUMN,firstForm)
                    }
                    else{
                        put(DataBaseConstants.Tables.Verbs.FIRST_VERB_FORM_COLUMN,oldVerb.getFirstFormVerb())
                    }
                    if(secondForm!=null){
                        put(DataBaseConstants.Tables.Verbs.SECOND_VERB_FORM_COLUMN,secondForm)
                    }
                    else{
                        put(DataBaseConstants.Tables.Verbs.SECOND_VERB_FORM_COLUMN,oldVerb.getSecondFormVerb())
                    }
                    if(thirdForm!=null){
                        put(DataBaseConstants.Tables.Verbs.THIRD_VERB_FORM_COLUMN,thirdForm)
                    }
                    else{
                        put(DataBaseConstants.Tables.Verbs.THIRD_VERB_FORM_COLUMN,oldVerb.getThirdFormVerb())
                    }
                }

                // Обновляем БД
                val updateCondition:String="${BaseColumns._ID} = ${id}"
                // Обновляем в таблице TABLE_NAME элемент, который удовлетворяет условию updateCondition
                database?.update(DataBaseConstants.Tables.Verbs.TABLE_NAME,values,updateCondition,null)
            }else{
                Log.e("database","Попытка обновить эелемент пустыми значениями.")
            }
        }
        else{
            Log.e("database","Попытка обновить эелемент не существующий элемент.")
        }
    }

    fun closeDatabase(){
        databaseManager.close()
    }
}
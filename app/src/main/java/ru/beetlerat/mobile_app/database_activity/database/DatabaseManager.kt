package ru.beetlerat.mobile_app.database_activity.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseManager(context: Context):SQLiteOpenHelper(context,DataBaseConstants.Info.DATABASE_NAME,null,DataBaseConstants.Info.DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL(DataBaseConstants.Requests.CREATE_VERB_TABLE)
    }

    override fun onUpgrade(database: SQLiteDatabase?, p1: Int, p2: Int) {
        database?.execSQL(DataBaseConstants.Requests.DROP_VERB_TABLE)
        onCreate(database)
    }
}
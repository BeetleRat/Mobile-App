package ru.beetlerat.mobile_app.database_activity.database

import android.provider.BaseColumns

object DataBaseConstants{

    object Info{
        // При изменении структуры БД версию надо увеличивать
        const val DATABASE_VERSION=1
        const val DATABASE_NAME="Verbs.db"
    }

    object Tables{
        object Verbs {
            const val TABLE_NAME="verbs_table"
            const val RUSSIAN_WORD_COLUMN="russian_word"
            const val FIRST_VERB_FORM_COLUMN="first_form"
            const val SECOND_VERB_FORM_COLUMN="second_form"
            const val THIRD_VERB_FORM_COLUMN="third_form"
        }
    }


    object Requests{
        const val CREATE_VERB_TABLE="CREATE TABLE IF NOT EXISTS ${Tables.Verbs.TABLE_NAME}(" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${Tables.Verbs.RUSSIAN_WORD_COLUMN} TEXT," +
                "${Tables.Verbs.FIRST_VERB_FORM_COLUMN} TEXT," +
                "${Tables.Verbs.SECOND_VERB_FORM_COLUMN} TEXT," +
                "${Tables.Verbs.THIRD_VERB_FORM_COLUMN} TEXT" +
                ")"
        const val DROP_VERB_TABLE="DROP TABLE IF EXISTS ${Tables.Verbs.TABLE_NAME}"
    }
}
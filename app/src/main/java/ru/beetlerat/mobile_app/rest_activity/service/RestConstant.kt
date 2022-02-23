package ru.beetlerat.mobile_app.rest_activity.service

object RestConstant {
    const val ENDPOINT="http://10.0.2.2:8080"
    const val MAIN_PAGE_URL="/Rest"
    const val BOOKS_LIST_URL="/Rest/list"
    // Названия объектов в json
    const val ID="id"
    const val NAME="name"
    const val AUTHOR_NAME="authorName"
    const val PRICE="price"
    const val DESCRIPTION="descr"
    // Константы логов ошибок сервера
    const val SERVER_NOT_ANSWER="Ответ от сервера не получен"
    const val SERVER_NOT_FOUND="Не удалось подключиться к серверу"
    const val SERVER_EXCEPTION="Неизвестная ошибка сервера"
    // Константы смены элементов списка
    const val NEXT_BOOK=true
    const val PREVIOUS_BOOK=false
}
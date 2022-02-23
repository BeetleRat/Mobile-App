package ru.beetlerat.mobile_app.rest_activity.service

class BookModel(
    private var id: Int,
    private var name: String,
    private var authorName: String,
    private var price:Int,
    private var description: String
) {

    fun getID(): Int {
        return id
    }

    fun getName(): String {
        return name
    }

    fun getAuthorName(): String {
        return authorName
    }

    fun getPrice(): Int {
        return price
    }

    fun getDescription(): String {
        return description
    }

    fun setID(id: Int) {
        this.id = id
    }

    fun setName(name: String) {
        this.name = name
    }

    fun setAuthorName(authorName: String) {
        this.authorName = authorName
    }

    fun setPrice(price: Int){
        this.price=price
    }

    fun setDescription(description: String) {
        this.description = description
    }
}
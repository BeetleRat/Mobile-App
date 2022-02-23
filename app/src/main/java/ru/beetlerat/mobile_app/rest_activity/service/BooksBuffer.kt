package ru.beetlerat.mobile_app.rest_activity.service

class BooksBuffer(private var booksList:ArrayList<BookModel>) {
    private var index:Int

    init {
        index=-1
    }

    fun isExist():Boolean{
        if(booksList.size==0){
            return false
        }
        return true
    }

    fun next(): BookModel? {
        if(!isExist()){
            return null
        }
        if(index<booksList.size-1){
            index++
        }
        return booksList.get(index)
    }

    fun back(): BookModel? {
        if(!isExist()){
            return null
        }
        index--
        if(index<0){
            index=0
        }
        return booksList.get(index)
    }

    fun getCurrentBook():BookModel?{
        if(!isExist()){
            return null
        }
        return booksList[index]
    }

    fun getCurrentIndex():Int{
        if(!isExist()){
            return -1
        }
        return index
    }

    fun getCurrentID():Int{
        if(!isExist()){
            return -1
        }
        return booksList[index].getID()
    }

    fun getSize():Int{
        return booksList.size
    }

    fun getLastID(): Int {
        if (!isExist()) {
            return -1
        } else {
            return booksList.last().getID()
        }
    }

    fun setCurrentIndex(index:Int){
        if(index>booksList.size-1){
            this.index=booksList.size-1
        }else{
            if(index<0){
                this.index=0
            }else{
                this.index=index
            }
        }
    }

    fun setCurrentIndexByID(id:Int){
       for(book in booksList){
           if(book.getID()==id){
               index=booksList.lastIndexOf(book)
               break
           }
       }
    }
}
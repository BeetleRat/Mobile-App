package ru.beetlerat.mobile_app.database_activity.database

class VerbsIDBuffer(private var verbsIdList:ArrayList<Int>) {
    private var index:Int

    init {
        index=-1
    }

    fun isExist():Boolean{
        if(verbsIdList.size==0){
            return false
        }
        return true
    }

    fun next(): Int {
        if(!isExist()){
            return -1
        }
        if(index<verbsIdList.size-1){
            index++
        }
        return verbsIdList.get(index)
    }

    fun back(): Int {
        if(!isExist()){
            return -1
        }
        index--
        if(index<0){
            index=0
        }
        return verbsIdList.get(index)
    }

    fun getRandomID():Int{
        if(!isExist()){
            return -1
        }
        val randomIndex=(0..verbsIdList.size-1).random()
        return verbsIdList[randomIndex]
    }

    fun getCurrentID():Int{
        if(!isExist()){
            return -1
        }
        return verbsIdList[index]
    }

    fun getCurrentIndex():Int{
        if(!isExist()){
            return -1
        }
        return index
    }
    fun getSize():Int{
        return verbsIdList.size
    }

    fun deleteCurrentID(){
        verbsIdList.remove(verbsIdList.get(index))
    }



}
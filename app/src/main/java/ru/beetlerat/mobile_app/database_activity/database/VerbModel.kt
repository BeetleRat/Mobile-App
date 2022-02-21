package ru.beetlerat.mobile_app.database_activity.database

class VerbModel(private var id: Int,
                private var russianVerb: String,
                private var firstFormVerb:String,
                private var secondFormVerb:String,
                private var thirdFormVerb:String) {

    fun getID():Int{
        return id
    }
    fun getRussianVerb():String{
        return russianVerb
    }
    fun getFirstFormVerb():String{
        return firstFormVerb
    }
    fun getSecondFormVerb():String{
        return secondFormVerb
    }
    fun getThirdFormVerb():String{
        return thirdFormVerb
    }

    fun setID(id: Int){
        this.id=id
    }
    fun setRussianVerb(russianVerb: String){
        this.russianVerb=russianVerb
    }
    fun setFirstFormVerb(firstFormVerb: String){
        this.firstFormVerb=firstFormVerb
    }
    fun setSecondFormVerb(secondFormVerb: String){
        this.secondFormVerb=secondFormVerb
    }
    fun setThirdFormVerb(thirdFormVerb: String){
        this.thirdFormVerb=thirdFormVerb
    }

    override fun toString(): String {
        return "russianVerb: ${russianVerb}\n" +
                "firstFormVerb: ${firstFormVerb}\n" +
                "secondFormVerb: ${secondFormVerb}\n" +
                "thirdFormVerb: ${thirdFormVerb}\n"
    }
}
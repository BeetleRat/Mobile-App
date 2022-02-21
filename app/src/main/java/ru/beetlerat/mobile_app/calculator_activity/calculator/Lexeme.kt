package ru.beetlerat.mobile_app.calculator_activity.calculator

class Lexeme {
    private  var value: String
    private  var type: LexemeType

    constructor(value: String, type: LexemeType) {
        this.value = value
        this.type = type
    }
    constructor(value: Char, type: LexemeType) {
        this.value = value.toString()
        this.type = type
    }

    fun getValue():String{
        return value
    }
    fun getType():LexemeType{
        return type
    }

    override fun toString(): String {
        return "value: "+value.toString()+"; type: "+type.toString()
    }
}
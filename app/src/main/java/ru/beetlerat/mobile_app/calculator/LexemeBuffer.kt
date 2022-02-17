package ru.beetlerat.mobile_app.calculator

class LexemeBuffer {
    private var index:Int
    private var lexemeList:List<Lexeme>

    constructor( lexemeList: List<Lexeme>) {
        this.index = 0
        this.lexemeList = lexemeList
    }

    fun next():Lexeme{
        val lexeme:Lexeme=lexemeList.get(index)
        if(index<lexemeList.size-1){
            index++
        }
        return lexeme
    }

    fun back():Lexeme{
        index--
        if(index<0){
            index=0
        }
        return lexemeList.get(index)
    }

    fun getCurrentIndex():Int{
        return index
    }
    fun getSize():Int{
        return lexemeList.size
    }
}
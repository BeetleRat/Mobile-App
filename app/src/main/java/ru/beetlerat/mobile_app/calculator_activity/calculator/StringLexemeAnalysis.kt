package ru.beetlerat.mobile_app.calculator_activity.calculator

class StringLexemeAnalysis {
    // Статичные элементы класса
    companion object {
        // Статичная функция stringToLexemes
        // Разбивает строку с выражением на массив лексем этого выражения
        fun stringToLexemes(string: String): List<Lexeme> {
            var lexemes: ArrayList<Lexeme> = arrayListOf()
            var index: Int = 0
            var char: Char
            while (index < string.length) {
                char = string[index]
                when (char) {
                    '(' -> {
                        lexemes.add(Lexeme(char, LexemeType.LEFT_BRACKET))
                    }
                    ')' -> {
                        lexemes.add(Lexeme(char, LexemeType.RIGHT_BRACKET))
                    }
                    '+' -> {
                        lexemes.add(Lexeme(char, LexemeType.PLUS))
                    }
                    '-' -> {
                        lexemes.add(Lexeme(char, LexemeType.MINUS))
                    }
                    '*' -> {
                        lexemes.add(Lexeme(char, LexemeType.MULTIPLY))
                    }
                    '/' -> {
                        lexemes.add(Lexeme(char, LexemeType.DIVIDE))
                    }
                    else -> {
                        if (char <= '9' && char >= '0') {
                            var numberString: String = ""
                            while (char <= '9' && char >= '0') {
                                numberString = numberString + char
                                index++
                                if (index >= string.length) {
                                    break
                                }
                                char = string[index]
                            }
                            lexemes.add(Lexeme(numberString, LexemeType.NUMBER))
                            index--
                        }
                    }
                }
                index++
            }
            // Убираем лишние знаки в конце выражения
            index=lexemes.size-1
            while (lexemes.get(index).getType()!=LexemeType.NUMBER&&lexemes.get(index).getType()!=LexemeType.RIGHT_BRACKET&&index>=0){
                lexemes.remove(lexemes.get(index))
                index--
            }

            lexemes.add(Lexeme("", LexemeType.EOF))
            return lexemes
        }
    }
}
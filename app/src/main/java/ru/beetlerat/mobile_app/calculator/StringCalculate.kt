package ru.beetlerat.mobile_app.calculator

import android.util.Log

class StringCalculate {
    // Статичные элементы класса
    companion object {
        // Статичная функция calculate
        fun calculate(string: String): Double {
            // Получить LexemeBuffer из входной строки
            val lexemes: LexemeBuffer= LexemeBuffer(StringLexemeAnalysis.stringToLexemes(string))
            // Вернуть результат вычисления LexemeBuffer
            return expression(lexemes)
        }

        // Вычисление выражения
        private fun expression(lexemes:LexemeBuffer):Double{

            val currentLexeme:Lexeme= lexemes.next()

            if(currentLexeme.getType()==LexemeType.EOF){
                Log.e("lexeme","В выражении отсутствуют лексемы")
                return 0.0
            }
            else{
                lexemes.back()
                return plusOrMinus(lexemes)
            }
        }

        // После того как перемножили все минимальные части
        // выполняем сложение и вычитание результатов умножения/деления
        private fun plusOrMinus(lexemes:LexemeBuffer):Double{

            var currentValue:Double= multiplicationOrDivision(lexemes)

            // Обходим все выражение
            while (lexemes.getCurrentIndex()!=lexemes.getSize()-1){

                val currentLexeme:Lexeme= lexemes.next()
                when(currentLexeme.getType()){
                    LexemeType.PLUS->{
                        // Складываем текущий результат с следующим результатом умножения/деления
                        currentValue=currentValue+ multiplicationOrDivision(lexemes)
                    }
                    LexemeType.MINUS->{
                        // Вычитаем из текущего результата следующий результат умножения/деления
                        currentValue=currentValue-multiplicationOrDivision(lexemes)
                    }
                    else->{
                        lexemes.back()
                        return currentValue
                    }
                }
            }
            return currentValue
        }

        // В выражении сначала выполняются операции деления и умножения
        // минимальных составных частей
        private fun multiplicationOrDivision(lexemes:LexemeBuffer):Double{
            // Получаем минимальную составную часть
            var currentValue:Double= numberOrExpressionInBrackets(lexemes)

            // Обходим все выражение
            while (lexemes.getCurrentIndex()!=lexemes.getSize()-1){

                val currentLexeme:Lexeme= lexemes.next()
                when(currentLexeme.getType()){
                    LexemeType.MULTIPLY->{
                        // Умножаем текущий результат умножения/деления на следующую минимальную составную часть
                        currentValue=currentValue*numberOrExpressionInBrackets(lexemes)
                    }
                    LexemeType.DIVIDE->{
                        // Делим текущий результат умножения/деления на следующую минимальную составную часть
                        currentValue=currentValue/numberOrExpressionInBrackets(lexemes)
                    }
                    else->{
                        lexemes.back()
                        return currentValue
                    }
                }
            }
            return currentValue
        }

        // Минимальная составная часть выражения это цифра или выражение в скобках
        // эту минимальную часть мы будем использовать в более сложных
        private fun numberOrExpressionInBrackets(lexemes:LexemeBuffer):Double{

            val currentLexeme:Lexeme= lexemes.next()

            when(currentLexeme.getType()){
                LexemeType.NUMBER->{
                    return currentLexeme.getValue().toDouble()
                }
                LexemeType.LEFT_BRACKET->{
                    // Вычисляем выражение в скобках
                    val value:Double= expression(lexemes)
                    // Если нет закрывающей скобки
                    if(lexemes.next().getType()!=LexemeType.RIGHT_BRACKET){
                        Log.e("lexeme","Отсутствует закрывающая скобка")
                        return 0.0
                    }
                    return value
                }
                else->{
                    Log.e("lexeme","В минимальную состовную часть попала сложная лексема")
                    return 0.0
                }
            }
        }
    }
}
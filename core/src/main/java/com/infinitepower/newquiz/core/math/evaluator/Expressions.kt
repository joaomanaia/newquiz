package com.infinitepower.newquiz.core.math.evaluator

import com.infinitepower.newquiz.core.math.evaluator.internal.Function
import com.infinitepower.newquiz.core.math.evaluator.internal.Evaluator
import com.infinitepower.newquiz.core.math.evaluator.internal.Expr
import com.infinitepower.newquiz.core.math.evaluator.internal.Parser
import com.infinitepower.newquiz.core.math.evaluator.internal.Scanner
import com.infinitepower.newquiz.core.math.evaluator.internal.Token
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.log
import kotlin.math.log10
import kotlin.math.sqrt

class ExpressionException(message: String) : RuntimeException(message)

class Expressions {
    private val evaluator = Evaluator()

    init {
        define("π", Math.PI)
        define("e", Math.E)

        evaluator.addFunction("ln", object : Function() {
            override fun call(arguments: List<BigDecimal>): BigDecimal {
                if (arguments.size != 1)
                    throw ExpressionException("ln requires one argument")

                return log(arguments.first().toDouble(), Math.E).toBigDecimal()
            }
        })

        evaluator.addFunction("log", object : Function() {
            override fun call(arguments: List<BigDecimal>): BigDecimal {
                if (arguments.size != 1)
                    throw ExpressionException("log requires one argument")

                return log10(arguments.first().toDouble()).toBigDecimal()
            }
        })

        evaluator.addFunction("√", object : Function() {
            override fun call(arguments: List<BigDecimal>): BigDecimal {
                if (arguments.size != 1)
                    throw ExpressionException("square root requires one argument")

                return sqrt(arguments.first().toDouble()).toBigDecimal()
            }
        })
    }

    val precision: Int
        get() = evaluator.mathContext.precision

    val roundingMode: RoundingMode
        get() = evaluator.mathContext.roundingMode

    fun setPrecision(precision: Int): Expressions {
        evaluator.mathContext = MathContext(precision, roundingMode)


        return this
    }

    fun setRoundingMode(roundingMode: RoundingMode): Expressions {
        evaluator.mathContext = MathContext(precision, roundingMode)

        return this
    }

    fun define(name: String, value: Long): Expressions {
        define(name, value.toString())

        return this
    }

    fun define(name: String, value: Double): Expressions {
        define(name, value.toString())

        return this
    }

    fun define(name: String, value: BigDecimal): Expressions {
        define(name, value.toPlainString())

        return this
    }

    fun define(name: String, expression: String): Expressions {
        val expr = parse(expression)
        evaluator.define(name, expr)

        return this
    }

    fun addFunction(name: String, function: Function): Expressions {
        evaluator.addFunction(name, function)

        return this
    }

    fun addFunction(name: String, func: (List<BigDecimal>) -> BigDecimal): Expressions {
        evaluator.addFunction(name, object : Function() {
            override fun call(arguments: List<BigDecimal>): BigDecimal {
                return func(arguments)
            }

        })

        return this
    }

    fun eval(expression: String): BigDecimal {
        return evaluator.eval(parse(expression))
    }

    /**
     * eval an expression then round it with {@link Evaluator#mathContext} and call toEngineeringString <br>
     * if error will return message from Throwable
     * @param expression String
     * @return String
     */
    fun evalToString(expression: String): String {
        return try {
            evaluator.eval(parse(expression)).round(evaluator.mathContext).stripTrailingZeros()
                .toEngineeringString()
        }catch (e:Throwable){
            e.cause?.message ?: e.message ?: "unknown error"
        }
    }

    private fun parse(expression: String): Expr {
        return parse(scan(expression))
    }

    private fun parse(tokens: List<Token>): Expr {
        return Parser(tokens).parse()
    }

    private fun scan(expression: String): List<Token> {
        return Scanner(expression, evaluator.mathContext).scanTokens()
    }
}
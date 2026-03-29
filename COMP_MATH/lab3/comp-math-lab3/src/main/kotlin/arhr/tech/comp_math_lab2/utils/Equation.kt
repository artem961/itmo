package arhr.tech.comp_math_lab2.utils

import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Expression
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class Equation(
    val formula: String,
    val view: String,
    val id: Int
) {
    private val mc = MathContext(32, RoundingMode.HALF_UP)

   
    fun f(x: BigDecimal): BigDecimal {
        val argument = Argument("x = ${x.toPlainString()}")
        val e = Expression(formula, argument)
        return BigDecimal(e.calculate(), mc)
    }

    fun isSingularity(x: BigDecimal): Boolean {
        try {
            val value = f(x)
            return value.abs() > BigDecimal.valueOf(1e10)
        } catch (e: Exception) {
            return true
        }
    }
}
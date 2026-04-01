package arhr.tech.comp_math_lab2.utils

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class Equation(
    val formula: String,
    val view: String,
    val lambda: (Double) -> Double,
    val id: Int
) {
    private val mc = MathContext(32, RoundingMode.HALF_UP)


    fun f(x: BigDecimal): BigDecimal {
        return BigDecimal(lambda(x.toDouble()))
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
package arhr.tech.comp_math_lab2.utils

import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Expression
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class BiVariableEquation(
    val formula: String,
    val view: String
) {
    private val mc = MathContext(32, RoundingMode.HALF_UP)
    private val h = BigDecimal("0.00000001")
    private val twoH = h.multiply(BigDecimal("2"))


    fun f(x: BigDecimal, y: BigDecimal): BigDecimal {
        val xArg = Argument("x = ${x.toPlainString()}")
        val yArg = Argument("y = ${y.toPlainString()}")
        val e = Expression(formula, xArg, yArg)
        return BigDecimal(e.calculate(), mc)
    }


    fun dfdx(xVal: BigDecimal, yVal: BigDecimal): BigDecimal {
        val fPlusH = f(xVal.add(h), yVal)
        val fMinusH = f(xVal.subtract(h), yVal)
        return fPlusH.subtract(fMinusH).divide(twoH, mc)
    }


    fun dfdy(xVal: BigDecimal, yVal: BigDecimal): BigDecimal {
        val fPlusH = f(xVal, yVal.add(h))
        val fMinusH = f(xVal, yVal.subtract(h))
        return fPlusH.subtract(fMinusH).divide(twoH, mc)
    }
}
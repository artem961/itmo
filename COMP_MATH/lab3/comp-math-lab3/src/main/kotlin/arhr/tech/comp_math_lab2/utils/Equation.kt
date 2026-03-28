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

    fun df(x: BigDecimal): BigDecimal {
        val h = BigDecimal("0.00000001")

        val fPlusH = f(x.add(h))
        val fMinusH = f(x.subtract(h))
        val twoH = h.multiply(BigDecimal("2"))

        return fPlusH.subtract(fMinusH).divide(twoH, mc)
    }

    fun ddf(x: BigDecimal): BigDecimal {
        val h = BigDecimal("0.0001")

        val fPlusH = f(x.add(h))
        val fMinusH = f(x.subtract(h))
        val twoFX = f(x).multiply(BigDecimal("2"))
        val hSquare = h.multiply(h)

        return fPlusH.subtract(twoFX).add(fMinusH).divide(hSquare, mc)
    }

    fun checkFurie(x: BigDecimal): Boolean {
        return this.f(x).multiply(this.ddf(x)).signum() > 0
    }

    fun checkIntervalHasOnlyOneRoot(a: BigDecimal, b: BigDecimal) {
        if (f(a).multiply(f(b)).signum() > 0)
            throw Exception("На интервале нет корней либо их больше одного")

        val steps = 100
        val h = b.subtract(a).divide(BigDecimal(steps), mc)
        var signChanges = 0
        var monotonic = df(a).signum()
        var currentX = a

        for (i in 0 until steps) {
            val nextX = currentX.add(h)
            val f1 = f(currentX)
            val f2 = f(nextX)

            if (f1.signum() != f2.signum() && f1.signum() != 0) {
                signChanges++
            }

            if (df(currentX).signum() != monotonic) throw Exception("Функция не монотонна на промежутке")

            currentX = nextX
        }

        if (signChanges > 1) {
            throw RuntimeException("Обнаружено несколько корней на интервале")
        }
    }
}
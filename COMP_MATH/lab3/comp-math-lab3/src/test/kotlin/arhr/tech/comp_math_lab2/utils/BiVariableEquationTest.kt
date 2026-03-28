package arhr.tech.comp_math_lab2.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class BiVariableEquationTest {

    private val mc = MathContext(32, RoundingMode.HALF_UP)

    // Допустимая погрешность для численного дифференцирования
    private val delta = BigDecimal("0.000001")

    @Test
    fun `test simple quadratic function`() {
        val equation = BiVariableEquation("x^2 + y^2")
        val x = BigDecimal("1.0")
        val y = BigDecimal("2.0")

        val fVal = equation.f(x, y)
        assertAlmostEqual(BigDecimal("5.0"), fVal, "f(1,2) should be 5")

        val dfdx = equation.dfdx(x, y)
        assertAlmostEqual(BigDecimal("2.0"), dfdx, "df/dx at (1,2) should be 2")

        val dfdy = equation.dfdy(x, y)
        assertAlmostEqual(BigDecimal("4.0"), dfdy, "df/dy at (1,2) should be 4")
    }

    @Test
    fun `test variant 17 trigonometric function`() {
        val equation = BiVariableEquation("tan(x*y) - x^2")
        val x = BigDecimal("0.5")
        val y = BigDecimal("0.5")

        val fVal = equation.f(x, y)
        assertTrue(fVal.toDouble() > 0.0053 && fVal.toDouble() < 0.0054)


        val dfdy = equation.dfdy(x, y)
        assertAlmostEqual(BigDecimal("0.5326"), dfdy, "df/dy test", BigDecimal("0.0001"))
    }


    private fun assertAlmostEqual(
        expected: BigDecimal,
        actual: BigDecimal,
        message: String,
        tolerance: BigDecimal = delta
    ) {
        val diff = expected.subtract(actual).abs()
        assertTrue(
            diff < tolerance,
            "$message: Expected $expected, but got $actual (diff: $diff, tolerance: $tolerance)"
        )
    }
}
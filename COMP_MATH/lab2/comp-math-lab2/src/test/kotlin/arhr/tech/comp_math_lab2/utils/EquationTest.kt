package arhr.tech.comp_math_lab2.utils

import org.junit.jupiter.api.BeforeAll
import org.mariuszgromada.math.mxparser.License
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class EquationTest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            License.iConfirmNonCommercialUse("nigger")
        }
    }

    @Test
    fun f_ReturnsValidValue() {
        var equation = Equation("x^2 + 2x - 5")

        assertEquals(equation.f(BigDecimal.valueOf(-1)), BigDecimal.valueOf(-6))
    }

    @Test
    fun df_ReturnsValidValue() {
        var equation = Equation("x^2 + 2x - 5")

        val result = equation.df(BigDecimal.valueOf(2))
        val expected = BigDecimal.valueOf(6)

        val eps = BigDecimal.valueOf(0.0001)
        assertEquals(expected.minus(result).abs() <= eps, true)

    }

    @Test
    fun ddf_ReturnsValidValue() {
        var equation = Equation("x^3 + 2x - 5")

        val result = equation.ddf(BigDecimal.valueOf(1))
        val expected = BigDecimal.valueOf(6)

        println(result)
        println(expected)

        val eps = BigDecimal.valueOf(0.0001)
        assertEquals(expected.minus(result).abs() <= eps, true)
    }
}
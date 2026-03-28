package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.api.models.SolveIntegralResponse
import arhr.tech.comp_math_lab2.dto.SolveIntegralRequestDto
import arhr.tech.comp_math_lab2.utils.Equation
import java.math.BigDecimal
import java.math.MathContext

abstract class AbstractRungeIntegralSolver : IntegralSolver {
    abstract val rungeCoff: Int

    override fun solve(
        request: SolveIntegralRequestDto
    ): SolveIntegralResponse {
        val equation = request.equation
        val a = request.a
        val b = request.b
        val eps = request.eps

        var n = 4
        var iterations = 0
        var h = calcH(a, b, n)
        var value = methodImpl(equation, a, b, h)

        while (true) {
            iterations++
            n *= 2
            h = calcH(a, b, n)
            val newValue = methodImpl(equation, a, b, h)

            val error = newValue.subtract(value)
                .divide(
                    (BigDecimal.TWO.pow(rungeCoff).subtract(BigDecimal.ONE)), MathContext.DECIMAL32
                )
            value = newValue

            if (error < eps) {
                return createResponse(newValue, n)
            }

            if (iterations > 100000) {
                throw RuntimeException("Достигнут лимит итераций")
            }
        }
    }

    abstract fun methodImpl(
        eq: Equation,
        a: BigDecimal,
        b: BigDecimal,
        h: BigDecimal
    ): BigDecimal

    fun calcH(
        a: BigDecimal,
        b: BigDecimal,
        n: Int
    ): BigDecimal {
        return b.subtract(a).abs()
            .divide(BigDecimal(n), MathContext.DECIMAL32)
    }

    fun createResponse(value: BigDecimal, splits: Int): SolveIntegralResponse {
        return SolveIntegralResponse(value = value, splits = splits)
    }
}
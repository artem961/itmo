package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.PointDto
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ExponentialSolver : AbstractApproximationSolver(
    ApproximationType.EXPONENTIAL
) {

    override fun buildMatrix(points: List<PointDto>): Array<Array<BigDecimal>> {
        val n = points.size.toBigDecimal()
        val sumX = points.sumOf { it.x }
        val sumX2 = points.sumOf { it.x * it.x }

        return arrayOf(
            arrayOf(n, sumX),
            arrayOf(sumX, sumX2)
        )
    }

    override fun buildVector(points: List<PointDto>): Array<BigDecimal> {
        val sumY = points.sumOf { ln(it.y) }
        val sumXY = points.sumOf { it.x * ln(it.y) }

        return arrayOf(sumY, sumXY)
    }

    override fun evaluate(
        coefficients: List<BigDecimal>,
        x: BigDecimal
    ): BigDecimal {
        val a0 = coefficients[0]  // ln(a)
        val a1 = coefficients[1]  // b

        val exponent = a0 + a1 * x
        return exp(exponent)
    }

    override fun buildFormula(coefficients: List<BigDecimal>): String {
        val a = coefficients[0].round()
        val b = coefficients[1].round()

        val a0 = exp(a).round()
        return "y = ${a0}e^(${b}x)"
    }

    private fun exp(x: BigDecimal): BigDecimal {
        return BigDecimal(Math.exp(x.toDouble()), mc)
    }

    private fun ln(x: BigDecimal): BigDecimal {
        require(x > BigDecimal.ZERO) {
            "Экспоненциальная аппроксимация требует y > 0. Получено y = $x"
        }
        return BigDecimal(Math.log(x.toDouble()), mc)
    }
}
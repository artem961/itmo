package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.PointDto
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class LogarithmicSolver : AbstractApproximationSolver(
    ApproximationType.LOGARITHMIC,
) {

    override fun buildMatrix(points: List<PointDto>): Array<Array<BigDecimal>> {
        val n = points.size.toBigDecimal()

        val sumX = points.sumOf { ln(it.x) }
        val sumX2 = points.sumOf { ln(it.x) * ln(it.x) }

        return arrayOf(
            arrayOf(n, sumX),
            arrayOf(sumX, sumX2)
        )
    }

    override fun buildVector(points: List<PointDto>): Array<BigDecimal> {
        val sumY = points.sumOf { it.y }
        val sumXY = points.sumOf { ln(it.x) * it.y }

        return arrayOf(sumY, sumXY)
    }

    override fun evaluate(
        coefficients: List<BigDecimal>,
        x: BigDecimal
    ): BigDecimal {
        val a0 = coefficients[0]
        val a1 = coefficients[1]

        return a1 * ln(x) + a0
    }

    override fun buildFormula(coefficients: List<BigDecimal>): String {
        val a0 = coefficients[0].round()
        val a1 = coefficients[1].round()

        return when {
            a0 == BigDecimal.ZERO -> "y = ${a0}"
            a1 == BigDecimal.ZERO -> "y = ${a1}·ln(x)"
            else -> "y = ${a1}ln(x) + ${a0}"
        }
    }

    private fun ln(x: BigDecimal): BigDecimal {
        require(x > BigDecimal.ZERO) {
            "Логарифмическая аппроксимация требует x > 0. Получено x = $x"
        }
        return BigDecimal(Math.log(x.toDouble()), mc)
    }
}
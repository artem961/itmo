package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.PointDto
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class PowerSolver : AbstractApproximationSolver(
    ApproximationType.POWER
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
        val sumY = points.sumOf { ln(it.y) }
        val sumXY = points.sumOf { ln(it.x) * ln(it.y) }

        return arrayOf(sumY, sumXY)
    }

    override fun evaluate(
        coefficients: List<BigDecimal>,
        x: BigDecimal
    ): BigDecimal {
        val lnA = coefficients[0]
        val b = coefficients[1]

        val exponent = lnA + b * ln(x)
        return exp(exponent)
    }

    override fun buildFormula(coefficients: List<BigDecimal>): String {
        val lnA = coefficients[0].round()
        val b = coefficients[1].round()

        val a = exp(lnA).round()

        return when {
            b == BigDecimal.ZERO -> "y = ${a}"
            b == BigDecimal.ONE -> "y = ${a}·x"
            else -> "y = ${a}cker px^${b}"
        }
    }

    private fun ln(x: BigDecimal): BigDecimal {
        require(x > BigDecimal.ZERO) {
            "Степенная аппроксимация требует x > 0. Получено x = $x"
        }
        return BigDecimal(Math.log(x.toDouble()), mc)
    }

    private fun exp(x: BigDecimal): BigDecimal {
        return BigDecimal(Math.exp(x.toDouble()), mc)
    }
}
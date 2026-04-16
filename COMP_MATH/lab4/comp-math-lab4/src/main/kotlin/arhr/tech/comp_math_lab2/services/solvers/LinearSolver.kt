package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.PointDto
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class LinearSolver : AbstractApproximationSolver(
    type = ApproximationType.LINEAR
) {
    override fun buildMatrix(points: List<PointDto>): Array<Array<BigDecimal>> {
        val n = points.size.toBigDecimal()
        val sumX = points.sumOf { it.x }
        val sumX2 = points.sumOf { it.x * it.x }

        return arrayOf(
            arrayOf(sumX2, sumX),
            arrayOf(sumX, n)
        )
    }

    override fun buildVector(points: List<PointDto>): Array<BigDecimal> {
        val sumY = points.sumOf { it.y }
        val sumXY = points.sumOf { it.x * it.y }

        return arrayOf(sumXY, sumY)
    }

    override fun evaluate(
        coefficients: List<BigDecimal>,
        x: BigDecimal
    ): BigDecimal {
        val a = coefficients[0]
        val b = coefficients[1]
        return a * x + b
    }

    override fun buildFormula(coefficients: List<BigDecimal>): String {
        val a = coefficients[0].round()
        val b = coefficients[1].round()
        return "y = ${a}x + ${b}"
    }

}
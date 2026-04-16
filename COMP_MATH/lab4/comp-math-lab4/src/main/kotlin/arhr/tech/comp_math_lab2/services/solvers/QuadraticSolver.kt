package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.PointDto
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class QuadraticSolver : AbstractApproximationSolver(
    ApproximationType.QUADRATIC
) {
    override fun buildMatrix(points: List<PointDto>): Array<Array<BigDecimal>> {
        val n = points.size.toBigDecimal()
        val sumX = points.sumOf { it.x }
        val sumX2 = points.sumOf { it.x.pow(2, mc)}
        val sumX3 = points.sumOf { it.x.pow(3, mc)}
        val sumX4 = points.sumOf { it.x.pow(4, mc)}

        return arrayOf(
            arrayOf(n, sumX, sumX2),
            arrayOf(sumX, sumX2, sumX3),
            arrayOf(sumX2, sumX3, sumX4),
        )
    }

    override fun buildVector(points: List<PointDto>): Array<BigDecimal> {
        val sumY = points.sumOf { it.y }
        val sumXY = points.sumOf { it.x * it.y}
        val sumX2Y = points.sumOf { it.x * it.x * it.y}

        return arrayOf(
            sumY,
            sumXY,
            sumX2Y,
        )
    }

    override fun evaluate(
        coefficients: List<BigDecimal>,
        x: BigDecimal
    ): BigDecimal {
        val a0 = coefficients[0]
        val a1 = coefficients[1]
        val a2 = coefficients[2]

        return a2 * x * x + a1 * x + a0
    }

    override fun buildFormula(coefficients: List<BigDecimal>): String {
        val a0 = coefficients[0].round()
        val a1 = coefficients[1].round()
        val a2 = coefficients[2].round()

        return "y = ${a2}x² + ${a1}x + ${a0}"
    }
}
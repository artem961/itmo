package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.ApproximationRequestDto
import arhr.tech.comp_math_lab2.dto.ApproximationResult
import arhr.tech.comp_math_lab2.dto.PointDto
import arhr.tech.comp_math_lab2.utils.SystemSolver
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.sqrt


abstract class AbstractApproximationSolver(
    protected val type: ApproximationType
) : ApproximationSolver {

    protected val mc = MathContext.DECIMAL64
    protected val systemSolver = SystemSolver()

    protected abstract fun buildMatrix(points: List<PointDto>): Array<Array<BigDecimal>>

    protected abstract fun buildVector(points: List<PointDto>): Array<BigDecimal>

    protected abstract fun evaluate(coefficients: List<BigDecimal>, x: BigDecimal): BigDecimal

    protected abstract fun buildFormula(coefficients: List<BigDecimal>): String

    override fun solve(request: ApproximationRequestDto): ApproximationResult {
        val points = request.points
        val n = points.size.toBigDecimal()

        // матрица
        val matrix = buildMatrix(points)
        val vector = buildVector(points)
        val solution = systemSolver.solve(matrix, vector)
        val coefficients = solution.toList()

        // формула
        val func = buildFormula(coefficients)

        // S
        var s = BigDecimal.ZERO
        for (point in points) {
            val yPred = evaluate(coefficients, point.x)
            val error = yPred - point.y
            s += error * error
        }

        // ско
        val delta = if (s != BigDecimal.ZERO) {
            sqrt(s.divide(n, mc).toDouble()).toBigDecimal()
        } else {
            BigDecimal.ZERO
        }

        // determ
        val sumY = points.sumOf { it.y }
        val yMean = sumY.divide(n, mc)

        var sTotal = BigDecimal.ZERO
        for (point in points) {
            val diff = point.y - yMean
            sTotal += diff * diff
        }

        val determ = when {
            sTotal == BigDecimal.ZERO -> BigDecimal.ONE
            s == BigDecimal.ZERO -> BigDecimal.ONE
            else -> BigDecimal.ONE - s.divide(sTotal, mc)
        }

        // pearson
        val pearson = if (type == ApproximationType.LINEAR) {
            val sumX = points.sumOf { it.x }
            val xMean = sumX.divide(n, mc)

            var sumXdevYdev = BigDecimal.ZERO
            var sumXdev2 = BigDecimal.ZERO
            var sumYdev2 = BigDecimal.ZERO

            for (point in points) {
                val xDev = point.x - xMean
                val yDev = point.y - yMean
                sumXdevYdev += xDev * yDev
                sumXdev2 += xDev * xDev
                sumYdev2 += yDev * yDev
            }

            if (sumXdev2 != BigDecimal.ZERO && sumYdev2 != BigDecimal.ZERO) {
                sumXdevYdev.divide(
                    sqrt((sumXdev2 * sumYdev2).toDouble()).toBigDecimal(),
                    mc
                )
            } else {
                BigDecimal.ZERO
            }
        } else {
            null
        }

        return ApproximationResult(
            type = type,
            func = func,
            s = s.round(mc),
            determ = determ.round(mc),
            delta = delta.round(mc),
            pearson = pearson?.round(mc),
            coffs = coefficients.map { it.round(mc) }
        )
    }

    override fun supports(type: ApproximationType): Boolean = this.type == type


    protected fun BigDecimal.round(): BigDecimal {
        if (this.abs() <= BigDecimal("0.0000001")) return BigDecimal.ZERO
        return this.round(MathContext(4, RoundingMode.HALF_UP))
    }

    protected fun List<BigDecimal>.sumOf(selector: (BigDecimal) -> BigDecimal): BigDecimal {
        var result = BigDecimal.ZERO
        for (element in this) {
            result += selector(element)
        }
        return result
    }

    protected fun Int.pow(exp: Int): Int {
        var result = 1
        repeat(exp) { result *= this }
        return result
    }
}
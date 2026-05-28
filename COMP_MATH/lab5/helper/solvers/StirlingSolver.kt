package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.InterpolationRequestDto
import arhr.tech.comp_math_lab2.dto.InterpolationResult
import arhr.tech.comp_math_lab2.dto.PointDto
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Component
class StirlingSolver : InterpolationSolver {

    private val mc = MathContext(16, RoundingMode.HALF_UP)

    override fun solve(request: InterpolationRequestDto): InterpolationResult {
        val points = normalizePoints(request.points)
        val x = request.x
        val n = points.size

        require(n >= 3) { "Для метода Стирлинга нужно минимум 3 точки" }
        require(n % 2 == 1) { "Для метода Стирлинга нужно нечётное количество точек" }
        checkEquidistant(points)
        val h = points[1].x - points[0].x
        val diffs = InterpolationSolver.calculateFiniteDifferences(points)
        val centerIdx = n / 2
        val x0 = points[centerIdx].x
        val t = (x - x0).divide(h, mc)
        val tAbs = t.abs()
        val warning = if (tAbs > BigDecimal("0.25")) "Внимание: |t| = $tAbs > 0.25, рекомендуется использовать другие методы" else null
        val evaluate: (BigDecimal) -> BigDecimal = { calcX ->
            val tCalc = (calcX - x0).divide(h, mc)
            interpolateStirling(diffs, tCalc, centerIdx, n)
        }
        val result = evaluate(x)
        val graphicPoints = InterpolationSolver.generateGraphicPoints(points, evaluate)
        return InterpolationResult(
            type = InterpolationType.STIRLING,
            x = x,
            y = result,
            message = warning ?: "Метод Стирлинга, t = ${t.round()}",
            graphicPoints = graphicPoints
        )
    }

    override fun supports(type: InterpolationType): Boolean = type == InterpolationType.STIRLING

    private fun interpolateStirling(
        diffs: List<List<BigDecimal>>,
        t: BigDecimal,
        centerIdx: Int,
        n: Int
    ): BigDecimal {
        var result = diffs[0][centerIdx]
        val firstAvgDelta = (diffs[1][centerIdx - 1] + diffs[1][centerIdx]).divide(BigDecimal(2), mc)
        result = result.add(t.multiply(firstAvgDelta, mc))

        for (k in 2 until n) {
            val term = stirlingTerm(t, k)
            val deltaY = if (k % 2 == 0) {
                val m = k / 2
                diffs[k][centerIdx - m]
            } else {
                val m = (k - 1) / 2
                (diffs[k][centerIdx - m - 1] + diffs[k][centerIdx - m]).divide(BigDecimal(2), mc)
            }
            result = result.add(term.multiply(deltaY, mc).divide(factorial(k), mc))
        }
        return result
    }

    private fun stirlingTerm(t: BigDecimal, order: Int): BigDecimal {
        if (order == 1) return t
        val t2 = t.multiply(t, mc)
        if (order == 2) return t2

        var term = if (order % 2 == 0) t2 else t
        val limit = if (order % 2 == 0) order / 2 - 1 else (order - 1) / 2
        for (j in 1..limit) {
            val factor = t2.subtract(BigDecimal(j * j), mc)
            term = term.multiply(factor, mc)
        }
        return term
    }

    private fun factorial(k: Int): BigDecimal {
        var result = BigDecimal.ONE
        for (i in 2..k) {
            result = result.multiply(BigDecimal(i), mc)
        }
        return result
    }
}
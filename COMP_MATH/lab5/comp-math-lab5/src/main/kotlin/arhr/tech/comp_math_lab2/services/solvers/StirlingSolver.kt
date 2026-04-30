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
        val points = request.points
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
        var currentT = t
        val t2 = t.multiply(t, mc)
        val avgDeltaY = (diffs[1][centerIdx - 1] + diffs[1][centerIdx]).divide(BigDecimal(2), mc)
        result = result.add(currentT.multiply(avgDeltaY, mc))

        var tPower = currentT
        var t2Power = BigDecimal.ONE
        for (k in 2 until n) {
            val isEven = k % 2 == 0
            if (isEven) {
                val m = k / 2
                t2Power = if (m == 1) t2 else t2Power.multiply(t2.subtract((m - 1).toBigDecimal().pow(2, mc), mc), mc)
                val deltaY = diffs[k][centerIdx - m]
                val factorial = factorial(k)
                result = result.add(t2Power.multiply(deltaY, mc).divide(factorial, mc))
            } else {
                val m = (k - 1) / 2
                val avgDeltaY2 = (diffs[k][centerIdx - m - 1] + diffs[k][centerIdx - m]).divide(BigDecimal(2), mc)
                tPower = if (m == 0) tPower.multiply(t2.subtract(BigDecimal.ONE, mc), mc) else tPower.multiply(t2.subtract(m.toBigDecimal().pow(2, mc), mc), mc)
                val factorial = factorial(k)
                result = result.add(tPower.multiply(avgDeltaY2, mc).divide(factorial, mc))
            }
        }
        return result
    }

    private fun factorial(k: Int): BigDecimal {
        var result = BigDecimal.ONE
        for (i in 2..k) {
            result = result.multiply(BigDecimal(i), mc)
        }
        return result
    }
}
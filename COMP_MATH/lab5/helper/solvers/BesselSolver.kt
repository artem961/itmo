package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.InterpolationRequestDto
import arhr.tech.comp_math_lab2.dto.InterpolationResult
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Component
class BesselSolver : InterpolationSolver {

    private val mc = MathContext(16, RoundingMode.HALF_UP)

    override fun solve(request: InterpolationRequestDto): InterpolationResult {
        val points = normalizePoints(request.points)
        val x = request.x
        val n = points.size

        require(n >= 4) { "Для метода Бесселя нужно минимум 4 точки" }
        require(n % 2 == 0) { "Для метода Бесселя нужно чётное количество точек" }

        checkEquidistant(points)
        val h = points[1].x - points[0].x
        val diffs = InterpolationSolver.calculateFiniteDifferences(points)

        val idx0 = n / 2 - 1
        val x0 = points[idx0].x
        val t = (x - x0).divide(h, mc)
        val tAbs = (t - BigDecimal("0.5")).abs()
        val warning = if (tAbs > BigDecimal("0.25")) "Внимание: t = $t, рекомендуется t ≈ 0.5 для метода Бесселя" else null
        val evaluate: (BigDecimal) -> BigDecimal = { calcX ->
            val tCalc = (calcX - x0).divide(h, mc)
            interpolateBessel(diffs, tCalc, idx0, n)
        }
        val result = evaluate(x)
        val graphicPoints = InterpolationSolver.generateGraphicPoints(points, evaluate)
        return InterpolationResult(
            type = InterpolationType.BESSEL,
            x = x,
            y = result,
            message = warning ?: "Метод Бесселя, t = ${t.round()}",
            graphicPoints = graphicPoints
        )
    }

    override fun supports(type: InterpolationType): Boolean = type == InterpolationType.BESSEL

    private fun interpolateBessel(
        diffs: List<List<BigDecimal>>,
        t: BigDecimal,
        idx0: Int,
        n: Int
    ): BigDecimal {
        val y0 = diffs[0][idx0]
        val y1 = diffs[0][idx0 + 1]
        var result = y0.add(y1, mc).divide(BigDecimal(2), mc)
        for (k in 1 until n) {
            val term = besselTerm(t, k)
            val deltaY = if (k == 1) {
                diffs[1][idx0]
            } else if (k % 2 == 0) {
                val m = k / 2
                (diffs[k][idx0 - m] + diffs[k][idx0 - m + 1]).divide(BigDecimal(2), mc)
            } else {
                val m = (k - 1) / 2
                diffs[k][idx0 - m]
            }
            result = result.add(term.multiply(deltaY, mc).divide(factorial(k), mc))
        }
        return result
    }

    private fun besselTerm(t: BigDecimal, order: Int): BigDecimal {
        val half = BigDecimal("0.5")
        if (order == 1) return t.subtract(half, mc)
        if (order == 2) return t.multiply(t.subtract(BigDecimal.ONE, mc), mc)

        var term = if (order % 2 == 0) {
            t.multiply(t.subtract(BigDecimal.ONE, mc), mc)
        } else {
            t.subtract(half, mc).multiply(t, mc).multiply(t.subtract(BigDecimal.ONE, mc), mc)
        }

        val maxPair = order / 2
        for (pair in 2..maxPair) {
            val left = t.subtract(BigDecimal(pair), mc)
            val right = t.add((pair - 1).toBigDecimal(), mc)
            term = term.multiply(left, mc).multiply(right, mc)
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
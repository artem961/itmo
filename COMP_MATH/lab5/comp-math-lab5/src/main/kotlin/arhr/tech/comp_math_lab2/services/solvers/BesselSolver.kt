package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.InterpolationRequestDto
import arhr.tech.comp_math_lab2.dto.InterpolationResult
import arhr.tech.comp_math_lab2.dto.PointDto
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Component
class BesselSolver : InterpolationSolver {

    private val mc = MathContext(16, RoundingMode.HALF_UP)

    override fun solve(request: InterpolationRequestDto): InterpolationResult {
        val points = request.points
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
        val deltaY0 = diffs[1][idx0]
        val tMinusHalf = t.subtract(BigDecimal("0.5"), mc)
        result = result.add(tMinusHalf.multiply(deltaY0, mc))
        val tMinus1 = t.subtract(BigDecimal.ONE, mc)
        val tTMinus1 = t.multiply(tMinus1, mc)
        val avgDelta2 = (diffs[2][idx0 - 1] + diffs[2][idx0]).divide(BigDecimal(2), mc)
        result = result.add(tTMinus1.multiply(avgDelta2, mc).divide(BigDecimal(2), mc))
        if (n >= 5 && idx0 - 1 >= 0) {
            val delta3 = diffs[3][idx0 - 1]
            val tFactor = tMinusHalf.multiply(t, mc).multiply(tMinus1, mc)
            result = result.add(tFactor.multiply(delta3, mc).divide(BigDecimal(6), mc))
        }
        if (n >= 6 && idx0 - 2 >= 0) {
            val tPlus1 = t.add(BigDecimal.ONE, mc)
            val tMinus2 = t.subtract(BigDecimal(2), mc)
            val tFactor = t.multiply(tMinus1, mc).multiply(tPlus1, mc).multiply(tMinus2, mc)
            val avgDelta4 = (diffs[4][idx0 - 2] + diffs[4][idx0 - 1]).divide(BigDecimal(2), mc)
            result = result.add(tFactor.multiply(avgDelta4, mc).divide(BigDecimal(24), mc))
        }
        return result
    }

    /**
     * Строит формулу для отображения
     */
}
package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.InterpolationRequestDto
import arhr.tech.comp_math_lab2.dto.InterpolationResult
import arhr.tech.comp_math_lab2.dto.PointDto
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Component
class GaussSolver : InterpolationSolver {

    private val mc = MathContext(16, RoundingMode.HALF_UP)

    override fun solve(request: InterpolationRequestDto): InterpolationResult {
        val points = normalizePoints(request.points)
        val x = request.x
        val n = points.size

        require(n >= 3) { "Для метода Гаусса нужно минимум 3 точки" }
        require(n % 2 == 1) { "Для симметричной интерполяции Гаусса рекомендуется нечётное число точек" }
        checkEquidistant(points)
        val h = points[1].x - points[0].x
        val diffs = InterpolationSolver.calculateFiniteDifferences(points)
        val centerIdx = n / 2
        val x0 = points[centerIdx].x
        val t = (x - x0).divide(h, mc)
        val useForward = t >= BigDecimal.ZERO
        val evaluate: (BigDecimal) -> BigDecimal = { calcX ->
            val tCalc = (calcX - x0).divide(h, mc)
            interpolateGauss(diffs, tCalc, centerIdx, n, tCalc >= BigDecimal.ZERO)
        }
        val result = evaluate(x)
        val graphicPoints = InterpolationSolver.generateGraphicPoints(points, evaluate)
        val tDisplay = t.setScale(4, RoundingMode.HALF_UP).toPlainString()
        return InterpolationResult(
            type = InterpolationType.GAUSS,
            x = x,
            y = result,
            message = "Метод Гаусса, ${if (useForward) "первая формула (t = $tDisplay)" else "вторая формула (t = $tDisplay)"}, центральный узел x₀ = $x0",
            graphicPoints = graphicPoints
        )
    }

    override fun supports(type: InterpolationType): Boolean = type == InterpolationType.GAUSS

    private fun interpolateGauss(
        diffs: List<List<BigDecimal>>,
        t: BigDecimal,
        centerIdx: Int,
        n: Int,
        useForward: Boolean
    ): BigDecimal {
        var result = diffs[0][centerIdx]
        val firstDeltaY = if (useForward) diffs[1][centerIdx] else diffs[1][centerIdx - 1]
        result = result.add(t.multiply(firstDeltaY, mc))

        for (k in 2 until n) {
            val term = if (useForward) gaussFirstTerm(t, k) else gaussSecondTerm(t, k)
            val deltaY = if (useForward) {
                val offset = if (k % 2 == 0) k / 2 else (k - 1) / 2
                diffs[k][centerIdx - offset]
            } else {
                val offset = if (k % 2 == 0) k / 2 else (k + 1) / 2
                diffs[k][centerIdx - offset]
            }
            result = result.add(term.multiply(deltaY, mc).divide(factorial(k, mc), mc))
        }
        return result
    }

    private fun gaussFirstTerm(t: BigDecimal, order: Int): BigDecimal {
        var term = t
        for (k in 2..order) {
            val factor = if (k % 2 == 0) {
                t.subtract((k / 2).toBigDecimal(), mc)
            } else {
                t.add(((k - 1) / 2).toBigDecimal(), mc)
            }
            term = term.multiply(factor, mc)
        }
        return term
    }

    private fun gaussSecondTerm(t: BigDecimal, order: Int): BigDecimal {
        var term = t
        for (k in 2..order) {
            val factor = if (k % 2 == 0) {
                t.add((k / 2).toBigDecimal(), mc)
            } else {
                t.subtract(((k - 1) / 2).toBigDecimal(), mc)
            }
            term = term.multiply(factor, mc)
        }
        return term
    }

}
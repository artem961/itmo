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
        val points = request.points
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
        var term = t
        val firstDeltaY = if (useForward) diffs[1][centerIdx] else diffs[1][centerIdx - 1]
        result = result.add(term.multiply(firstDeltaY, mc))
        var currentT = t
        for (k in 2 until n) {
            val isEven = k % 2 == 0
            val offset = if (useForward) if (isEven) k / 2 else k / 2 else (k + 1) / 2
            val deltaY = diffs[k][centerIdx - offset]
            currentT = if (isEven) {
                val m = (k / 2).toBigDecimal()
                currentT.multiply(t.multiply(t, mc).subtract(m.multiply(m, mc)), mc)
            } else {
                currentT.multiply(t, mc)
            }
            if (deltaY != BigDecimal.ZERO) {
                val factorialValue = factorial(k, mc)
                val coeff = currentT.multiply(deltaY, mc).divide(factorialValue, mc)
                result = result.add(coeff)
            }
        }
        return result
    }

}
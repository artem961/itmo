package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.InterpolationRequestDto
import arhr.tech.comp_math_lab2.dto.InterpolationResult
import arhr.tech.comp_math_lab2.dto.PointDto
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Component
class NewtonSolver : InterpolationSolver {

    private val mc = MathContext(16, RoundingMode.HALF_UP)

    override fun solve(request: InterpolationRequestDto): InterpolationResult {
        val points = normalizePoints(request.points)
        val x = request.x
        val n = points.size

        checkEquidistant(points)
        val h = points[1].x - points[0].x

        val diffs = InterpolationSolver.calculateFiniteDifferences(points)
        val evaluate: (BigDecimal) -> BigDecimal = { calcX ->
            val useFwd = (calcX - points[0].x).abs() <= (calcX - points.last().x).abs()
            val tCalc = if (useFwd) (calcX - points[0].x).divide(h, mc) else (calcX - points.last().x).divide(h, mc)
            var res = if (useFwd) points[0].y else points.last().y
            var termCalc = BigDecimal.ONE
            for (k in 1 until n) {
                val delta = if (useFwd) tCalc - BigDecimal(k - 1) else tCalc + BigDecimal(k - 1)
                termCalc = termCalc.multiply(delta, mc)
                val deltaY = if (useFwd) diffs[k][0] else diffs[k][n - 1 - k]
                val fact = factorial(k, mc)
                res = res.add(termCalc.multiply(deltaY, mc).divide(fact, mc))
            }
            res
        }
        val useForward = (x - points[0].x).abs() <= (x - points.last().x).abs()
        val result = evaluate(x)
        val graphicPoints = InterpolationSolver.generateGraphicPoints(points, evaluate)
        return InterpolationResult(
            type = InterpolationType.NEWTON,
            x = x,
            y = result,
            message = "Использовано интерполирование " + if (useForward) "вперёд" else "назад",
            graphicPoints = graphicPoints
        )
    }

    override fun supports(type: InterpolationType): Boolean = type == InterpolationType.NEWTON

}
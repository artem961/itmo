package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.InterpolationRequestDto
import arhr.tech.comp_math_lab2.dto.InterpolationResult
import arhr.tech.comp_math_lab2.dto.PointDto
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Component
class LagranzhSolver : InterpolationSolver {

    private val mc = MathContext(16, RoundingMode.HALF_UP)

    override fun solve(request: InterpolationRequestDto): InterpolationResult {
        val points = request.points
        val n = points.size

        val evaluate: (BigDecimal) -> BigDecimal = { calcX ->
            var res = BigDecimal.ZERO
            for (i in 0 until n) {
                var li = BigDecimal.ONE
                for (j in 0 until n) {
                    if (j != i) {
                        val numerator = calcX - points[j].x
                        val denominator = points[i].x - points[j].x
                        require(denominator != BigDecimal.ZERO) { "Значения X не могут быть одинаковыми" }
                        li = (li * numerator).divide(denominator, mc)
                    }
                }
                res += points[i].y * li
            }
            res
        }
        val result = evaluate(request.x)
        val graphicPoints = InterpolationSolver.generateGraphicPoints(points, evaluate)
        return InterpolationResult(
            type = InterpolationType.LAGRANZH,
            x = request.x.round(),
            y = result.round(),
            message = null,
            graphicPoints = graphicPoints
        )
    }

    override fun supports(type: InterpolationType): Boolean {
        return type == InterpolationType.LAGRANZH
    }
}

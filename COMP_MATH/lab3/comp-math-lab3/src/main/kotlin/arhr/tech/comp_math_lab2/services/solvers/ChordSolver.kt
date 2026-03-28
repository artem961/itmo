package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.api.models.SolveEquationRequest
import arhr.tech.comp_math_lab2.api.models.SolveEquationResponse
import arhr.tech.comp_math_lab2.data.EquationRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Component
class ChordSolver(
    val equationRepository: EquationRepository
) : EquationSolver {
    override fun solve(request: SolveEquationRequest): SolveEquationResponse {
        val a = request.a ?: throw RuntimeException("Граница a не указана")
        val b = request.b ?: throw RuntimeException("Граница b не указана")
        val eps = request.eps ?: throw RuntimeException("Точность не указана")
        val equation = equationRepository.getById(request.equationId)
        val mc = MathContext(32, RoundingMode.HALF_UP)
        var i = 0
        equation.checkIntervalHasOnlyOneRoot(a, b)

        var fixed: BigDecimal = b
        var x: BigDecimal = a
        if (equation.checkFurie(a)) {
            fixed = a
            x = b
        }


        do {
            val fx = equation.f(x)


            // nextX = x - ((fixed - x) / (fFixed - fx)) * fx

            val correction = (fixed.subtract(x))
                .divide(equation.f(fixed).subtract(equation.f(x)), mc)

            val nextX = x.subtract(correction.multiply(equation.f(x)))

            val diff = nextX.subtract(x).abs()
            x = nextX
            i++

            if (i > 1000) throw Exception("Достигнут лимит итераций. Метод не сошёлся")
        } while (diff >= eps)

        return createResponse(x, eps, i, equation);
    }

    override fun supports(type: SolveType): Boolean {
        return type == SolveType.CHORD
    }
}
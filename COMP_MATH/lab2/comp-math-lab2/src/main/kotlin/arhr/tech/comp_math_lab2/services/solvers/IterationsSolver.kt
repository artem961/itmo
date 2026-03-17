package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.api.models.SolveEquationRequest
import arhr.tech.comp_math_lab2.api.models.SolveEquationResponse
import arhr.tech.comp_math_lab2.data.EquationRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Component
class IterationsSolver(
    val equationRepository: EquationRepository
) : EquationSolver {
    override fun solve(request: SolveEquationRequest): SolveEquationResponse {
        val a = request.a ?: throw RuntimeException("Граница a не указана")
        val b = request.b ?: throw RuntimeException("Граница b не указана")
        val eps = request.eps ?: throw RuntimeException("Точность не указана")
        val equation = equationRepository.getById(request.equationId)
        val mc = MathContext(32, RoundingMode.HALF_UP)
        equation.checkIntervalHasOnlyOneRoot(a, b)


        val dfa = equation.df(a)
        val dfb = equation.df(b)

        val maxDf = if (dfa.abs() > dfb.abs()) dfa else dfb

        val lambda = BigDecimal.ONE.divide(maxDf, mc).negate()

        val qA = BigDecimal.ONE.add(lambda.multiply(dfa)).abs()
        val qB = BigDecimal.ONE.add(lambda.multiply(dfb)).abs()
        val q = if (qA > qB) qA else qB

        if (q >= BigDecimal.ONE) {
            throw RuntimeException("Условие сходимости не выполнено q >= 1. Попробуйте другой интервал.")
        }

        var i = 0
        var x = a
        var diff: BigDecimal

        do {
            val fx = equation.f(x)

            // nextX = x + lambda * f(x)
            val nextX = x.add(lambda.multiply(fx))

            diff = nextX.subtract(x).abs()

            x = nextX
            i++

            if (i > 1000) throw Exception("Достигнут лимит итераций. Метод не сошёлся")
        } while (diff >= eps)

        return createResponse(x, eps, i, equation);
    }

    override fun supports(type: SolveType): Boolean {
        return type == SolveType.ITERATIONS
    }
}
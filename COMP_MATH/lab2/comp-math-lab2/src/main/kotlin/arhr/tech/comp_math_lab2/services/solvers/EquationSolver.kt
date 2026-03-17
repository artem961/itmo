package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.api.models.SolveEquationRequest
import arhr.tech.comp_math_lab2.api.models.SolveEquationResponse
import arhr.tech.comp_math_lab2.utils.Equation
import java.math.BigDecimal
import java.math.RoundingMode

interface EquationSolver {
    fun solve(request: SolveEquationRequest): SolveEquationResponse
    fun supports(type: SolveType): Boolean

    fun createResponse(x: BigDecimal, eps: BigDecimal, iterations: Int, equation: Equation): SolveEquationResponse{
        return SolveEquationResponse(
            x.setScale(eps.scale(), RoundingMode.HALF_UP),
            equation.f(x).setScale(eps.scale(), RoundingMode.HALF_UP),
            iterations,
            null
        )
    }
}
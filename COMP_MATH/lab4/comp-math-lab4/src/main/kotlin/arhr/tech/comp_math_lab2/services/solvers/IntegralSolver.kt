package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.api.models.SolveIntegralResponse
import arhr.tech.comp_math_lab2.dto.SolveIntegralRequestDto
import arhr.tech.comp_math_lab2.utils.Equation
import java.math.BigDecimal
import java.math.RoundingMode

interface IntegralSolver {
    fun solve(request: SolveIntegralRequestDto): SolveIntegralResponse
    fun supports(type: SolveType): Boolean
}
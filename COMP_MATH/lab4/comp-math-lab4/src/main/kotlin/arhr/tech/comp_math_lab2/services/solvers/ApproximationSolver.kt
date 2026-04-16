package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.ApproximationRequestDto
import arhr.tech.comp_math_lab2.dto.ApproximationResult

interface ApproximationSolver {
    fun solve(request: ApproximationRequestDto): ApproximationResult
    fun supports(type: ApproximationType): Boolean
}
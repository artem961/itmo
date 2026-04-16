package arhr.tech.comp_math_lab2.api.models

import arhr.tech.comp_math_lab2.dto.ApproximationResult
import arhr.tech.comp_math_lab2.services.solvers.ApproximationType

data class ApproximationResponse(
    val results: List<ApproximationResult>?,
    val best: ApproximationType? = null,
    val error: String? = null
)

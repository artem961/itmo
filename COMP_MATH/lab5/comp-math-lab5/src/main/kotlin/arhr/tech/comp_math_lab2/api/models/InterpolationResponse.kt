package arhr.tech.comp_math_lab2.api.models

import arhr.tech.comp_math_lab2.dto.InterpolationResult
import java.math.BigDecimal

data class InterpolationResponse(
    val results: List<InterpolationResult>?,
    val diffTable: List<List<BigDecimal>>? = null,
    val error: String? = null
)

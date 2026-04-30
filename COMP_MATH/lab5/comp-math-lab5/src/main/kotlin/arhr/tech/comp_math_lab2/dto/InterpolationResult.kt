package arhr.tech.comp_math_lab2.dto

import arhr.tech.comp_math_lab2.services.solvers.InterpolationType
import java.math.BigDecimal

data class InterpolationResult(
    val type: InterpolationType,
    val x: BigDecimal?,
    val y: BigDecimal?,
    val message: String?,
    var graphicPoints: List<PointDto>? = null
)

package arhr.tech.comp_math_lab2.dto

import arhr.tech.comp_math_lab2.services.solvers.ApproximationType
import java.math.BigDecimal

data class ApproximationResult(
    val type: ApproximationType,
    val func: String,
    val s: BigDecimal,
    val determ: BigDecimal,
    val delta: BigDecimal,
    val pearson: BigDecimal? = null,
    val coffs: List<BigDecimal>,
)

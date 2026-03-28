package arhr.tech.comp_math_lab2.api.models

import java.math.BigDecimal

data class SolveIntegralResponse(
    val value: BigDecimal? = null,
    val splits: Int? = null,
    val error: String? = null
)

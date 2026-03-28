package arhr.tech.comp_math_lab2.api.models

import java.math.BigDecimal

data class SolveEquationResponse(
    val root: BigDecimal? = null,
    val funcValue: BigDecimal? = null,
    val iterations: Int? = null,
    val error: String? = null
)
